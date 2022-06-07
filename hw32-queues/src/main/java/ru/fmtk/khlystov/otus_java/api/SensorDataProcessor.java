package ru.fmtk.khlystov.otus_java.api;


import ru.fmtk.khlystov.otus_java.api.model.SensorData;

public interface SensorDataProcessor {
    void process(SensorData data);

    default void onProcessingEnd() {
    }
}
