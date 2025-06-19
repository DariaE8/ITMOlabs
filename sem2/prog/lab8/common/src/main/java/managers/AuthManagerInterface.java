package managers;

import auth_utils.User;

public interface AuthManagerInterface {
    Integer validate(User user) throws Exception;
    Integer register(User user) throws IllegalArgumentException;
}