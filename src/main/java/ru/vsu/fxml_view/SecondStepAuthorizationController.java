package ru.vsu.fxml_view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.MailException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.vsu.FXEngine;
import ru.vsu.jpa.domain.GoogleAuthToUser;
import ru.vsu.jpa.domain.User;
import ru.vsu.services.UserService;
import ru.vsu.services.mail.EmailService;
import ru.vsu.services.security.SecurityService;
import ru.vsu.services.security.second_step_auth.GoogleAuthenticator;

import java.util.Optional;

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
    @FXML
    private TextField emailAuthTextField;

    private final SecurityService securityService;
    private final EmailService emailService;
    private final UserService userService;

    private Authentication authentication;
    private String randomEmailCode = null;

    public SecondStepAuthorizationController(SecurityService securityService, EmailService emailService, UserService userService) {
        this.securityService = securityService;
        this.emailService = emailService;
        this.userService = userService;
    }

    @FXML
    void initialize() {
        this.authentication = SecurityContextHolder.getContext().getAuthentication();
        appendStatusText("Current user id: " + authentication.getName());
    }

    @FXML
    void goBackToMainScreenAction(ActionEvent event) {
        this.randomEmailCode = null;
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
    void sendEmailAuthCode(ActionEvent event) {
        final Optional<User> user = userService.findUserById(Integer.parseInt(authentication.getName()));

        if (user.isEmpty()) {
            appendStatusText("User not found, something went wrong.");
            throw new RuntimeException("User not found, something went wrong");
        }

        this.randomEmailCode = RandomStringUtils.randomAlphabetic(6);

        try {
            emailService.sendMessageWithAuthorizationCode(user.get().getEmail(), randomEmailCode);
        } catch (final MailException e) {
            appendStatusText("Error, while sending email message: " + e.toString());
        }
    }

    @FXML
    void checkEmailAuthCode(ActionEvent event) {
        if (randomEmailCode == null) {
            appendStatusText("Please, press send email button before");
        } else {
            if (emailAuthTextField.getText().equals(this.randomEmailCode)) {
                this.randomEmailCode = null;
                FXEngine.showMainNoteStage();
            } else {
                appendStatusText("Wrong code, try again");
            }
        }
    }

    @FXML
    void loginIgnoreAllSecurity(ActionEvent event) {
        this.randomEmailCode = null;
        FXEngine.showMainNoteStage();
    }

    private void appendStatusText(String message) {
        statusArea.appendText("\n");
        statusArea.appendText(message);
    }
}