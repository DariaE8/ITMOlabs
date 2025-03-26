package cli.prompts;

import cli.Terminal;
import models.Coordinates;
import utils.AbstractPrompt;

public class CoordinatesPrompt extends AbstractPrompt<Coordinates> {

    public CoordinatesPrompt(Terminal terminal) {
        super(terminal);
    }

    @Override
    public Coordinates ask() throws InputCancelledException {
        try {
            float x = promptFor("coordinates.x", Float::parseFloat, "Ошибка: введите число с плавающей запятой.");
            long y = promptFor("coordinates.y", Long::parseLong, "Ошибка: введите целое число.");
            return new Coordinates(x, y);
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

