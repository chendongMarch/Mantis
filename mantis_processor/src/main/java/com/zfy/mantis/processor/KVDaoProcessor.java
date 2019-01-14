package com.zfy.mantis.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.zfy.mantis.annotation.KVDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * CreateAt : 2019/1/11
 * Describe :
 *
 * @author chendong
 */

@AutoService(Processor.class)
public class KVDaoProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(KVDao.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 被注解的所有元素
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(KVDao.class);
        // 因为是加在 class 上的注解，所以可以保证是 TypeElement
        List<TypeElement> typeElements = new ArrayList<>();

        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            // 获取当前的类名 UserInfo
            Name simpleName = typeElement.getSimpleName();
            // 获取所有的成员
            List<? extends Element> allMembers = processingEnv.getElementUtils().getAllMembers(typeElement);
            // 生成 UserInfoDao
            TypeSpec.Builder builder = TypeSpec.classBuilder(simpleName + "Dao");

            // 添加获取 preference
            builder.addMethod(MethodSpec.methodBuilder("getPreference")
                    .addModifiers(Modifier.FINAL, Modifier.STATIC, Modifier.PRIVATE)
                    .returns(ClassName.bestGuess("android.content.SharedPreferences"))
                    .addStatement("return $T.getPreferences()", ClassName.bestGuess("com.zfy.mantis.library.Mantis"))
                    .build());

            // 添加获取 parser
            builder.addMethod(MethodSpec.methodBuilder("getParser")
                    .addModifiers(Modifier.FINAL, Modifier.STATIC, Modifier.PRIVATE)
                    .returns(ClassName.bestGuess("com.zfy.mantis.library.SerializeParser"))
                    .addStatement("return $T.getParser()", ClassName.bestGuess("com.zfy.mantis.library.Mantis"))
                    .build());

            for (Element member : allMembers) {
                if (member instanceof VariableElement) {
                    VariableElement variableElement = (VariableElement) member;

                    // 添加 put 方法
                    builder.addMethod(createPutter(typeElement, variableElement));
                    builder.addMethod(createGetter(typeElement, variableElement));
                    builder.addMethod(createRemover(typeElement, variableElement));
                    MethodSpec defaultGetter = createDefaultGetter(typeElement, variableElement);
                    if (defaultGetter != null) {
                        builder.addMethod(defaultGetter);
                    }
                }
            }

            JavaFile javaFile = JavaFile.builder(processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString(), builder.build()).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    private MethodSpec createDefaultGetter(TypeElement typeElement, VariableElement member) {
        String memberName = member.getSimpleName().toString();
        TypeName typeName = TypeName.get(member.asType());
        String defaultValue = getDefaultValue(typeName);
        if (defaultValue != null && !isObjType(typeName)) {
            return MethodSpec.methodBuilder("get" + StringX.capitalize(memberName))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .returns(typeName)
                    .addCode(String.format("return get%s(%s);", StringX.capitalize(memberName), defaultValue))
                    .addCode("\n")
                    .build();
        }
        return null;
    }

    private MethodSpec createGetter(TypeElement typeElement, VariableElement member) {
        String memberName = member.getSimpleName().toString();
        TypeName typeName = TypeName.get(member.asType());
        MethodSpec.Builder builder = MethodSpec.methodBuilder("get" + StringX.capitalize(memberName))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .returns(typeName);
        if (isObjType(typeName)) {
            builder.addStatement(String.format("$T json = getPreference().getString(\"%s_\" + \"%s\", \"\")", typeElement.getSimpleName(), memberName), String.class);
            if (typeName.toString().startsWith("java.util.List")) {
                ParameterizedTypeName parameterType = (ParameterizedTypeName) ParameterizedTypeName.get(member.asType());
                builder.addCode(String.format("return getParser().toList(json, %s.class);", parameterType.typeArguments.get(0)));
            } else if (typeName.toString().startsWith("java.util.Map")) {
                ParameterizedTypeName parameterType = (ParameterizedTypeName) ParameterizedTypeName.get(member.asType());
                builder.addCode(String.format("return getParser().toMap(json, %s.class, %s.class);", parameterType.typeArguments.get(0), parameterType.typeArguments.get(1)));
            } else {
                builder.addCode(String.format("return getParser().toObj(json, %s.class);", typeName.toString()));
            }

        } else {
            String defValue = "def" + StringX.capitalize(memberName);
            builder.addParameter(typeName, defValue)
                    .returns(typeName)
                    .addCode(String.format("return getPreference().get%s(\"%s_\" + \"%s\", %s);", getKvName(typeName), typeElement.getSimpleName(), memberName, defValue));
        }
        return builder.addCode("\n").build();
    }

    // 添加 put 方法
    private MethodSpec createPutter(TypeElement typeElement, VariableElement member) {
        String memberName = member.getSimpleName().toString();
        TypeName typeName = TypeName.get(member.asType());
        MethodSpec.Builder builder = MethodSpec.methodBuilder("put" + StringX.capitalize(memberName))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addParameter(typeName, memberName);
        if (isObjType(typeName)) {
            builder.addStatement(String.format("$T json = getParser().toJson(%s)", memberName), String.class)
                    .addCode(String.format("getPreference().edit().putString(\"%s_\" + \"%s\", json);", typeElement.getSimpleName(), memberName));
        } else {
            builder.addCode(String.format("getPreference().edit().put%s(\"%s_\" + \"%s\", %s);", getKvName(typeName), typeElement.getSimpleName(), memberName, memberName));

        }
        return builder.addCode("\n").build();
    }

    // 添加 put 方法
    private MethodSpec createRemover(TypeElement typeElement, VariableElement member) {
        String memberName = member.getSimpleName().toString();
        TypeName typeName = TypeName.get(member.asType());
        MethodSpec.Builder builder = MethodSpec.methodBuilder("remove" + StringX.capitalize(memberName))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addParameter(typeName, memberName)
                .addCode(String.format("getPreference().edit().remove(\"%s_\" + \"%s\");", typeElement.getSimpleName(), memberName));
        return builder.addCode("\n").build();
    }

    private boolean isObjType(TypeName name) {
        if (name.isPrimitive()) {
            return false;
        } else if (name.toString().equals("java.lang.String")) {
            return false;
        } else {
            return true;
        }
    }

    private String getKvName(TypeName name) {
        if (name.isPrimitive()) {
            return StringX.capitalize(name.toString());
        } else if (name.toString().equals("java.lang.String")) {
            return "String";
        } else {
            return "Obj";
        }
    }


    private String getDefaultValue(TypeName name) {
        if (name.isPrimitive()) {
            if (name.equals(TypeName.BYTE) || name.equals(TypeName.SHORT) || name.equals(TypeName.INT) || name.equals(TypeName.FLOAT) || name.equals(TypeName.LONG) || name.equals(TypeName.DOUBLE)) {
                return "0";
            } else if (name.equals(TypeName.BOOLEAN)) {
                return "false";
            }
        } else if (name.toString().equals("java.lang.String")) {
            return "";
        } else if (name.isBoxedPrimitive()) {
            return getDefaultValue(name.unbox());
        }
        return null;
    }
}
