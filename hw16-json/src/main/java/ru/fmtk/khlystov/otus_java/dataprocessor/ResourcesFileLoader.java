package ru.fmtk.khlystov.otus_java.dataprocessor;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.fmtk.khlystov.otus_java.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private final Gson gson = new Gson();

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileProcessException("Can't read file " + fileName);
        }
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        return gson.fromJson(reader, new TypeToken<ArrayList<Measurement>>() {
        }.getType());
    }
}
