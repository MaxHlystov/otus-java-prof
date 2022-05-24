package ru.fmtk.khlystov.java_otus.config;

import ru.fmtk.khlystov.java_otus.services.EquationPreparer;
import ru.fmtk.khlystov.java_otus.services.EquationPreparerImpl;
import ru.fmtk.khlystov.java_otus.services.GameProcessor;
import ru.fmtk.khlystov.java_otus.services.GameProcessorImpl;
import ru.fmtk.khlystov.java_otus.services.IOService;
import ru.fmtk.khlystov.java_otus.appcontainer.api.AppComponent;
import ru.fmtk.khlystov.java_otus.appcontainer.api.AppComponentsContainerConfig;
import ru.fmtk.khlystov.java_otus.services.IOServiceStreams;
import ru.fmtk.khlystov.java_otus.services.PlayerService;
import ru.fmtk.khlystov.java_otus.services.PlayerServiceImpl;

@AppComponentsContainerConfig(order = 1)
public class AppConfig {

    @AppComponent(order = 0, name = "equationPreparer")
    public EquationPreparer equationPreparer(){
        return new EquationPreparerImpl();
    }

    @AppComponent(order = 1, name = "playerService")
    public PlayerService playerService(IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }

    @AppComponent(order = 2, name = "gameProcessor")
    public GameProcessor gameProcessor(IOService ioService,
                                       PlayerService playerService,
                                       EquationPreparer equationPreparer) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }

    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }

}
