package client.cmd;

import client.LoginManager;
import utils.Terminal;
import utils.Command;
import utils.CommandException;

import java.util.Objects;

public class SignOut extends Command {
    private Terminal terminal;
    private LoginManager loginManager;

    public SignOut(Terminal terminal, LoginManager loginManager) {
        super("sign_out", "команда для выхода из системы");
        this.terminal = terminal;
        this.loginManager = Objects.requireNonNull(loginManager, "Менеджер авторизации не может быть null");
    }
    
    @Override
    public void execute(String[] args) throws CommandException {
        try {
            String username = loginManager.getCurrentUser().getUsername();
            loginManager.logout();
            terminal.println("Пользователь " + username + " успешно вышел из системы");
        } catch (Exception e) {
            throw new CommandException("Возникла непредвиденная ошибка: " + e.getMessage());
        }
    }
}