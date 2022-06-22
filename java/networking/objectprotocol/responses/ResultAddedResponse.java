package networking.objectprotocol.responses;

import model.Result;
import model.ResultDTO;
import networking.objectprotocol.Response;
import networking.objectprotocol.UpdateResponse;

import java.util.List;

public class ResultAddedResponse implements UpdateResponse {

    private List<ResultDTO> results;
    public ResultAddedResponse(List<ResultDTO> results) {
        this.results = results;
    }

    public List<ResultDTO> getResults() {
        return results;
    }
}
