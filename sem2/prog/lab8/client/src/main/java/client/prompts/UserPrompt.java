package client.prompts;

import utils.Terminal;
import utils.AbstractPrompt;

import java.util.function.Function;

import auth_utils.User;


public class UserPrompt extends AbstractPrompt<User> {

    public UserPrompt(Terminal terminal) {
        super(terminal);
    }

    @Override
    public User ask() throws InputCancelledException {
        try {

            String login = promptFor("login", Function.identity(),"Ошибка при вводе login");
            String password = promptFor("password", Function.identity(),"Ошибка при вводе password");
            return new User(0, login, password);
        } catch (Exception e) {
            if (terminal.checkScanner()) {
                terminal.printError("Произошла непредвиденная ошибка: " + e.getMessage());
                return null;
            } else {                
                throw new InputCancelledException(e.getMessage());
            }
        }
    }
}

