package networking.objectprotocol.responses;

import model.Result;
import model.ResultDTO;
import networking.objectprotocol.Response;

import java.util.List;

public class GetAllResultsResponse implements Response {
    private List<ResultDTO> results;
    public GetAllResultsResponse(List<ResultDTO> results) {
        this.results = results;
    }
    public List<ResultDTO> getResults(){
        return this.results;
    }
}
