package cli.commands;

import utils.Command;
import utils.CommandException;


/**
 * Команда для завершения работы приложения.
 * Завершает программу с кодом возврата 0 (успешное завершение).
 * Внимание: не сохраняет данные перед выходом!
 */
public class Exit extends Command {
    /**
     * Конструктор команды Exit.
     */
    public Exit() {
        super("exit", "завершить программу");
    }

    /**
     * Выполняет завершение работы приложения.
     * 
     * @param args аргументы команды (игнорируются)
     * @throws CommandException никогда не выбрасывается (переопределено для совместимости)
     */
    @Override
    public void execute(String[] args) throws CommandException {
        System.exit(0);
    }
}