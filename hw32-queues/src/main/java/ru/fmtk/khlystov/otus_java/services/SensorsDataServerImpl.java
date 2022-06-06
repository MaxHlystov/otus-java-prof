package ru.fmtk.khlystov.otus_java.services;

import ru.fmtk.khlystov.otus_java.api.SensorsDataChannel;
import ru.fmtk.khlystov.otus_java.api.SensorsDataServer;
import ru.fmtk.khlystov.otus_java.api.model.SensorData;

public class SensorsDataServerImpl implements SensorsDataServer {

    private final SensorsDataChannel sensorsDataChannel;

    public SensorsDataServerImpl(SensorsDataChannel sensorsDataChannel) {
        this.sensorsDataChannel = sensorsDataChannel;
    }

    @Override
    public void onReceive(SensorData sensorData) {
        sensorsDataChannel.push(sensorData);
    }
}
