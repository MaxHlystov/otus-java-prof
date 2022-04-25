package ru.fmtk.khlystov.otus_java.demo;

import ru.fmtk.khlystov.otus_java.aop.Logger;

public class LoggerImpl implements Logger {
    @Override
    public void add(String s) {
        System.out.print("\t\t" + s);
    }
}
