package ru.fmtk.khlystov.otus_java.atm;

import java.math.BigDecimal;
import java.util.Objects;

import static java.lang.Math.min;

public class AtmCell {
    private final BankNote bankNote;
    private int count;

    public AtmCell(BankNote bankNote) {
        this(bankNote, 0);
    }

    public AtmCell(BankNote bankNote, int count) {
        if (bankNote == null) {
            throw new IllegalArgumentException("Banknote must not be null");
        }
        this.bankNote = bankNote;
        this.count = count;
    }

    public BankNote getBankNote() {
        return bankNote;
    }

    public int getCount() {
        return count;
    }

    public BigDecimal getTotalSum() {
        return bankNote.getValue().multiply(BigDecimal.valueOf(count));
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public void addBanknotes(int count) {
        this.count += count;
    }

    public int takeBanknotesToSum(BigDecimal sum) {
        int needCount = sum.divideToIntegralValue(bankNote.getValue()).intValue();
        int canTakeCount = min(needCount, this.count);
        this.count -= canTakeCount;
        return canTakeCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AtmCell atmCell = (AtmCell) o;
        return Objects.equals(bankNote, atmCell.bankNote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bankNote);
    }

    @Override
    public String toString() {
        return bankNote.toString() + "=" + count;
    }
}
