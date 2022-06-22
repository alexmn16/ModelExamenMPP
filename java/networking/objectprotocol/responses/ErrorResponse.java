package networking.objectprotocol.responses;


import networking.objectprotocol.Response;

public class ErrorResponse implements Response {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
