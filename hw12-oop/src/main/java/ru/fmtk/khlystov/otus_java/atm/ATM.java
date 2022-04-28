package ru.fmtk.khlystov.otus_java.atm;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ATM {

    private final TreeMap<BankNote, AtmCell> cells = new TreeMap<>(Comparator.reverseOrder());

    // принимать банкноты разных номиналов (на каждый номинал должна быть своя ячейка)
    public void add(BankNote bankNote, int count) {
        if (count > 0) {
            AtmCell cell = getCell(bankNote);
            cell.addBanknotes(count);
        }
    }

    public Map<BankNote, Integer> audit() {
        return cells.values().stream()
                .collect(Collectors.toMap(
                        AtmCell::getBankNote,
                        AtmCell::getCount));
    }

    // Выдавать запрошенную сумму минимальным количеством банкнот или ошибку, если сумму нельзя выдать.
    // Это задание не на алгоритмы, а на проектирование. Поэтому оптимизировать выдачу не надо.
    public Map<BankNote, Integer> getSum(final BigDecimal sum) {
        BigDecimal currentSum = sum;
        final Map<BankNote, Integer> result = new HashMap<>();
        while (currentSum.compareTo(BigDecimal.ZERO) > 0) {
            AtmCell ceilingCell = getCeilingCell(currentSum);
            if (ceilingCell == null) {
                throw new IllegalArgumentException("We cannot cashing sum " + sum);
            }
            BankNote currentBankNote = ceilingCell.getBankNote();
            if (ceilingCell.isEmpty()) {
                cells.remove(currentBankNote);
                continue;
            }
            int take = ceilingCell.takeBanknotesToSum(currentSum);
            if (take == 0) {
                throw new IllegalArgumentException("We cannot cashing sum " + sum);
            }
            if (ceilingCell.isEmpty()) {
                cells.remove(currentBankNote);
            }
            result.put(currentBankNote, take);
            currentSum = currentSum.subtract(currentBankNote.getValue().multiply(BigDecimal.valueOf(take)));
        }
        return result;
    }

    //выдавать сумму остатка денежных средств
    public BigDecimal calcTotalSum() {
        return cells.values().stream()
                .map(AtmCell::getTotalSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "ATM{ " + cells.values() + ", sum=" + calcTotalSum() + " }";
    }

    private AtmCell getCeilingCell(BigDecimal sum) {
        BankNote fake = new BankNote(sum);
        var floor = cells.ceilingEntry(fake);
        if (floor == null) {
            return null;
        }
        return floor.getValue();
    }

    private AtmCell getCell(BankNote bankNote) {
        AtmCell result = cells.get(bankNote);
        if (result == null) {
            result = new AtmCell(bankNote);
            cells.put(bankNote, result);
        }
        return result;
    }
}
