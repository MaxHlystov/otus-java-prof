package ru.fmtk.khlystov.otus_java.sessionmanager;

public interface TransactionManager {
    <T> T doInTransaction(TransactionAction<T> action);
}
