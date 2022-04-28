package ru.fmtk.khlystov.otus_java.atm;

import java.math.BigDecimal;
import java.util.Objects;

public class BankNote implements Comparable<BankNote> {
    private final BigDecimal value;

    public BankNote(int value) {
        this(BigDecimal.valueOf(value));
    }

    public BankNote(BigDecimal value) {
        this.value = value;
    }

    public static BankNote fractional(int value, int divider) {
        return new BankNote(BigDecimal.valueOf(value).divide(BigDecimal.valueOf(divider)));
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "$" + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankNote bankNote = (BankNote) o;
        return Objects.equals(value, bankNote.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(BankNote o) {
        if (o == null) {
            return 1;
        }
        return value.compareTo(o.value);
    }
}
