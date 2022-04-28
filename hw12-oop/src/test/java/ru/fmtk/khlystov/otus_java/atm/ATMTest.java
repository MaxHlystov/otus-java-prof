package ru.fmtk.khlystov.otus_java.atm;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ATMTest {

    @Test
    void add_one() {
        ATM atm = new ATM();
        atm.add(new BankNote(10), 2);
        assertEquals(Map.of(new BankNote(10), 2), atm.audit());
    }

    @Test
    void add_severalIncreased() {
        ATM atm = new ATM();
        atm.add(BankNote.fractional(1, 10), 5);
        atm.add(new BankNote(1), 4);
        atm.add(new BankNote(10), 3);
        atm.add(new BankNote(20), 2);
        atm.add(new BankNote(60), 1);
        assertEquals(Map.of(
                        BankNote.fractional(1, 10), 5,
                        new BankNote(60), 1,
                        new BankNote(20), 2,
                        new BankNote(10), 3,
                        new BankNote(1), 4),
                atm.audit());
    }

    @Test
    void getSum_isOk() {
        ATM atm = new ATM();
        atm.add(BankNote.fractional(1, 10), 5);
        atm.add(new BankNote(1), 4);
        atm.add(new BankNote(10), 3);
        atm.add(new BankNote(20), 2);
        atm.add(new BankNote(60), 1);
        Map<BankNote, Integer> cash = atm.getSum(BigDecimal.valueOf(53.1));
    }

    @Test
    void getSum_exactSum_isOk() {
        ATM atm = new ATM();
        atm.add(BankNote.fractional(1, 10), 5);
        atm.add(new BankNote(1), 4);
        atm.add(new BankNote(10), 3);
        atm.add(new BankNote(20), 2);
        atm.add(new BankNote(60), 1);
        Map<BankNote, Integer> cash = atm.getSum(BigDecimal.valueOf(134.5));
    }

    @Test
    void getSum_tooBigSum_isBad() {
        ATM atm = new ATM();
        atm.add(BankNote.fractional(1, 10), 5);
        atm.add(new BankNote(1), 4);
        atm.add(new BankNote(10), 3);
        atm.add(new BankNote(20), 2);
        atm.add(new BankNote(60), 1);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> atm.getSum(BigDecimal.valueOf(3000)));
        assertEquals("We cannot cashing sum 3000", exception.getMessage());
    }

    @Test
    void getSum_notFactorisedSum_isBad() {
        ATM atm = new ATM();
        atm.add(BankNote.fractional(1, 10), 5);
        atm.add(new BankNote(1), 4);
        atm.add(new BankNote(10), 3);
        atm.add(new BankNote(20), 2);
        atm.add(new BankNote(60), 1);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> atm.getSum(BigDecimal.valueOf(15)));
        assertEquals("We cannot cashing sum 15", exception.getMessage());
    }

    @Test
    void calcTotalSum_forEmptyAtm_isZero() {
        ATM atm = new ATM();
        assertEquals(BigDecimal.ZERO, atm.calcTotalSum());
    }

    @Test
    void calcTotalSum_for10By3Atm_is30() {
        ATM atm = new ATM();
        atm.add(new BankNote(10), 3);
        assertEquals(BigDecimal.valueOf(30), atm.calcTotalSum());
    }

    @Test
    void calcTotalSum_for10By3And1By2Atm_is32() {
        ATM atm = new ATM();
        atm.add(new BankNote(10), 3);
        atm.add(new BankNote(1), 2);
        assertEquals(BigDecimal.valueOf(32), atm.calcTotalSum());
    }

    @Test
    void calcTotalSum_forFractional_isExactValue() {
        ATM atm = new ATM();
        atm.add(new BankNote(10), 3);
        atm.add(new BankNote(1), 2);
        atm.add(BankNote.fractional(1, 10), 1);
        atm.add(BankNote.fractional(1, 1000), 1);
        atm.add(BankNote.fractional(1, 100000), 1);
        assertEquals(BigDecimal.valueOf(32.10101), atm.calcTotalSum());
    }
}