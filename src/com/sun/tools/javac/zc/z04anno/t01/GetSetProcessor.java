package com.sun.tools.javac.zc.z04anno.t01;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes({"com.sun.tools.javac.zc.z04anno.Getter"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class GetSetProcessor extends AbstractProcessor {
    private Messager messager;
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;

    // 覆写了init()方法初始化了一些工具类，利用这些工具类可以修改抽象语法树
    @Override
    public synchronized void init(ProcessingEnvironment pe) {
        super.init(pe);
        messager = pe.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) pe).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    // 为所有标注@Getter注解类的私有成员变量生成getXxx()方法
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment re) {
        Set<? extends Element> elems = re.getElementsAnnotatedWith(MyGetter.class);

        TreeTranslator tt = new TreeTranslator() {
            @Override
            public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                List<JCTree.JCVariableDecl> jcVariableDeclList = List.nil();
                // 将类中定义的成员变量保存到jcVariableDeclList列表中
                for (JCTree tree : jcClassDecl.defs) {
                    if (tree.getKind().equals(Tree.Kind.VARIABLE)) {
                        JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) tree;
                        jcVariableDeclList = jcVariableDeclList.append(jcVariableDecl);
                    }
                }
                // 调用makeGetterMethodDecl()方法为成员变量创建getXxx()方法
                // 对应的语法树并追加到jcClassDecl.defs中
                for (JCTree.JCVariableDecl jcVariableDecl : jcVariableDeclList) {
                    messager.printMessage(Diagnostic.Kind.NOTE,jcVariableDecl.getName() + " has been processed");
                    jcClassDecl.defs = jcClassDecl.defs.prepend(makeGetterMethodDecl(jcVariableDecl));
                }
                super.visitClassDef(jcClassDecl);
            }
        };
        for (Element element : elems) {
            JCTree jcTree = trees.getTree(element);
            jcTree.accept(tt);
        }
        return true;
    }

    // 为成员变量创建getXxx()方法对应的语法树
    private JCTree.JCMethodDecl makeGetterMethodDecl(JCTree.JCVariableDecl jcVariableDecl) {
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        JCTree.JCIdent jci = treeMaker.Ident(names.fromString("this"));
        JCTree.JCFieldAccess jcf = treeMaker.Select(jci, jcVariableDecl.getName());
        JCTree.JCReturn jcr = treeMaker.Return(jcf);
        statements.append(jcr);
        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC),
                getNewMethodName(jcVariableDecl.getName()),
                jcVariableDecl.vartype,
                List.<JCTree.JCTypeParameter>nil(),
                List.<JCTree.JCVariableDecl>nil(),
                List.<JCTree.JCExpression>nil(),
                body,
                null
        );
    }

    private Name getNewMethodName(Name name) {
        String s = name.toString();
        String mn = "get" + s.substring(0, 1).toUpperCase() + s.substring(1, name.length());
        return names.fromString(mn);
    }
}