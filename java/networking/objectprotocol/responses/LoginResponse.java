package networking.objectprotocol.responses;

import model.User;
import networking.objectprotocol.Response;

public class LoginResponse implements Response {
    private User user;

    public LoginResponse(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
