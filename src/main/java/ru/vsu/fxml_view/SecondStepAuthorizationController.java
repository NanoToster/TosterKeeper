package ru.vsu.fxml_view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import ru.vsu.FXEngine;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ivan Rovenskiy
 * 01 March 2020
 */
@Component
@FxmlView("second_step_authorization.fxml")
public class SecondStepAuthorizationController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField googleAuthentificatorTextField;

    @FXML
    void initialize() {

    }

    @FXML
    void goBackToMainScreenAction(ActionEvent event) {
        FXEngine.showMainStage();
    }
}
