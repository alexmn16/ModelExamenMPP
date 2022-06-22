package networking.objectprotocol.requests;

import model.Game;
import model.Result;
import model.User;
import networking.objectprotocol.Request;

import java.time.LocalDateTime;

public class AddResultRequest implements Request {
    private Result result;


    public AddResultRequest(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }
}
