package ru.fmtk.khlystov.otus_java.dataprocessor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.fmtk.khlystov.otus_java.model.Measurement;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        //группирует выходящий список по name, при этом суммирует поля value
        return data.stream()
                .collect(Collectors.groupingBy(
                        Measurement::getName,
                        LinkedHashMap::new,
                        Collectors.summingDouble(Measurement::getValue)
                ));
    }
}
