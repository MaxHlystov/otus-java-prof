package ru.fmtk.khlystov.otus_java.currencies;

import java.math.BigDecimal;

public enum RU implements Banknote<RU> {
    v1000(1000), v100(100), v50(50), v10(10), v1(1), v0001(1, 4);

    private final BigDecimal value;

    RU(long value) {
        this.value = BigDecimal.valueOf(value);
    }

    RU(int value, int scale) {
        this.value = BigDecimal.valueOf(value, scale);
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }
}
