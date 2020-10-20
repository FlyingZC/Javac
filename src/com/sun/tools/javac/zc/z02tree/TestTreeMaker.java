package com.sun.tools.javac.zc.z02tree;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.*;
/**
 * 手动创建抽象语法树
 *
 * @author zhangcheng
 * @date 2020/10/18
 */
public class TestTreeMaker {
    static Names names;
    static TreeMaker F;

    public static void main(String[] args) {
        Context context = new Context();
        JavacFileManager.preRegister(context);
        F = TreeMaker.instance(context);
        names = Names.instance(context);

        // public int a = 1;
        JCModifiers mods = F.Modifiers(Flags.PUBLIC);
        JCPrimitiveTypeTree type = F.TypeIdent(TypeTag.INT);
        Name name = names.fromString("a");
        JCLiteral init = F.Literal(TypeTag.INT, "1");
        // 创建变量
        JCVariableDecl result = F.VarDef(mods, name, type, init);

        ListBuffer<JCTree> defs = new ListBuffer<JCTree>();
        defs.append(result);

        // public class Test
        JCModifiers mods1 = F.Modifiers(Flags.PUBLIC);
        Name name1 = names.fromString("Test");

        // 类型参数
        List<JCTypeParameter> typarams = List.nil();
        // 表达式
        List<JCExpression> implementing = List.nil();
        // 创建类型
        JCClassDecl jcc = F.ClassDef(mods1, name1, typarams, null, implementing, defs.toList());

        ListBuffer<JCTree> defsx = new ListBuffer<JCTree>();
        defsx.add(jcc);

        // package zc;
        List<JCAnnotation> packageAnnotations = List.nil();
        JCIdent ifr = F.Ident(names.fromString("zc"));
        JCExpression pid = ifr;
        // 根据 包名 和 类型定义 创建根节点
        JCCompilationUnit toplevel = F.TopLevel(packageAnnotations, pid, defsx.toList());

        System.out.println(toplevel.toString());
    }
}