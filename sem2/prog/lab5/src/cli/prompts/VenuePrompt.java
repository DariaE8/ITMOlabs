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
    public Venue ask() throws InputCancelledException {
        try {
            // long id = promptFor("venue.id", Long::parseLong, "Ошибка: введите целое число.");
            String name = promptFor("venue.name", input -> {
                if (input.isEmpty()) throw new IllegalArgumentException();
                return input;
            }, "Ошибка: имя не может быть пустым.");
            int capacity = promptFor("venue.capacity", input -> {
                int value = Integer.parseInt(input);
                if (value <= 0) {
                    throw new IllegalArgumentException("Вместимость должна быть больше 0");
                }
                return value;
            }, "Вместимость должна быть больше 0");
            VenueType type = promptFor("venue.type (BAR, THEATRE, MALL)", input -> {
                try {
                    return VenueType.valueOf(input.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Допустимые значения: BAR, THEATRE, MALL");
                }
            }, "Ошибка: введите корректный тип Venue.");
            return new Venue(name, capacity, type);
        } catch (NumberFormatException e) {
            terminal.printError("Некорректный формат числа. Пожалуйста, попробуйте снова.");
            return null;
        } catch (IllegalArgumentException e) {
            terminal.printError("Ошибка в данных: " + e.getMessage());
            return null;
        } catch (Exception e) {
            if (terminal.checkScanner()) {
                terminal.printError("Произошла непредвиденная ошибка: " + e.getMessage());
                return null;
            } else {                
                throw new InputCancelledException(e.getMessage());
            }
        }
    }
}
