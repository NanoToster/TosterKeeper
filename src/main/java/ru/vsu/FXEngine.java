package ru.vsu;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.vsu.fxml_view.MainNoteController;
import ru.vsu.fxml_view.SecondStepAuthorizationController;
import ru.vsu.fxml_view.StartScreenController;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
public class FXEngine {
    private static Stage stage;
    private static FxWeaver fxWeaver;

    public static void startApplication(Stage stage, FxWeaver fxWeaver) {
        FXEngine.stage = stage;
        FXEngine.fxWeaver = fxWeaver;
    }

    public static void showStartStage() {
        SecurityContextHolder.clearContext();
        Parent root = fxWeaver.loadView(StartScreenController.class);
        showStage(root);
    }

    public static void showSecondAuthorizationStepStage() {
        Parent root = fxWeaver.loadView(SecondStepAuthorizationController.class);
        showStage(root);
    }

    public static void showMainNoteStage() {
        Parent root = fxWeaver.loadView(MainNoteController.class);
        showStage(root);
    }

    private static void showStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}