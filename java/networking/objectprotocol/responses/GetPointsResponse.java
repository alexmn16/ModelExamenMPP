package networking.objectprotocol.responses;

import model.Result;
import networking.objectprotocol.Response;

public class GetPointsResponse implements Response {
    private Result result;

    public GetPointsResponse(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }
}
