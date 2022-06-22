package networking.objectprotocol;

import controller.IController;
import controller.IObserver;
import model.Game;
import model.Result;
import model.ResultDTO;
import model.User;
import networking.objectprotocol.requests.*;
import networking.objectprotocol.responses.*;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ObjectProxy implements IController {

    private String host;
    private int port;
    private IObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ObjectProxy(String host, int port){
        this.host = host;
        this.port = port;
        this.qresponses = new LinkedBlockingQueue<Response>();
    }

    private void closeConnection() {
        this.finished = true;

        try {
            this.input.close();
            this.output.close();
            this.connection.close();
            this.client = null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws Exception {
        try {
            this.output.writeObject(request);
            this.output.flush();
        } catch (IOException ex) {
            throw new Exception("Error sending object " + ex);
        }
    }

    private Response readResponse(){
        Response response = null;
        try {
            response = (Response) this.qresponses.take();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return response;
    }

    private void initializeConnection(){
        try {
            this.connection = new Socket(this.host, this.port);
            this.output = new ObjectOutputStream(this.connection.getOutputStream());
            this.output.flush();
            this.input = new ObjectInputStream(this.connection.getInputStream());
            this.finished = false;
            this.startReader();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(UpdateResponse update) {
        if (update instanceof ResultAddedResponse resultAddedResponse){
            try{
                this.client.resultAdded(resultAddedResponse.getResults());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public User login(User user, IObserver client) throws Exception {
        initializeConnection();
        sendRequest(new LoginRequest(user));
        Response response = readResponse();
        if (response instanceof LoginResponse loginResponse)
        {
            this.client = client;
            return loginResponse.getUser();
        }
        if (response instanceof ErrorResponse){
            ErrorResponse errorResponse = (ErrorResponse) response;
            closeConnection();
            throw new Exception(errorResponse.getMessage());
        }
        return null;
    }

    @Override
    public void addResult(Result result) throws Exception {
        sendRequest(new AddResultRequest(result));
    }


    @Override
    public Collection<ResultDTO> getAllResults() {

        try {
            sendRequest(new GetAllResultsRequest());
            Response response = readResponse();
            if (response instanceof ErrorResponse) {
                ErrorResponse errorResponse = (ErrorResponse) response;
                closeConnection();
                throw new Exception(errorResponse.getMessage());
            }
            GetAllResultsResponse resp = (GetAllResultsResponse) response;
            return resp.getResults();
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public Game getRandomGame() {
        try{
            sendRequest(new GetRandomGameRequest());
            Response response = readResponse();
            if (response instanceof ErrorResponse) {
                ErrorResponse errorResponse = (ErrorResponse) response;
                closeConnection();
                throw new Exception(errorResponse.getMessage());
            }
            GetRandomGameResponse resp = (GetRandomGameResponse) response;
            return resp.getGame();

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public Result getPoints(String guess, Game game, Result result) {
        try{
            sendRequest(new GetPointsRequest(guess, game, result));
            Response response = readResponse();
            if (response instanceof ErrorResponse) {
                ErrorResponse errorResponse = (ErrorResponse) response;
                closeConnection();
                throw new Exception(errorResponse.getMessage());
            }
            GetPointsResponse resp = (GetPointsResponse) response;
            return resp.getResult();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }

    private class ReaderThread implements Runnable {
        private ReaderThread() {
        }

        public void run() {
            while (!ObjectProxy.this.finished) {
                try {

                    Object response = ObjectProxy.this.input.readObject();
                    System.out.println("response received " + response);
                    if (response instanceof UpdateResponse) {
                        ObjectProxy.this.handleUpdate((UpdateResponse) response);
                    } else {
                        try {
                            ObjectProxy.this.qresponses.put((Response) response);
                        } catch (InterruptedException var3) {
                            var3.printStackTrace();
                        }
                    }
                } catch (IOException var4) {
                    throw new RuntimeException(var4.getMessage());
                } catch (ClassNotFoundException var5) {
                    System.out.println("Reading error " + var5);
                }

            }
        }
    }
}
