package ru.fmtk.khlystov.otus_java.atm;

import java.math.BigDecimal;

import ru.fmtk.khlystov.otus_java.currencies.Banknote;

public interface Cell<T extends Banknote<T>> extends Comparable<Cell<T>> {
    void add(int count);

    T getBanknote();

    int getIntCount();

    BigDecimal getSum();

    boolean hasSumLessThan(BigDecimal sum);

    BigDecimal takeBanknotesToSum(BigDecimal currentSum);

    BigDecimal calcSum(BigDecimal count);
}
