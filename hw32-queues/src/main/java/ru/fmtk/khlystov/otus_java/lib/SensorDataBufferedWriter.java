package ru.fmtk.khlystov.otus_java.lib;


import ru.fmtk.khlystov.otus_java.api.model.SensorData;

import java.util.List;

public interface SensorDataBufferedWriter {
    void writeBufferedData(List<SensorData> bufferedData);
}
