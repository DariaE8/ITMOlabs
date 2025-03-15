package cli.commands;
import models.Command;

public class RemoveGreaterKeyNull extends Command {
    public RemoveGreaterKeyNull() {
        super("remove_greater_key null", "удалить из коллекции все элементы, ключ которых превышает заданный");
    }

    //удалить из коллекции все элементы, ключ которых превышает заданный
    @Override
    public void run(String[] args) {
        if (args.length < 1) {
            terminal.printError("Ошибка: Не указан ключ для удаления.");
            return;
        }

        try {
            int key = Integer.parseInt(args[0]); // Парсим ключ из аргумента
            tm.removeGreaterKey(key);
        } catch (NumberFormatException e) {
            terminal.printError("Ошибка: Ключ должен быть числом.");
        }
    }
}