package ru.fmtk.khlystov.otus_java.demo;

import java.util.List;

import ru.fmtk.khlystov.otus_java.aop.Log;

public class DemoSubClass extends DemoClass {

    @Override
    public void test(double x) {
        System.out.println("DemoSubClass::test(double " + x + ")");
    }

    @Log
    public void test2(List<String> list) {
        int size = list == null ? 0 : list.size();
        System.out.println("@Log DemoSubClass::test2(List<String> " + size + ")");
    }

    public void test2() {
        System.out.println("DemoSubClass::test2()");
    }

    @Log
    @Override
    public String toString() {
        return "Will return null because of @Log method has to return void";
    }
}
