package client.cmd;

import java.util.Objects;

import client.ConnectionHandler;
import utils.Command;
import utils.CommandException;
import utils.RequestException;
import utils.Response;

public class Remove extends Command{
    private final ConnectionHandler chandler;

    
    public Remove(ConnectionHandler chandler) {
        super("remove", "удалить билет по указанному ID");
        this.chandler = Objects.requireNonNull(chandler, "Обработчик запросов не может быть null");
    }
    
    @Override
    public void execute(String args[]) {

    }

    public String execute_gui(long id) throws CommandException{
        try {
            Response resp = chandler.request(new server_cmd.Remove(id));

            if (resp.isError()) {
                throw new CommandException(resp.getMessage());
            }
            return resp.getMessage();
        } catch (IllegalArgumentException e) {
            throw new CommandException("Неверный аргумент: " + e.getMessage());
        } catch (RequestException e) {
            throw new CommandException("Ошибка сервера: " + e.getMessage());
        } catch (Exception e) {
            throw new CommandException("Ошибка при обновлении: " + e.getMessage());
        }

    }
}
