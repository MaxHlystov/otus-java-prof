package ru.fmtk.khlystov.otus_java.atm;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

import ru.fmtk.khlystov.otus_java.currencies.BN;

public class AtmImpl<T extends Enum<T> & BN<T>> implements Atm<T> {

    private final Class<T> banknoteClass;

    private final Map<T, BigDecimal> cells;

    public AtmImpl(Class<T> banknoteClass) {
        this.banknoteClass = banknoteClass;
        this.cells = new EnumMap<>(banknoteClass);
    }

    @Override
    public void add(T bankNote, int count) {
        if (count > 0) {
            BigDecimal result = cells.get(bankNote);
            if (result == null) {
                result = BigDecimal.ZERO;
            }
            result = result.add(BigDecimal.valueOf(count));
            cells.put(bankNote, result);
        }
    }

    @Override
    public Map<T, Integer> audit() {
        return cells.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().intValue()));
    }

    @Override
    public EnumMap<T, Integer> getSum(final BigDecimal sum) {
        BigDecimal currentSum = sum;
        final EnumMap<T, Integer> result = new EnumMap<>(this.banknoteClass);
        while (currentSum.compareTo(BigDecimal.ZERO) > 0) {
            T floorBn = getFloorEntry(currentSum);
            if (floorBn == null) {
                throw new IllegalArgumentException("We cannot cashing sum " + sum);
            }
            BigDecimal take = takeBanknotesToSum(floorBn, currentSum);
            if (BigDecimal.ZERO.compareTo(take) >= 0) {
                throw new IllegalArgumentException("We cannot cashing sum " + sum);
            }
            result.put(floorBn, take.intValue());
            currentSum = currentSum.subtract(floorBn.getValue().multiply(take));
        }
        return result;
    }

    @Override
    public BigDecimal calcTotalSum() {
        return cells.entrySet().stream()
                .map(entry -> entry.getKey().getValue().multiply(entry.getValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "ATM{ " + cells.values() + ", sum=" + calcTotalSum() + " }";
    }

    private T getFloorEntry(BigDecimal sum) {
        return cells.keySet().stream()
                .filter(key -> key.getValue().compareTo(BigDecimal.ZERO) > 0 && key.getValue().compareTo(sum) <= 0)
                .max(BN::compareBN)
                .orElse(null);
    }

    private BigDecimal getCount(T banknote) {
        if (banknote == null) {
            throw new IllegalArgumentException("Banknote must not be null");
        }
        BigDecimal count = cells.get(banknote);
        if (count == null) {
            return BigDecimal.ZERO;
        }
        return count;
    }

    private BigDecimal takePossibleCount(T banknote, BigDecimal takeCount) {
        if (banknote == null) {
            throw new IllegalArgumentException("Banknote must not be null");
        }
        if (takeCount == null) {
            throw new IllegalArgumentException("Take count must not be null");
        }
        BigDecimal bdCount = getCount(banknote);
        takeCount = takeCount.min(bdCount);
        cells.put(banknote, bdCount.subtract(takeCount));
        return takeCount;
    }

    private BigDecimal takeBanknotesToSum(T banknote, BigDecimal sum) {
        BigDecimal bdCount = getCount(banknote);
        if (bdCount.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        BigDecimal needCount = sum.divideToIntegralValue(banknote.getValue());
        if (needCount.compareTo(BigDecimal.ZERO) > 0) {
            return takePossibleCount(banknote, needCount);
        }
        return BigDecimal.ZERO;
    }

}
