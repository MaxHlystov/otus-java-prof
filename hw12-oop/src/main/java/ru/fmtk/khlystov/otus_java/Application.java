package ru.fmtk.khlystov.otus_java;

import java.math.BigDecimal;

import ru.fmtk.khlystov.otus_java.atm.Atm;
import ru.fmtk.khlystov.otus_java.atm.AtmImpl;
import ru.fmtk.khlystov.otus_java.currencies.EU;

public class Application {
    public static void main(String[] args) {
        System.out.println("ATM demo");
        Atm<EU> atmImpl = new AtmImpl<>(EU.class);
        atmImpl.add(EU.v100, 1);
        atmImpl.add(EU.v10, 3);
        System.out.println(atmImpl);
        BigDecimal val1 = BigDecimal.valueOf(120);
        var cash = atmImpl.getSum(val1);
        System.out.println("Cash for " + val1 + " is " + cash);
        System.out.println("New state is " + atmImpl);
    }
}
