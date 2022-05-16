package ru.fmtk.khlystov.otus_java.atm;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import ru.fmtk.khlystov.otus_java.currencies.Banknote;

public class AtmImpl<T extends Enum<T> & Banknote<T>> implements Atm<T> {

    private final Class<T> banknoteClass;
    private final Function<T, Cell<T>> cellCreator;

    private final Map<T, Cell<T>> cells;

    public AtmImpl(Class<T> banknoteClass, Function<T, Cell<T>> cellCreator) {
        this.banknoteClass = banknoteClass;
        this.cellCreator = cellCreator;
        this.cells = new EnumMap<>(banknoteClass);
    }

    @Override
    public void add(T bankNote, int count) {
        if (count > 0) {
            Cell<T> cell = cells.computeIfAbsent(bankNote, cellCreator);
            cell.add(count);
        }
    }

    @Override
    public Map<T, Integer> audit() {
        return cells.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getIntCount()));
    }

    @Override
    public EnumMap<T, Integer> getSum(final BigDecimal sum) {
        BigDecimal currentSum = sum;
        final EnumMap<T, Integer> result = new EnumMap<>(this.banknoteClass);
        while (currentSum.compareTo(BigDecimal.ZERO) > 0) {
            Cell<T> floorCell = getFloorEntry(currentSum);
            if (floorCell == null) {
                throw new IllegalArgumentException("We cannot cashing sum " + sum);
            }
            BigDecimal take = floorCell.takeBanknotesToSum(currentSum);
            if (BigDecimal.ZERO.compareTo(take) >= 0) {
                throw new IllegalArgumentException("We cannot cashing sum " + sum);
            }
            result.put(floorCell.getBanknote(), take.intValue());
            currentSum = currentSum.subtract(floorCell.calcSum(take));
        }
        return result;
    }

    @Override
    public BigDecimal calcTotalSum() {
        return cells.values().stream()
                .map(Cell::getSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "ATM{ " + cells.values() + ", sum=" + calcTotalSum() + " }";
    }

    private Cell<T> getFloorEntry(BigDecimal sum) {
        return cells.values().stream()
                .filter(cell -> cell.hasSumLessThan(sum))
                .max(Comparator.naturalOrder())
                .orElse(null);
    }
}
