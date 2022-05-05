package ru.fmtk.khlystov.otus_java.currencies;

import java.math.BigDecimal;

public enum EU implements BN<EU> {
    v100(100), v10(10);

    private final BigDecimal value;

    EU(long value) {
        this.value = BigDecimal.valueOf(value);
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }
}
