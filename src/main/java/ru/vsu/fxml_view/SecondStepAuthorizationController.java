package ru.vsu.fxml_view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.vsu.FXEngine;
import ru.vsu.jpa.domain.GoogleAuthToUser;
import ru.vsu.services.security.second_step_auth.GoogleAuthenticator;
import ru.vsu.services.security.SecurityService;

/**
 * @author Ivan Rovenskiy
 * 01 March 2020
 */
@Component
@FxmlView("second_step_authorization.fxml")
public class SecondStepAuthorizationController {
    @FXML
    private TextArea statusArea;
    @FXML
    private TextField googleAuthenticatorTextField;

    private final SecurityService securityService;

    public SecondStepAuthorizationController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @FXML
    void initialize() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        appendStatusText("Current user id: " + authentication.getName());
    }

    @FXML
    void goBackToMainScreenAction(ActionEvent event) {
        FXEngine.showStartStage();
    }

    @FXML
    void checkGoogleAuthCode(ActionEvent event) {
        final String userIdRaw = SecurityContextHolder.getContext().getAuthentication().getName();
        final GoogleAuthToUser googleAuthByUserId = securityService.findGoogleAuthByUserId(Integer.parseInt(userIdRaw));

        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        if (googleAuthenticator.authorise(
                new GoogleAuthenticator.Input(
                        googleAuthenticatorTextField.getText(),
                        googleAuthByUserId.getUserSecret()))) {
            FXEngine.showMainNoteStage();
        } else {
            appendStatusText("Failed, try again!");
        }
    }

    @FXML
    void loginIgnoreAllSecurity(ActionEvent event) {
        FXEngine.showMainNoteStage();
    }

    private void appendStatusText(String message) {
        statusArea.appendText("\n");
        statusArea.appendText(message);
    }
}