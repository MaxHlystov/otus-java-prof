package ru.fmtk.khlystov.otus_java.currencies;

import java.math.BigDecimal;

public interface Banknote<T extends Banknote<T>> {
    BigDecimal getValue();

    default int compare(T o) {
        if (o == null) {
            return 1;
        }
        return this.getValue().compareTo(o.getValue());
    }
}
