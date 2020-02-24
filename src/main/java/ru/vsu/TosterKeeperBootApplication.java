package ru.vsu;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
@SpringBootApplication
public class TosterKeeperBootApplication {
    public static void main(String[] args) {
        // we start FX application from common boot main method
        Application.launch(JavaFxApplication.class, args);
    }
}
