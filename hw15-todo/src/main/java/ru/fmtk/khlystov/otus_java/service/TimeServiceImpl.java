package ru.fmtk.khlystov.otus_java.service;

import java.time.LocalDateTime;

public class TimeServiceImpl implements TimeService {
    @Override
    public LocalDateTime getNowDateTime() {
        return LocalDateTime.now();
    }
}
