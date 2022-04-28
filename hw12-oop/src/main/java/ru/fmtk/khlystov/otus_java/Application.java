package ru.fmtk.khlystov.otus_java;

import java.math.BigDecimal;

import ru.fmtk.khlystov.otus_java.atm.ATM;
import ru.fmtk.khlystov.otus_java.atm.BankNote;

public class Application {
    public static void main(String[] args) {
        System.out.println("ATM demo");
        ATM atm = new ATM();
        atm.add(new BankNote(500), 0);
        atm.add(new BankNote(100), 1);
        atm.add(new BankNote(10), 3);
        atm.add(new BankNote(5), 4);
        atm.add(new BankNote(1), 10);
        atm.add(BankNote.fractional(5, 10), 10);
        atm.add(BankNote.fractional(1, 10), 20);
        System.out.println(atm);
        BigDecimal val1 = BigDecimal.valueOf(47.8);
        var cash = atm.getSum(val1);
        System.out.println("Cash for " + val1 + " is " + cash);
        System.out.println("New state is " + atm);
    }
}
