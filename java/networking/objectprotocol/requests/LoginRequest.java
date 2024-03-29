package networking.objectprotocol.requests;

import model.User;
import networking.objectprotocol.Request;

public class LoginRequest implements Request {
    private User user;

    public LoginRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
