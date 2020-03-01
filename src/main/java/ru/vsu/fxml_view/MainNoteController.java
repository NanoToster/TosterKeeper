package ru.vsu.fxml_view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import ru.vsu.FXEngine;

/**
 * @author Ivan Rovenskiy
 * 01 March 2020
 */
@Component
@FxmlView("main_note_screen.fxml")
public class MainNoteController {
    @FXML
    void initialize() {
        // TODO [RIP]: 01/03/2020 add content to panel
    }

    @FXML
    void goBackToMainScreenAction(ActionEvent event) {
        FXEngine.showStartStage();
    }
}