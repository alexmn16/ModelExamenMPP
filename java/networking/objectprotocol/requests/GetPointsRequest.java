package networking.objectprotocol.requests;

import model.Game;
import model.Result;
import networking.objectprotocol.Request;

public class GetPointsRequest implements Request {
    private String guess;
    private Game game;
    private Result result;

    public GetPointsRequest(String guess, Game game, Result result) {
        this.guess = guess;
        this.game = game;
        this.result = result;
    }

    public String getGuess() {
        return guess;
    }

    public Game getGame() {
        return game;
    }

    public Result getResult() {
        return result;
    }
}
