package networking.objectprotocol.responses;

import model.Game;
import networking.objectprotocol.Response;

public class GetRandomGameResponse implements Response {
    private Game game;
    public GetRandomGameResponse(Game game) {
        this.game = game;
    }
    public Game getGame(){
        return game;
    }
}
