package utils;

import auth_utils.User;

public interface UserAwareCommand {
    void attachUser(User user);
}