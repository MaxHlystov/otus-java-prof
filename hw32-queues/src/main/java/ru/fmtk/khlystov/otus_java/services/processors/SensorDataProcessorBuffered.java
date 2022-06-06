package ru.fmtk.khlystov.otus_java.services.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.fmtk.khlystov.otus_java.api.SensorDataProcessor;
import ru.fmtk.khlystov.otus_java.api.model.SensorData;
import ru.fmtk.khlystov.otus_java.lib.SensorDataBufferedWriter;

// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final List<SensorData> dataBuffer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.dataBuffer = new ArrayList<>(bufferSize);
    }

    @Override
    public void process(SensorData data) {
        synchronized (dataBuffer) {
            if (dataBuffer.size() >= bufferSize) {
                flush();
            }
            this.dataBuffer.add(data);
        }
    }

    public void flush() {
        synchronized (dataBuffer) {
            if (dataBuffer.size() > 0) {
                try {
                    SensorData[] array = dataBuffer.toArray(SensorData[]::new);
                    dataBuffer.clear();
                    Arrays.sort(array, Comparator.comparing(SensorData::getMeasurementTime));
                    writer.writeBufferedData(Arrays.asList(array));
                } catch (Exception e) {
                    log.error("Ошибка в процессе записи буфера", e);
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
