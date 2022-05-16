package ru.fmtk.khlystov.otus_java.atm;

import java.math.BigDecimal;
import java.util.Objects;

import ru.fmtk.khlystov.otus_java.currencies.Banknote;

public class CellImpl<T extends Banknote<T>> implements Cell<T> {
    private final T banknote;
    private BigDecimal count;

    public CellImpl(T banknote) {
        if (banknote == null) {
            throw new IllegalArgumentException("Banknote must not be null");
        }
        this.banknote = banknote;
        this.count = BigDecimal.ZERO;
    }

    @Override
    public void add(int count) {
        if (count > 0) {
            this.count = this.count.add(BigDecimal.valueOf(count));
        }
    }

    @Override
    public T getBanknote() {
        return banknote;
    }

    @Override
    public int getIntCount() {
        return count.intValue();
    }

    @Override
    public BigDecimal getSum() {
        return banknote.getValue().multiply(count);
    }

    @Override
    public boolean hasSumLessThan(BigDecimal sum) {
        return count.compareTo(BigDecimal.ZERO) > 0 && banknote.getValue().compareTo(sum) <= 0;
    }

    @Override
    public BigDecimal calcSum(BigDecimal count) {
        return banknote.getValue().multiply(count);
    }

    @Override
    public BigDecimal takeBanknotesToSum(BigDecimal sum) {
        if (count.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        BigDecimal needCount = sum.divideToIntegralValue(banknote.getValue());
        if (needCount.compareTo(BigDecimal.ZERO) > 0) {
            return takePossibleCount(needCount);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public int compareTo(Cell<T> cell) {
        return banknote.compare(cell.getBanknote());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CellImpl<?> cell = (CellImpl<?>) o;
        return banknote.equals(cell.banknote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(banknote);
    }

    @Override
    public String toString() {
        return "" + banknote + "=" + count;
    }

    private BigDecimal takePossibleCount(BigDecimal takeCount) {
        if (takeCount == null) {
            return BigDecimal.ZERO;
        }
        takeCount = takeCount.min(count);
        count = count.subtract(takeCount);
        return takeCount;
    }
}
