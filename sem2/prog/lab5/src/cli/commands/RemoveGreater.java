package cli.commands;

import models.Command;
import models.Ticket;
import java.util.Iterator;
import java.util.Map;

public class RemoveGreater extends Command {
    public RemoveGreater() {
        super("remove_greater {element}", "удалить из коллекции все элементы, превышающие заданный");
    }

    @Override
    public void run(String[] args) {
        if (args.length < 1) {
            terminal.printError("Ошибка: Не указан элемент для удаления.");
            return;
        }

        try {
            Ticket referenceTicket = Ticket.fromArgs(args);
            Iterator<Map.Entry<Integer, Ticket>> iterator = tm.getEntrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, Ticket> entry = iterator.next();
                if (entry.getValue().compareTo(referenceTicket) > 0) {
                    iterator.remove();
                }
            }
            terminal.printSuccess("Удалены все элементы, превышающие заданный.");
        } catch (Exception e) {
            terminal.printError("Ошибка: Некорректный формат элемента.");
        }
    }
}
