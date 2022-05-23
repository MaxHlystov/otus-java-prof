package ru.fmtk.khlystov.otus_java.core.sessionmanager;

import java.util.function.Function;

import org.hibernate.Session;

public interface TransactionAction<T> extends Function<Session, T> {
}
