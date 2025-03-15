package cli.commands;

import models.Command;
import models.Ticket;

public class UpdateId extends Command {
    public UpdateId() {
        super("update_id", "обновляет элемент коллекции по ID");
    }

    @Override
    public void run(String[] args) {
        if (args.length < 2) {
            terminal.println("Ошибка: необходимо передать ID и данные для обновления.");
            return;
        }

        try {
            int id = Integer.parseInt(args[0]);
            Ticket updatedTicket = Ticket.fromArgs(args); // Создаём Ticket из аргументов
            tm.update(id, updatedTicket);
            terminal.println("Объект обновлён.");
        } catch (NumberFormatException e) {
            terminal.println("Ошибка: ID должен быть числом.");
        }
    }
}


