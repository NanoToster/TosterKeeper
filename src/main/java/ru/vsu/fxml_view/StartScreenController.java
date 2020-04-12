package ru.vsu.fxml_view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.vsu.FXEngine;
import ru.vsu.jpa.domain.User;
import ru.vsu.services.security.first_step.HandService;
import ru.vsu.services.UserService;
import ru.vsu.services.security.SecurityService;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
@Component
@FxmlView("start_screen.fxml")
public class StartScreenController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> userNameColumn;
    @FXML
    private TextArea statusArea;
    @FXML
    private ImageView handImageView;
    @FXML
    private TextField passwordTextField;

    // ------------------- Registration UI start
    @FXML
    private TextField registrationEmailTextField;
    @FXML
    private TextField registrationUserNameTextField;
    @FXML
    private TextField registrationPasswordTextField;
    // ------------------- Registration UI end

    private final UserService userService;
    private final HandService handService;
    private final AuthenticationManager authenticationManager;
    private final SecurityService securityService;

    public StartScreenController(UserService userService, HandService handService, AuthenticationManager authenticationManager, SecurityService securityService) {
        this.userService = userService;
        this.handService = handService;
        this.authenticationManager = authenticationManager;
        this.securityService = securityService;
    }

    @FXML
    void loginViaHandAction(ActionEvent event) {
        final User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            appendStatusText("Please, choose user.");
        } else {
            appendStatusText("Login via hand not implemented right now, sorry.");
        }
    }

    @FXML
    void loginViaPasswordAction(ActionEvent event) {
        final User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            appendStatusText("Please, choose user.");
        } else {
            if (StringUtils.isNotBlank(passwordTextField.getText())) {
                try {
                    Authentication authToken = new UsernamePasswordAuthenticationToken(selectedUser.getId(), passwordTextField.getText());
                    authToken = authenticationManager.authenticate(authToken);
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    appendStatusText("success login");
                    FXEngine.showSecondAuthorizationStepStage();
                } catch (AuthenticationException e) {
                    appendStatusText("Login failure, please try again. Reason: " + e.getMessage());
                }
            } else {
                appendStatusText("please, enter password for selected user.");
            }
        }
    }

    @FXML
    void registerNewUser(ActionEvent event) {
        if (StringUtils.isBlank(registrationEmailTextField.getText()) || !registrationEmailTextField.getText().contains("@")) {
            // TODO [RIP]: 01/03/2020 we can add regexp here
            appendStatusText("Invalid email");
            return;
        }
        if (StringUtils.isBlank(registrationUserNameTextField.getText())) {
            appendStatusText("Invalid username");
            return;
        }
        if (StringUtils.isBlank(registrationPasswordTextField.getText())) {
            appendStatusText("Invalid password");
            return;
        }

        final String userSecretForGoogleAuth = securityService.generateUserSecretForGoogleAuth();

        userService.addNewUser(
                registrationUserNameTextField.getText(),
                registrationEmailTextField.getText(),
                registrationPasswordTextField.getText(),
                userSecretForGoogleAuth);

        appendStatusText("Please save this code to your Google Authenticator application!! You will see it only 1 time!");
        appendStatusText("========== SECRET START =========");
        appendStatusText(userSecretForGoogleAuth);
        appendStatusText("=========== SECRET END ==========");

        updateUserList();
    }

    @FXML
    void initialize() {
        appendStatusText("Application started!");

        updateUserList();

        registrationEmailTextField.setText("tanker995@gmail.com");
        registrationUserNameTextField.setText(LocalDate.now().toString());
        passwordTextField.setText("p");
    }

    private void updateUserList() {
        userTableView.setItems(FXCollections.observableArrayList(userService.findAllActiveUsers()));
        userNameColumn.setCellValueFactory(userCell -> new SimpleStringProperty(userCell.getValue().getName()));
    }

    private void appendStatusText(String message) {
        statusArea.appendText("\n");
        statusArea.appendText(message);
    }
}