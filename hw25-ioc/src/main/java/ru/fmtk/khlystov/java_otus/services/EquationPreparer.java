package ru.fmtk.khlystov.java_otus.services;

import ru.fmtk.khlystov.java_otus.model.Equation;

import java.util.List;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
