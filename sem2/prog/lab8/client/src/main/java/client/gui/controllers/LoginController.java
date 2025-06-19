package client.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import client.cmd.SignIn;
import client.cmd.SignUp;


import java.io.IOException;

import client.ConnectionHandler;
import client.LoginManager;
import javafx.event.ActionEvent;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Label messageLabel;

    private ConnectionHandler chandler;
    private LoginManager loginManager;

    public void init(ConnectionHandler chandler, LoginManager loginManager) {
        this.chandler = chandler;
        this.loginManager = loginManager;
    }

    /**
     * Обработчик нажатия кнопки Login
     */
    @FXML
    private void onLoginClicked(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Пожалуйста, заполните логин и пароль");
            return;
        }

        // Здесь вызывается метод аутентификации, который ты реализуешь
        if (authenticateUser(username, password)) {
            pass();
        }
    

        // if (success) {
        //     messageLabel.setText("Успешный вход");
        //     pass();
        // } else {
        //     messageLabel.setText("Неверный логин или пароль");
        // }
    }

    /**
     * Обработчик нажатия кнопки Register
     */
    @FXML
    private void onRegisterClicked(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Пожалуйста, заполните логин и пароль");
            return;
        }

        // Здесь вызывается метод регистрации, который ты реализуешь
        // boolean success = registerUser(username, password);
        if (registerUser(username, password)) {
            pass();
        }

        // if (success) {
        //     messageLabel.setText("Регистрация успешна, войдите в систему");
        // } else {
        //     messageLabel.setText("Ошибка регистрации (возможно, пользователь уже существует)");
        // }
    }

    private boolean authenticateUser(String username, String password) {
        // return true;
        System.out.printf("Попытка аутентификации: %s / %s\n", username, password);
        SignIn sign_in = new SignIn(chandler, loginManager);
        try {
            sign_in.execute_gui(new String[]{username, password});
            return true;
        } catch (Exception e){
            messageLabel.setText(e.getMessage());
            return false;
        }
    }

    private boolean registerUser(String username, String password) {
        System.out.printf("Попытка регистрации: %s / %s\n", username, password);
        SignUp sign_up = new SignUp(chandler, loginManager);
        try {
            sign_up.execute_gui(new String[]{username, password});
            return true;
        } catch (Exception e){
            messageLabel.setText(e.getMessage());
            return false;
        }
    }

    private void pass() {
        // Заглушка: без проверки, просто открыть главное окно
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui/views/main.fxml"));
            Parent root = loader.load();

            MainController controller = loader.getController();
            controller.init(chandler, loginManager);

            Stage stage = new Stage();
            stage.setTitle("Главное окно");
            stage.setScene(new Scene(root));
            stage.show();

            // Закрыть окно логина
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            // В реальном приложении — показать ошибку пользователю
        }
    }
}
