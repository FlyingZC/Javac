package com.sun.tools.javac.zc.z05;

/**
 * @author zhangcheng
 * @date 2020/11/3
 */
public class T01Converter {
    // <editor-fold desc="main方法" defaultstate="collapsed">
    public static void main(String[] args) {
        // 1. 同一性转换（Identity Conversion）
        int a1 = 1;
        int b1 = a1;

        // 2. 基本类型宽化转换（Widening Primitive Conversion）
        // byte、short、char < int < long <float < double
        byte bb = 1;
        char cc = ' ';
        int ii = bb + cc;

        // 3. 基本类型窄化转换（Narrowing Primitive Conversion）
        float a3 = 10;
        int b3 = (int) a3;

        // 4. 基本类型宽化和窄化转换（Widening and Narrowing Primitive Conversion）
        byte a4 = 10;
        // byte 类型不能直接转换为 char 类型，因此先需要宽化处理转换为 int 类型，然后再窄化转换为 char 类型
        char b4 = (char) a4;

        // 5. 引用类型宽化转换（Widening Reference Conversion）
        // 引用类型宽化处理就是子类转换为父类或接口的情况
        Sub sub = new Sub();
        Parent p = sub;

        Sub sub2 = new Sub();
        IA pp5 = sub2;

        // 6. 引用类型窄化转换（Narrowing Reference Conversion）
        // 有父子关系的引用类型可以通过强制类型转换的方式进行窄化转换，由于 Object 是所有类的超类，因而通过强制类型转换可以转换为任何一种引用类型。这种转换同样也适用于数组
        Object obj6 = new Object();
        if (obj6 instanceof Parent) {
            Parent p61 = (Parent) obj6;
        }

        Parent[] p6 = new Parent[10];
        // java.lang.ClassCastException: [Lcom.sun.tools.javac.zc.z05.T01Converter$Parent; cannot be cast to [Lcom.sun.tools.javac.zc.z05.T01Converter$Sub
        // 上面 p6 实际类型是 Parent[],强转报错
        // Sub[] s6 = (Sub[]) p6;

        Parent[] p61 = new Sub[10];
        Sub[] s61 = (Sub[]) p61;

        // 对于数组来说，Cloneable 或 Serializable 接口都可以通过强制类型转换的方式转为数组类型，因为数组类型实现了这两个接口?????

        // 7. 类型装箱转换（Boxing Conversion）
        // 从基本类型转换 -> 引用类型就称为类型装箱转换
        int i7 = 1;
        Integer ii7 = i7;

        // 8. 类型拆箱转换（Unboxing Conversion）
        // 引用类型 -> 基本数据类型
        Integer ii8 = 1;
        int i8 = ii8;

        // 9. 非检查转换（Unchecked Conversion）
        // 从裸类型（包括类、接口和数组）转换为 参数化类型 时就称为 非检查转换
        TestClass p9 = new TestClass();
        // Unchecked assignment: 'com.sun.tools.javac.zc.z05.T01Converter.TestClass' to 'com.sun.tools.javac.zc.z05.T01Converter.TestClass<java.lang.String>'
        // 这样的转换并不能保证在运行期就一定成功
        TestClass<String> b9 = p9; // 警告，未经检查的转换

        // 10. 字符串转换（String Conversion）
        // 任何类型都可能通过字符串转换转为字符串类型
        Object obj10 = new Object();
        // 对于引用类型来说，直接调用 toString() 方法即可完成转换，不过当引用类型为 null 时，将被转换为字符串 null
        String str10 = obj10.toString();
        // 对于基本类型来说，可以先封装为对应的引用类型后调用 toString() 方法
        int i10 = 1;
        String str101 = new Integer(i10).toString();

        // 11. 捕获转换（Capture Conversion）
        // 声明 Plate<? extends Apple> a, 编译器只知道 Plate 容器内是 Apple 或者它的派生类,但并不能确定具体的类型
        // 当前a被赋值为 a = Plate<Apple>，因此Plate容器内具体的类型为Apple，但是编译器并没有将这个类型记录下来

        Plate<? extends Apple> a111 = new Plate<Apple>();
        // 当a被赋值给类型为 Plate<? extends Fruit> 的变量b时，
        // 为了检查类型的兼容性，Plate<? extends Apple> 类型会发生捕获转换，
        // 具体说就是会捕获 通配符类型 所代表的具体类型（虽然具体的类型是Apple，但是编译器并没有记录这个具体的类型，发生捕获转换时只能尽量结合已有的信息来捕获具体的类型），
        // 这样a的类型就与Plate<Apple>一样，是一个具体的类型了。
        Plate<? extends Fruit> b111 = a111;

        Plate<?> a112 = new Plate<Apple>();
        Plate<?> b112 = a112;

        Plate<? super Apple> a113 = new Plate<Apple>();
        Plate<? super Apple> b113 = a113;

    }
    // </editor-fold>
    interface IA{ }

    private static class Fruit {
    }

    private static class Apple extends Fruit {
    }

    private static class Plate<T> {
        private T item;

        public void set(T t) {
            item = t;
        }

        public T get() {
            return item;
        }
    }

    private static class TestClass<T> {

    }

    private static class TestClone implements Cloneable {

    }

    private static class Parent {
    }

    private static class Sub extends Parent implements IA {
    }


}
