package ru.fmtk.khlystov.otus_java.atm;

import java.math.BigDecimal;
import java.util.Map;

import ru.fmtk.khlystov.otus_java.currencies.BN;

public interface Atm<T extends Enum<T> & BN<T>> {

    /**
     * принимать банкноты разных номиналов (на каждый номинал должна быть своя ячейка)
     */
    void add(T bankNote, int count);

    /**
     * Выдавать запрошенную сумму минимальным количеством банкнот или ошибку, если сумму нельзя выдать.
     * Это задание не на алгоритмы, а на проектирование. Поэтому оптимизировать выдачу не надо.
     **/
    Map<T, Integer> getSum(final BigDecimal sum);

    /**
     * выдавать сумму остатка денежных средств
     */
    BigDecimal calcTotalSum();

    Map<T, Integer> audit();
}
