package server;

import controller.IController;
import model.Game;
import networking.utils.AbstractServer;
import networking.utils.ObjectConcurrentServer;
import persistance.repository.orm.GameHbmRepository;
import persistance.repository.orm.ResultHbmRepository;
import persistance.repository.orm.UserHbmRepository;

import services.Service;


import java.io.IOException;
import java.util.Properties;

public class StartServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {
        Properties serverProperties = new Properties();

        try{
            serverProperties.load(StartServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProperties.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties " + e);
            return;
        }
        UserHbmRepository userHbmRepository = new UserHbmRepository();
        GameHbmRepository gameHbmRepository =  new GameHbmRepository();
        ResultHbmRepository resultHbmRepository = new ResultHbmRepository();

        Service service = new Service(userHbmRepository, gameHbmRepository, resultHbmRepository);

        IController controller = new ControllerImplementation(service);

        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProperties.getProperty("server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new ObjectConcurrentServer(serverPort, controller);
        try {
            server.start();
        } catch (Exception e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
