package ru.vsu.fxml_view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import ru.vsu.jpa.domain.HandInformation;
import ru.vsu.jpa.domain.User;
import ru.vsu.services.HandService;
import ru.vsu.services.UserService;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
@Component
@FxmlView("authorisation_screen.fxml")
public class AuthorisationScreenController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private ListView<?> userListView;
    @FXML
    private Label statusLabel;
    @FXML
    private ImageView handImageView;

    private final UserService userService;
    private final HandService handService;

    public AuthorisationScreenController(UserService userService, HandService handService) {
        this.userService = userService;
        this.handService = handService;
    }

    @FXML
    void loginViaHandAction(ActionEvent event) {

    }

    @FXML
    void loginViaPasswordAction(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert userListView != null : "fx:id=\"userListView\" was not injected: check your FXML file 'authorisation_screen.fxml'.";
        assert statusLabel != null : "fx:id=\"statusLabel\" was not injected: check your FXML file 'authorisation_screen.fxml'.";
        assert handImageView != null : "fx:id=\"handImageView\" was not injected: check your FXML file 'authorisation_screen.fxml'.";

        statusLabel.setText("Hello!");

        final Optional<User> userById = userService.findUserById(1);
        final Optional<HandInformation> handById = handService.findHandById(3);
        System.out.println("sdf");
    }
}
