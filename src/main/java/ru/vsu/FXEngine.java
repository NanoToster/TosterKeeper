package ru.vsu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
public class FXEngine {
    private static Scene mainMenuScene;
    private static Stage primaryStage;

    static void initFx(Stage stage) {
        primaryStage = stage;

        mainMenuScene = new Scene(FXEngine.loadFXML("fxml_stages/authorisation_screen.fxml"));
//        mainMenuScene.getStylesheets().add("ru/vsu/styles/main_menu.css");

        setPrimaryScene(mainMenuScene);
    }

    public static void setPrimaryScene(Scene primaryScene) {
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    private static Parent loadFXML(String fxmlFileName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(JavaFxApplication.class.getResource(fxmlFileName));
            return fxmlLoader.load();
        } catch (final IOException ex) {
            System.out.println("Some problems with files: " + ex);
            throw new NullPointerException();
        }
    }

//    public static Scene getMainMenuScene() {
//        return mainMenuScene;
//    }
//
//    public static Scene getGameFieldScene() {
//        Scene gameFieldScene = new Scene(FXEngine.loadFXML("game_field.fxml"));
//        gameFieldScene.getStylesheets().add("ru/vsu/styles/game_field.css");
//        return gameFieldScene;
//    }
}
