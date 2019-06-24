package com.zfy.mantis.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.zfy.mantis.annotation.LookupArgs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * CreateAt : 2019/1/29
 * Describe :
 *
 * @author chendong
 */
@AutoService(Processor.class)
public class AutowiredProcessor extends BaseAbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(LookupArgs.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 被注解的所有元素
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(LookupArgs.class);
        // 分类成 Map<TypeElement, List<VariableElement>>
        // 也就是 类 + 底下的注解元素
        Map<TypeElement, List<VariableElement>> categories = categories(elements);
        // 所有类
        Set<TypeElement> typeElements = categories.keySet();
        // 解析生成对应得类
        for (TypeElement typeElement : typeElements) {
            // debug = "";
            List<VariableElement> variableElements = categories.get(typeElement);
            if (variableElements == null) {
                continue;
            }

            // 生成类
            Name originClazzName = typeElement.getSimpleName();
            TypeSpec.Builder builder = TypeSpec.classBuilder(originClazzName + MantisConsts.CLASS_SUFFIX)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ClassName.bestGuess(MantisConsts.ISYRINGE_CLASS_NAME));

            boolean hasParent = false;
            TypeElement parentType = findParentType(typeElement, typeElements);
            if (parentType != null) {
                hasParent = true;
                // debug = parentType.toString();
                builder.superclass(ClassName.bestGuess(parentType.toString() + MantisConsts.CLASS_SUFFIX));
            }
            builder.addMethod(createInjectMethod(hasParent, typeElement, variableElements));

            TypeSpec typeSpec = builder.build();
            JavaFile javaFile = JavaFile.builder(processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString(), typeSpec).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private String debug = "";

    private MethodSpec createInjectMethod(boolean hasParent, TypeElement typeElement, List<VariableElement> variableElements) {
        String dataAwClassName = "com.zfy.mantis.api.provider.IDataProvider";
        String objAwClassName = "com.zfy.mantis.api.provider.IObjProvider";
        String mantisClassName = "com.zfy.mantis.api.Mantis";
        String callbackClassName = "com.zfy.mantis.api.provider.ProviderCallback";
        MethodSpec.Builder builder = MethodSpec.methodBuilder(MantisConsts.ISYRINGE_METHOD_NAME);
        if (hasParent) {
            builder.addStatement("super." + MantisConsts.ISYRINGE_METHOD_NAME + "(" + MantisConsts.ISYRINGE_METHOD_PARAM_NAME + ")");
        }
        builder.returns(void.class)
                .addComment(debug)
                .addAnnotation(Override.class)
                .addParameter(Object.class, MantisConsts.ISYRINGE_METHOD_PARAM_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$T callback = $T.getProviderCallback()", ClassName.bestGuess(callbackClassName), ClassName.bestGuess(mantisClassName))
                .addStatement("$T dataProvider = callback.getDataProvider(target)", ClassName.bestGuess(dataAwClassName))
                .addStatement("$T objProvider = callback.getObjProvider(target,dataProvider)", ClassName.bestGuess(objAwClassName))
                .addStatement("$T thiz = ($T) target", typeElement.asType(), typeElement.asType());
        // 循环绑定数据
        for (VariableElement variableElement : variableElements) {
            LookupArgs annotation = variableElement.getAnnotation(LookupArgs.class);
            if (annotation == null) {
                continue;
            }
            Name varName = variableElement.getSimpleName();
            TypeName typeName = TypeName.get(variableElement.asType());
            String typeStr = getTypeStr(variableElement);
            if (isNotEmpty(annotation.desc())) {
                builder.addComment(annotation.desc());
            }
            if (null == typeStr) {
                // obj
                builder.addStatement(String.format("thiz.%s = (%s)objProvider.getObject(\"%s\", %s.class)",
                        varName,
                        typeName,
                        annotation.value(),
                        typeName));
            } else {
                // data
                if (typeStr.equalsIgnoreCase("Parcelable")) {
                    builder.addStatement(String.format("thiz.%s = dataProvider.get%s(\"%s\")",
                            varName,
                            typeStr,
                            annotation.value()));
                } else {
                    // data
                    builder.addStatement(String.format("thiz.%s = dataProvider.get%s(\"%s\", thiz.%s)",
                            varName,
                            typeStr,
                            annotation.value(),
                            varName));
                }
            }
            if (annotation.required()) {
                if (!typeName.isPrimitive()) {
                    builder.beginControlFlow(String.format("if(thiz.%s == null)", varName))
                            .addStatement(String.format("throw new RuntimeException(\"<%s.%s> is null\")", typeElement.getSimpleName(), varName))
                            .endControlFlow();
                }
            }
        }
        return builder.build();
    }

    private boolean isNotEmpty(CharSequence charSequence) {
        return charSequence != null && charSequence.length() > 0;
    }


    private String getTypeStr(VariableElement element) {
        TypeName typeName = TypeName.get(element.asType());
        if (typeName.isBoxedPrimitive()) {
            return AptUtil.capitalize(typeName.unbox().toString());
        } else if (typeName.isPrimitive()) {
            return AptUtil.capitalize(typeName.toString());
        } else if (typeName.toString().equals("java.lang.String")) {
            return "String";
        } else {
            Types typeUtils = processingEnv.getTypeUtils();
            TypeMirror parcelableType = processingEnv.getElementUtils().getTypeElement(MantisConsts.PARCELABLE).asType();
            if (typeUtils.isSubtype(element.asType(), parcelableType)) {
                return "Parcelable";
            }
        }
        return null;
    }


    private TypeElement findParentType(TypeElement typeElement, Set<TypeElement> parents) {
        TypeMirror type;
        while (true) {
            type = typeElement.getSuperclass();
            if (type.getKind() == TypeKind.NONE) {
                return null;
            }
            typeElement = (TypeElement) ((DeclaredType) type).asElement();
            if (parents.contains(typeElement)) {
                return typeElement;
            }
        }
    }


    private Map<TypeElement, List<VariableElement>> categories(Set<? extends Element> elements) {
        Map<TypeElement, List<VariableElement>> category = new HashMap<>();
        for (Element element : elements) {
            // 只允许注解在 field 上
            if (!(element instanceof VariableElement)) {
                continue;
            }
            VariableElement variableElement = (VariableElement) element;
            Element enclosingElement = variableElement.getEnclosingElement();
            // 如果包含他的不是 TypeElement 也是不允许的
            if (!(enclosingElement instanceof TypeElement)) {
                continue;
            }
            TypeElement typeElement = (TypeElement) enclosingElement;
            if (category.containsKey(typeElement)) {
                category.get(typeElement).add(variableElement);
            } else {
                ArrayList<VariableElement> list = new ArrayList<>();
                list.add(variableElement);
                category.put(typeElement, list);
            }
        }
        return category;
    }
}
