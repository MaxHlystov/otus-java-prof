package ru.fmtk.khlystov.otus_java.api.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SensorData {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    private final LocalDateTime measurementTime;
    private final String room;
    private final Double value;

    public SensorData(LocalDateTime measurementTime, String room, Double value) {
        this.measurementTime = measurementTime;
        this.room = room;
        this.value = value;
    }

    public LocalDateTime getMeasurementTime() {
        return measurementTime;
    }

    public String getRoom() {
        return room;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        String at = measurementTime == null ? "" : "at=" + measurementTime.format(formatter);
        return "SensorData{" +
                at +
                ", room='" + room + '\'' +
                ", value=" + value +
                '}';
    }
}
