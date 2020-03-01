package ru.vsu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
public class JavaFxApplication extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        this.applicationContext = new SpringApplicationBuilder()
                .sources(TosterKeeperBootApplication.class)
                .run(args);
    }

    @Override
    public void start(Stage stage) {
        FXEngine.startApplication(stage, applicationContext.getBean(FxWeaver.class));
        FXEngine.showStartStage();
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }
}