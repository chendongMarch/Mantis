package com.zfy.mantis.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.zfy.mantis.annotation.Lookup;

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
import javax.lang.model.type.MirroredTypeException;
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
        return Collections.singleton(Lookup.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 被注解的所有元素
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Lookup.class);
        // 分类成 Map<TypeElement, List<VariableElement>>
        // 也就是 类 + 底下的注解元素
        Map<TypeElement, List<VariableElement>> categories = categories(elements);
        // 所有类
        Set<TypeElement> typeElements = categories.keySet();
        // 解析生成对应得类
        for (TypeElement typeElement : typeElements) {

            List<VariableElement> variableElements = categories.get(typeElement);
            if (variableElements == null) {
                continue;
            }
            // 生成类
            Name originClazzName = typeElement.getSimpleName();
            TypeSpec.Builder builder = TypeSpec.classBuilder(originClazzName + MantisConst.CLASS_SUFFIX)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ClassName.bestGuess(MantisConst.SYRINGE_INTERFACE));
            // 首先注入父类
            boolean hasParent = false;
            TypeElement parentType = findParentType(typeElement, typeElements);
            if (parentType != null) {
                hasParent = true;
                // debug = parentType.toString();
                builder.superclass(ClassName.bestGuess(parentType.toString() + MantisConst.CLASS_SUFFIX));
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
        String lookupOptsClassName = "com.zfy.mantis.annotation.LookupOpts";
        String dataProviderFactoryClassName = "com.zfy.mantis.api.provider.IDataProviderFactory";

        String lookOptsName = "lookupOpts";

        MethodSpec.Builder builder = MethodSpec.methodBuilder(MantisConst.METHOD_NAME);
        // 调用 super 方法
        if (hasParent) {
            builder.addStatement("super." + MantisConst.METHOD_NAME + "(" + MantisConst.METHOD_PARAM_GROUP + "," + MantisConst.METHOD_PARAM_TARGET + ")");
        }

        // 添加通用的数据源
        ClassName mantisClass = ClassName.bestGuess(mantisClassName);
        builder.returns(void.class)
                .addAnnotation(Override.class)
                .addParameter(TypeName.INT, MantisConst.METHOD_PARAM_GROUP)
                .addParameter(Object.class, MantisConst.METHOD_PARAM_TARGET)
                .addModifiers(Modifier.PUBLIC)
                .addComment("初始化 opts")
                .addStatement("$T $L = $T.obtainOpts()", ClassName.bestGuess(lookupOptsClassName), lookOptsName, mantisClass)
                .addStatement("$L.setTarget($L);", lookOptsName, MantisConst.METHOD_PARAM_TARGET)
                .addComment("数据获取工厂函数")
                .addStatement("$T factory = $T.getDataProviderFactory()", ClassName.bestGuess(dataProviderFactoryClassName), mantisClass)
                .addStatement("$T dataProvider = factory.create($L)", ClassName.bestGuess(dataAwClassName), MantisConst.METHOD_PARAM_TARGET)
                .addStatement("$T objProvider = $T.getObjProvider();", ClassName.bestGuess(objAwClassName), mantisClass)
                .addStatement("$T thiz = ($T) $L", typeElement.asType(), typeElement.asType(), MantisConst.METHOD_PARAM_TARGET);


        Map<Integer, List<VariableElement>> map = new HashMap<>();

        // 循环绑定数据
        for (VariableElement variableElement : variableElements) {
            Lookup annotation = variableElement.getAnnotation(Lookup.class);
            if (annotation == null) {
                continue;
            }
            int type = annotation.group();
            if (type <= 0 && type != Lookup.DEF_GROUP) {
                throw new RuntimeException("group " + type + " must > 0");
            }
            List<VariableElement> list = map.get(type);
            if (list == null) {
                list = new ArrayList<>();
            }
            if (!list.contains(variableElement)) {
                list.add(variableElement);
            }
            map.put(type, list);
        }
        for (Integer key : map.keySet()) {
            List<VariableElement> list = map.get(key);
            builder.beginControlFlow("if ($L == $L)", MantisConst.METHOD_PARAM_GROUP, key);
            for (VariableElement variableElement : list) {
                Lookup annotation = variableElement.getAnnotation(Lookup.class);
                if (annotation == null) {
                    continue;
                }
                Name varName = variableElement.getSimpleName();
                TypeName typeName = TypeName.get(variableElement.asType());
                String typeStr = getTypeStr(variableElement);
                if (isNotEmpty(annotation.desc())) {
                    builder.addComment(annotation.desc());
                }
                if ((null == typeStr || typeStr.equalsIgnoreCase("Parcelable")) && annotation.obj()) {
                    typeStr = null;
                }
                if (null == typeStr) {
                    // obj


                    builder
                            .addStatement("$L.setAnnotation(\"$L\",$L, $T.class)",
                                    lookOptsName,
                                    varName,
                                    key,
                                    getClazz(annotation))
                            .addStatement("$L.setField($T.class,\"$L\")",
                                    lookOptsName,
                                    variableElement.asType(),
                                    varName)
                            .addStatement("thiz.$L = ($T)objProvider.getObject($L)",
                                    varName,
                                    variableElement.asType(),
                                    lookOptsName);
                } else {
                    // data
                    if (typeStr.equalsIgnoreCase("Parcelable")) {
                        builder.addStatement("thiz.$L = dataProvider.get$L(\"$L\")",
                                varName,
                                typeStr,
                                annotation.value());
                    } else {
                        // data
                        builder.addStatement("thiz.$L = dataProvider.get$L(\"$L\", thiz.$L)",
                                varName,
                                typeStr,
                                annotation.value(),
                                varName);
                    }
                }
                if (annotation.required()) {
                    if (!typeName.isPrimitive()) {
                        builder.beginControlFlow("if(thiz.$L == null)", varName)
                                .addStatement("throw new RuntimeException(\"<$L.$L> is null\")", typeElement.getSimpleName(), varName)
                                .endControlFlow();
                    }
                }
            }
            builder.endControlFlow();
        }

        return builder.build();
    }

    private boolean isNotEmpty(CharSequence charSequence) {
        return charSequence != null && charSequence.length() > 0;
    }

    private TypeMirror getClazz(Lookup annotation) {
        try {
            annotation.clazz();
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
        return null;
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
            TypeMirror parcelableType = processingEnv.getElementUtils().getTypeElement(MantisConst.PARCELABLE).asType();
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
