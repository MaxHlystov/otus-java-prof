package ru.fmtk.khlystov.otus_java.api;

import ru.fmtk.khlystov.otus_java.api.model.SensorData;

public interface SensorsDataServer {
    void onReceive(SensorData sensorData);
}
