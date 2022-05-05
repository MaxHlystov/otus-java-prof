package ru.fmtk.khlystov.otus_java.dataprocessor;

import ru.fmtk.khlystov.otus_java.model.Measurement;

import java.util.List;

public interface Loader {

    List<Measurement> load();
}
