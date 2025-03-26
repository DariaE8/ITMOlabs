package cli.commands;

import cli.DumpManager;
import utils.Command;
import models.TicketManager;
import java.io.IOException;
import java.util.Objects;

import utils.CommandException;

/**
 * Команда для сохранения коллекции билетов в файл.
 */
public class Save extends Command {
    private final DumpManager dumpManager;
    private final TicketManager ticketManager;

    /**
     * Конструктор команды Save.
     *
     * @param dumpManager менеджер для работы с файлами данных
     * @param ticketManager менеджер коллекции билетов
     * @throws NullPointerException если ticketManager или dumpManager равен null
     */
    public Save(DumpManager dumpManager, TicketManager ticketManager) {
        super("save", "сохранить коллекцию в файл");
        this.dumpManager = Objects.requireNonNull(dumpManager, "DumpManager не может быть null");
        this.ticketManager = Objects.requireNonNull(ticketManager, "TicketManager не может быть null");
    }

    /**
     * Выполняет сохранение коллекции в файл.
     *
     * @param args аргументы команды (не используются)
     * @throws CommandException если произошла ошибка при сохранении
     */
    @Override
    public void execute(String[] args) throws CommandException {
        try {
            dumpManager.save(ticketManager.toCSV());
        } catch (IOException e) {
            throw new CommandException("Ошибка сохранения: " + e.getMessage());
        }
    }

    /**
     * Возвращает строку с описанием команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "Сохраняет текущую коллекцию билетов в файл";
    }
}