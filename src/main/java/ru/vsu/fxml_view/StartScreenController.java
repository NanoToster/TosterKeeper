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
import ru.vsu.jpa.domain.HandInformation;
import ru.vsu.jpa.domain.User;
import ru.vsu.services.HandService;
import ru.vsu.services.UserService;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
@Component
@FxmlView("authorisation_screen.fxml")
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

    private final UserService userService;
    private final HandService handService;
    private final AuthenticationManager authenticationManager;

    public StartScreenController(UserService userService, HandService handService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.handService = handService;
        this.authenticationManager = authenticationManager;
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
                Authentication authToken = new UsernamePasswordAuthenticationToken(selectedUser.getId(), passwordTextField.getText());
                try {
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
    void initialize() {
        appendStatusText("Application started!");

//        userService.addNewUser("security", "sec@sec.com", "p");

        final Optional<User> userById = userService.findUserById(1);
        final Optional<HandInformation> handById = handService.findHandById(3);

        userTableView.setItems(FXCollections.observableArrayList(userService.findAllUsers()));
        userNameColumn.setCellValueFactory(userCell -> new SimpleStringProperty(userCell.getValue().getName()));
    }

    private void appendStatusText(String message) {
        statusArea.appendText("\n");
        statusArea.appendText(message);
    }
}
