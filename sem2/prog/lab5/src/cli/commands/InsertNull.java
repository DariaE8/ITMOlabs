package cli.commands;
import cli.prompts.TicketPrompt;
import models.Command;
import models.Ticket;

public class InsertNull extends Command {
    public InsertNull() {
        super("insert", "добавить новый элемент с заданным ключом");
    }

    @Override
    public void run(String[] args) {
        if (args.length < 1) {
            terminal.printError("Ошибка: Не указан ключ для вставки.");
            return;
        }

        try {
            int idx = Integer.parseInt(args[0]); // Получаем id из аргумента

            TicketPrompt tp = new TicketPrompt(terminal);
            Ticket ticket = tp.ask();
            
            tm.insert(idx, ticket); // Вставляем объект с ключом
        } catch (NumberFormatException e) {
            terminal.printError("Ошибка: Неверный формат ключа. Ожидалось целое число.");
        } catch (Exception e) {
            terminal.printError("Ошибка ввода: " + e.getMessage());
        }
    }
}