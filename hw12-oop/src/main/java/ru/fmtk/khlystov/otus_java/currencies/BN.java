package ru.fmtk.khlystov.otus_java.currencies;

import java.math.BigDecimal;

public interface BN<T extends BN<T>> {
    BigDecimal getValue();

    default int compareBN(T o) {
        if (o == null) {
            return 1;
        }
        return this.getValue().compareTo(o.getValue());
    }
}
