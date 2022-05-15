package ru.fmtk.khlystov.otus_java.dataprocessor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;

public class FileSerializer implements Serializer {
    private final Gson gson = new Gson();
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            gson.toJson(data, fileWriter);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
