package ru.fmtk.khlystov.java_otus;

import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.fmtk.khlystov.java_otus.appcontainer.AppComponentsContainerImpl;
import ru.fmtk.khlystov.java_otus.config.AppConfig;
import ru.fmtk.khlystov.java_otus.services.EquationPreparer;
import ru.fmtk.khlystov.java_otus.services.IOService;
import ru.fmtk.khlystov.java_otus.services.PlayerService;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    @DisplayName("Из контекста тремя способами должен корректно доставаться компонент с проставленными полями")
    @ParameterizedTest(name = "Достаем по: {0}")
    @CsvSource(value = {
            "GameProcessor, ru.fmtk.khlystov.java_otus.services.GameProcessor",
            "GameProcessorImpl, ru.fmtk.khlystov.java_otus.services.GameProcessor",
            "gameProcessor, ru.fmtk.khlystov.java_otus.services.GameProcessor",

            "IOService, ru.fmtk.khlystov.java_otus.services.IOService",
            "IOServiceStreams, ru.fmtk.khlystov.java_otus.services.IOService",
            "ioService, ru.fmtk.khlystov.java_otus.services.IOService",

            "PlayerService, ru.fmtk.khlystov.java_otus.services.PlayerService",
            "PlayerServiceImpl, ru.fmtk.khlystov.java_otus.services.PlayerService",
            "playerService, ru.fmtk.khlystov.java_otus.services.PlayerService",

            "EquationPreparer, ru.fmtk.khlystov.java_otus.services.EquationPreparer",
            "EquationPreparerImpl, ru.fmtk.khlystov.java_otus.services.EquationPreparer",
            "equationPreparer, ru.fmtk.khlystov.java_otus.services.EquationPreparer"
    })
    public void shouldExtractFromContextCorrectComponentWithNotNullFields(String classNameOrBeanId,
                                                                          Class<?> rootClass) throws Exception {
        var ctx = new AppComponentsContainerImpl(AppConfig.class);

        assertThat(classNameOrBeanId).isNotEmpty();
        Object component;
        if (classNameOrBeanId.charAt(0) == classNameOrBeanId.toUpperCase().charAt(0)) {
            Class<?> gameProcessorClass = Class.forName("ru.fmtk.khlystov.java_otus.services." + classNameOrBeanId);
            assertThat(rootClass).isAssignableFrom(gameProcessorClass);

            component = ctx.getAppComponent(gameProcessorClass);
        } else {
            component = ctx.getAppComponent(classNameOrBeanId);
        }
        assertThat(component).isNotNull();
        assertThat(rootClass).isAssignableFrom(component.getClass());

        var fields = Arrays.stream(component.getClass().getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .peek(f -> f.setAccessible(true))
                .collect(Collectors.toList());

        for (var field : fields) {
            var fieldValue = field.get(component);
            assertThat(fieldValue).isNotNull().isInstanceOfAny(IOService.class, PlayerService.class,
                    EquationPreparer.class, PrintStream.class, Scanner.class);
        }
    }
}