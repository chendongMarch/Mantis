package com.zfy.mantis.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.zfy.mantis.annotation.MView;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.lang.model.util.Elements;

/**
 * CreateAt : 2019/1/10
 * Describe :
 *
 * @author chendong
 */
@AutoService(Processor.class)
public class MViewProcessor extends AbstractProcessor {

    private Elements mElementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mElementUtils = processingEnv.getElementUtils();
    }

    // 针对处理 MView 注解
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(MView.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(MView.class);
        Map<TypeElement, List<MViewPair>> targetViewMap = new HashMap<>();
        for (Element element : elementsAnnotatedWith) {
            // 拿到每一个带有注解的 field
            VariableElement variableElement = (VariableElement) element;
            // 返回封装他的 element , Activity
            TypeElement enclosingElement = (TypeElement) variableElement.getEnclosingElement();
            List<MViewPair> viewPairs = targetViewMap.computeIfAbsent(enclosingElement, k -> new ArrayList<>());
            MView annotation = variableElement.getAnnotation(MView.class);
            viewPairs.add(new MViewPair(annotation.value(), variableElement));
        }

        for (TypeElement typeElement : targetViewMap.keySet()) {
            List<MViewPair> mViewPairs = targetViewMap.get(typeElement);
            String packageName = mElementUtils.getPackageOf(typeElement).getQualifiedName().toString();
            JavaFile javaFile = JavaFile.builder(packageName, generateClass(typeElement, mViewPairs)).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    private static class MViewPair {
        private int             viewId;
        private VariableElement element;

        public MViewPair(int viewId, VariableElement element) {
            this.viewId = viewId;
            this.element = element;
        }
    }

    private TypeSpec generateClass(TypeElement typeElement, List<MViewPair> viewPairs) {
        return TypeSpec.classBuilder(typeElement.getSimpleName().toString() + "_MView")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(generateMethod(typeElement, viewPairs))
                .build();
    }

    private MethodSpec generateMethod(TypeElement typeElement, List<MViewPair> viewPairs) {
        ClassName className = ClassName.bestGuess(typeElement.getQualifiedName().toString());

        String paramName = "_" + className.simpleName();
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("bindView")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(className, paramName);

        for (MViewPair viewPair : viewPairs) {
            // 注解的字段名
            Name viewName = viewPair.element.getSimpleName();
            String viewType = viewPair.element.asType().toString();
            String template = "{0}.{1} = ({2})({3}.findViewById({4}));";
            methodBuilder.addCode(MessageFormat.format(template,  paramName, viewName, viewType, paramName, String.valueOf(viewPair.viewId)));
            methodBuilder.addCode("\n");
        }

        return methodBuilder.build();
    }
}
