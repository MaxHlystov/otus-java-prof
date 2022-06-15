package ru.fmtk.khlystov.otus_java.sessionmanager;

import java.util.function.Supplier;

public interface TransactionAction<T> extends Supplier<T> {
}
