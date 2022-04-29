package ru.fmtk.khlystov.otus_java.model;

import java.util.Arrays;
import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public static ObjectForMessage of(String... args) {
        var ofm = new ObjectForMessage();
        ofm.setData(Arrays.asList(args));
        return ofm;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
