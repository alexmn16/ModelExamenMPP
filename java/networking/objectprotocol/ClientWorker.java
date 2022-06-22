package networking.objectprotocol;

import controller.IController;
import controller.IObserver;
import model.Result;
import model.ResultDTO;
import model.User;
import model.Game;
import networking.objectprotocol.requests.*;
import networking.objectprotocol.responses.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientWorker implements Runnable, IObserver{
    private IController server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientWorker(IController controller, Socket connection) {
        this.server = controller;
        this.connection = connection;

        try {
            this.output = new ObjectOutputStream(connection.getOutputStream());
            this.output.flush();
            this.input = new ObjectInputStream(connection.getInputStream());
            this.connected = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void run() {
        while (this.connected) {
            try {
                Object request = this.input.readObject();
                Object response = this.handleRequest((Request) request);
                if (response != null) {
                    this.sendResponse((Response) response);
                }
            } catch (IOException var4) {
                var4.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        try {
            this.input.close();
            this.output.close();
            this.connection.close();
        } catch (IOException ex) {
            System.out.println("Error " + ex);
        }
    }

    private Response handleRequest(Request request) {
        if (request instanceof LoginRequest loginRequest){
            System.out.println("Login request ...");
            User user = loginRequest.getUser();
            try{
                return new LoginResponse(this.server.login(user, this));

            } catch (Exception ex) {
                this.connected = false;
                return new ErrorResponse(ex.getMessage());
            }
        }
        else if (request instanceof AddResultRequest addResultRequest)
        {
            System.out.println("Add result request ...");

            try {
                this.server.addResult(addResultRequest.getResult());
                var results = (List) server.getAllResults();
                return new ResultAddedResponse(results);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(request instanceof GetAllResultsRequest getAllResultsRequest) {
            System.out.println("Get all results request ...");
            try {
                List<ResultDTO> results = (List<ResultDTO>) this.server.getAllResults();
                return new GetAllResultsResponse(results);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (request instanceof GetRandomGameRequest getRandomGameRequest){
            System.out.println("Get random game request ...");
            try{
                Game game = this.server.getRandomGame();
                return new GetRandomGameResponse(game);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (request instanceof GetPointsRequest getPointsRequest){
            System.out.println("Get points request ...");
            try{
                Result result = server.getPoints(getPointsRequest.getGuess(), getPointsRequest.getGame(), getPointsRequest.getResult());
                return new GetPointsResponse(result);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        this.output.writeObject(response);
        this.output.flush();
    }

    @Override
    public void resultAdded(List<ResultDTO> results) throws Exception {
        try{
            sendResponse(new ResultAddedResponse(results));

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
