package ru.vsu.fxml_view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.vsu.FXEngine;
import ru.vsu.jpa.domain.User;
import ru.vsu.jpa.domain.UserRoleEnum;
import ru.vsu.jpa.domain.hands.HandKeyPointsToUser;
import ru.vsu.services.UserService;
import ru.vsu.services.security.SecurityService;
import ru.vsu.services.security.first_step.HandService;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
@Component
@FxmlView("start_screen.fxml")
public class StartScreenController {
    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> userNameColumn;
    @FXML
    private TextArea statusArea;
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

    @FXML
    private Canvas handCanvas;

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
            List<String> keyPointForLoginList = startHandCapturingAndParseOutput();

            List<HandKeyPointsToUser> handKeyPointsToUserList = handService.findHandKeyPointsToUserListByUser(selectedUser);
            appendStatusText("Found hand list size: " + handKeyPointsToUserList.size());

            if (handService.isUserAuthentic(handKeyPointsToUserList, keyPointForLoginList)) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(selectedUser, null,
                        AuthorityUtils.createAuthorityList(UserRoleEnum.Admin.name()));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                appendStatusText("success login");
                FXEngine.showSecondAuthorizationStepStage();
            } else {
                appendStatusText("Login failed, please try to use password");
            }
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
    void captureHandAndRegisterNewUser(ActionEvent event) {
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

        final User registeredUser = userService.addNewUser(
                registrationUserNameTextField.getText(),
                registrationEmailTextField.getText(),
                registrationPasswordTextField.getText(),
                userSecretForGoogleAuth);

        appendStatusText("Please save this code to your Google Authenticator application!! You will see it only 1 time!");
        appendStatusText("========== SECRET START =========");
        appendStatusText(userSecretForGoogleAuth);
        appendStatusText("=========== SECRET END ==========");

        List<String> handPointsList = startHandCapturingAndParseOutput();
        handService.addNewHandKeyPointsToUser(registeredUser, handPointsList);

        updateUserList();
    }

    @FXML
    void initialize() {
        appendStatusText("Application started!");
        updateUserList();

        registrationEmailTextField.setText("tanker995@gmail.com");
        registrationUserNameTextField.setText("User name: " + new Date().getTime());
        passwordTextField.setText("p");
    }

    private List<String> startHandCapturingAndParseOutput() {
        try {
            ProcessBuilder capturingProcessBuilder = new ProcessBuilder("./run_mediapipe.sh");
            capturingProcessBuilder.directory(new File(System.getProperty("user.home") + "/Documents/mediapipe"));
            Process capturingProcess = capturingProcessBuilder.start();

            capturingProcess.waitFor();
            BufferedReader outputBufferedReader = new BufferedReader(new InputStreamReader(capturingProcess.getInputStream()));
            List<String> outputLinesList = outputBufferedReader.lines().collect(Collectors.toCollection(ArrayList::new));
            int indexOfLastFrameStart = outputLinesList.lastIndexOf("//// new frame");

            List<String> rawHandPointsList = outputLinesList.subList(indexOfLastFrameStart - 21, indexOfLastFrameStart);// 21 because we have 21 point
            drawHandOnCanvas(rawHandPointsList);

            return rawHandPointsList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Problems with hand capturing");
        }
    }

    private void drawHandOnCanvas(List<String> rawHandPointsList) {
        GraphicsContext gc = handCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, handCanvas.getWidth(), handCanvas.getHeight());
        gc.setFill(Color.RED);
        rawHandPointsList.forEach(rawPoint -> {
            String[] xAndYRaw = rawPoint.split(":");
            drawPointToHandCanvas(Double.parseDouble(xAndYRaw[0]), Double.parseDouble(xAndYRaw[1]));
        });
    }

    private void drawPointToHandCanvas(double x, double y) {
        GraphicsContext gc = handCanvas.getGraphicsContext2D();
        gc.fillOval(x * 700, y * 700, 7, 7);
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