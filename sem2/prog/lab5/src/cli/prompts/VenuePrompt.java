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
    public Venue ask() throws Exception {
        try {
            long id = promptFor("venue.id", Long::parseLong, "Ошибка: введите целое число.");
            String name = promptFor("venue.name", input -> {
                if (input.isEmpty()) throw new IllegalArgumentException();
                return input;
            }, "Ошибка: имя не может быть пустым.");
            int capacity = promptFor("venue.capacity", Integer::parseInt, "Ошибка: введите целое число больше 0.");
            VenueType type = promptFor("venue.type(BAR, THEATRE, MALL)", VenueType::valueOf, "Ошибка: введите корректный тип Venue.");

            return new Venue(id, name, capacity, type);
        } catch (Exception e) {
            terminal.printError("Ошибка ввода: " + e.getMessage());
            return null;
        }
    }
}
