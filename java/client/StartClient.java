package client;

import controller.IController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import networking.objectprotocol.ObjectProxy;

import java.io.IOException;
import java.util.Properties;

public class StartClient extends Application{


    private static  int defaultChatPort = 55555;
    private static String defaultServer = "localhost";


    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties properties = new Properties();

        try {
            properties.load(StartClient.class.getResourceAsStream("/client.properties"));
        } catch (IOException exception) {
            System.out.println("Cannot find client.properties " + exception);
        }

        String serverIP = properties.getProperty("server.host", defaultServer);
        int serverPort = defaultChatPort;
        try {
            serverPort = Integer.parseInt(properties.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IController server = new ObjectProxy(serverIP, serverPort);

        FXMLLoader loginLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/login.fxml"));
        Parent root= loginLoader.load();

        FXMLLoader mainLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main.fxml"));
        Parent mainParent = mainLoader.load();


        LoginController loginController = loginLoader.getController();
        loginController.setMainParent(mainParent);
        loginController.setServer(server);


        MainController mainController = mainLoader.getController();
        mainController.setServer(server);
        loginController.setMainController(mainController);

        primaryStage.setTitle("Game");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}
