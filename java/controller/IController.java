package controller;


import model.Game;
import model.Result;
import model.ResultDTO;
import model.User;

import java.util.Collection;

public interface IController {
    User login (User user, IObserver client) throws Exception;
    void addResult(Result result) throws Exception;
    Collection<ResultDTO> getAllResults();
    Game getRandomGame();
    Result getPoints(String guess, Game game, Result result);
}
