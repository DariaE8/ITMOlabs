package client;

import auth_utils.User;
import utils.Terminal;

public class LoginManager {
    private User currentUser;

    public void login(User user) {
        this.currentUser = user;
        Terminal.setLoggedShell(user.getUsername());
    }

    public void logout() {
        this.currentUser = null;
        Terminal.setDefaultShell();
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
