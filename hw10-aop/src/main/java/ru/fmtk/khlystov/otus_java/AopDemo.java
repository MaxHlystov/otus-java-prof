package ru.fmtk.khlystov.otus_java;

import java.util.List;

import ru.fmtk.khlystov.otus_java.aop.BeanCreator;
import ru.fmtk.khlystov.otus_java.demo.DemoClass;
import ru.fmtk.khlystov.otus_java.demo.DemoSubClass;

public class AopDemo {
    public static void main(String[] args) {
        final BeanCreator beanCreator = new BeanCreator("ru.fmtk.khlystov.otus_java.demo");
        testDemoClass(beanCreator.getBean("DemoClass"));
        System.out.println();
        testDemoSubClass(beanCreator.getBean("DemoSubClass"));
    }


    private static void testDemoClass(DemoClass demo) {
        System.out.println("Start test demo class");
        demo.test(123);
        demo.test(3.1415);
        demo.test(4, "xyz");
        demo.test1(4, "xyz");
        System.out.println("demo.toString() == " + demo);
        System.out.println("End test demo class");
    }

    private static void testDemoSubClass(DemoSubClass subDemo) {
        System.out.println("Start test demo subclass");
        DemoClass demo = subDemo;
        demo.test(123);
        subDemo.test(123);
        demo.test(3.1415);
        subDemo.test(3.1415);
        demo.test(4, "xyz");
        demo.test1(4, "xyz");
        subDemo.test2(List.of("1", "2", "3"));
        subDemo.test2();
        System.out.println("subDemo.toString() == " + subDemo);
        System.out.println("End test demo subclass");
    }
}
