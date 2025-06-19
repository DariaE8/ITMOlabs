package client.gui;

import java.io.IOException;

import client.ConnectionHandler;
import client.LoginManager;
import client.gui.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private ConnectionHandler connectionHandler;
    private LoginManager loginManager;

    @Override
    public void start(Stage stage) throws Exception {
        initializeComponents();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui/views/login.fxml"));
        Scene scene = new Scene(loader.load());

        LoginController controller = loader.getController();
        controller.init(connectionHandler, loginManager);

        stage.setScene(scene);
        stage.setTitle("Ticket Manager - Авторизация");
        stage.show();
    }


    private void initializeComponents() throws IOException {
        this.loginManager = new LoginManager();
        this.connectionHandler = new ConnectionHandler();
        connectionHandler.init(this.loginManager);
    }

    public static void main(String[] args) {
        launch();
    }
}
