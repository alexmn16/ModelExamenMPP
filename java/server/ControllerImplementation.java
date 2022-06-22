package server;

import controller.IController;
import controller.IObserver;
import model.Game;
import model.Result;
import model.ResultDTO;
import model.User;
import services.Service;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControllerImplementation implements IController{

    private Service service;
    private Map<Integer, IObserver> loggedUsers;

    public ControllerImplementation(Service service) {
        this.service = service;
        this.loggedUsers = new ConcurrentHashMap<>();
    }


    @Override
    public synchronized User login(User user, IObserver client) throws Exception {
        User searchedUser = this.service.findUserByAlias(user.getAlias());
        if (searchedUser != null){
            if (loggedUsers.get(searchedUser.getId()) != null) {
                throw new Exception("User already logged in");
            }
            loggedUsers.put(searchedUser.getId(), client);
            return searchedUser;
        } else {
            throw new Exception("Authentication failed");
        }
    }

    @Override
    public synchronized void addResult(Result result) throws Exception {
        try{
            this.service.addResult(result);
            var results = (List<ResultDTO>) service.getAllResultsDTO();
            for (IObserver observer:loggedUsers.values())
                observer.resultAdded(results);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage());
        }
    }


    @Override
    public synchronized Collection<ResultDTO> getAllResults() {
        return service.getAllResultsDTO();
    }

    @Override
    public synchronized Game getRandomGame() {
        return service.getRandomGame();
    }

    @Override
    public Result getPoints(String guess, Game game, Result result) {
        return service.resultThisRound(guess, game, result);
    }
}
