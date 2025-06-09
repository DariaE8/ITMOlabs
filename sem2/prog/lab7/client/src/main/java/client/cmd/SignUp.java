package client.cmd;

import client.ConnectionHandler;
import client.LoginManager;
import client.prompts.UserPrompt;
import utils.Terminal;
import utils.AbstractPrompt;
import utils.Command;
import utils.CommandException;
import utils.RequestException;
import utils.Response;

import java.util.Objects;

import auth_utils.User;

public class SignUp extends Command {
    private Terminal terminal;
    private ConnectionHandler chandler;
    private LoginManager loginManager;

    /**
     * Конструктор команды информации.
     *
     * @param terminal терминал для вывода информации
     * @param chandler обработчик соединения с сервером
     * @throws NullPointerException если terminal или chandler равен null
     */
    public SignUp(Terminal terminal, ConnectionHandler chandler, LoginManager loginManager) {
        super("sign_up", "команда для регистрации в системе");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.chandler = Objects.requireNonNull(chandler, "Обработчик соединения не может быть null");
        this.loginManager = Objects.requireNonNull(loginManager, "Менеджер авторизации не может быть null");
    }
    
    /**
     * Выводит подробную информацию о коллекции билетов.
     *
     * @param args аргументы команды (игнорируются)
     * @throws CommandException если произошла ошибка при выполнении команды
     */
    @Override
    public void execute(String[] args) throws CommandException {
        try {
            User user = getUserPrompt();
            Response resp = chandler.request(new server_cmd.SignUp(user));
            if (resp.isError()) {
                throw new CommandException(resp.getMessage());
            }
            user.setId((int) resp.getData());
            terminal.println(resp.getMessage());
            loginManager.login(user);
        } catch (RequestException e) {
            throw new CommandException("Ошибка при обращении к серверу: " + e.getMessage());
        } catch (Exception e) {
            throw new CommandException("Возникла непредвиденная ошибка: " + e.getMessage());
        }
    }

    private User getUserPrompt() throws AbstractPrompt.InputCancelledException {
        terminal.println("Введите данные для входа:");
        UserPrompt prompt = new UserPrompt(terminal);
        return prompt.ask();
    }
}