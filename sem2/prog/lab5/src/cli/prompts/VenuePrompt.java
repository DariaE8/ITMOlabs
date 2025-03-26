package cli.prompts;

import cli.Terminal;
import models.Venue;
import models.VenueType;
import utils.AbstractPrompt;

public class VenuePrompt extends AbstractPrompt<Venue> {

    public VenuePrompt(Terminal terminal) {
        super(terminal);
    }

    @Override
    public Venue ask() throws InputCancelledException{
        try {
            long id = promptFor("venue.id", Long::parseLong, "Ошибка: введите целое число.");
            String name = promptFor("venue.name", input -> {
                if (input.isEmpty()) throw new IllegalArgumentException();
                return input;
            }, "Ошибка: имя не может быть пустым.");
            int capacity = promptFor("venue.capacity", Integer::parseInt, "Ошибка: введите целое число больше 0.");
            VenueType type = promptFor("venue.type(BAR, THEATRE, MALL)", VenueType::valueOf, "Ошибка: введите корректный тип Venue.");

            return new Venue(id, name, capacity, type);
        } catch (NumberFormatException e) {
            terminal.printError("Некорректный формат числа. Пожалуйста, попробуйте снова.");
            return null;
        } catch (IllegalArgumentException e) {
            terminal.printError("Ошибка в данных: " + e.getMessage());
            return null;
        } catch (Exception e) {
            terminal.printError("Произошла непредвиденная ошибка: " + e.getMessage());
            return null; // Выход из метода при критической ошибке
        }
    }
}
