package ru.fmtk.khlystov.otus_java.demo;

import ru.fmtk.khlystov.otus_java.aop.Log;

public class DemoClass {

    @Log
    public void test(int x) {
        System.out.println("@Log DemoClass::test(int " + x + ")");
    }

    public void test(double x) {
        System.out.println("DemoClass::test(double " + x + ")");
    }

    @Log
    public void test(int x, String y) {
        System.out.println("@Log DemoClass::test(int " + x + ", String '" + y + "')");
    }

    public void test1(int x, String y) {
        System.out.println("DemoClass::test1(" + x + ", String '" + y + "')");
    }
}
