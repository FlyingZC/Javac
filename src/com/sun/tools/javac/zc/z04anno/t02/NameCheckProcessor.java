package com.sun.tools.javac.zc.z04anno.t02;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;
// 编写插入式注解处理器
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class NameCheckProcessor extends AbstractProcessor {
    private NameCheckScanner nameCheckScanner;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        nameCheckScanner = new NameCheckScanner(processingEnv.getMessager());
    }

    // 对输入的语法树的各个节点进行名称检查
    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            for (Element element : roundEnv.getRootElements())
                nameCheckScanner.scan(element);
        }
        return false;
    }
}