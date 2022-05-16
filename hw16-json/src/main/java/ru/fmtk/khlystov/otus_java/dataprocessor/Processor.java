package ru.fmtk.khlystov.otus_java.dataprocessor;

import ru.fmtk.khlystov.otus_java.model.Measurement;

import java.util.List;
import java.util.Map;

public interface Processor {

    Map<String, Double> process(List<Measurement> data);
}
