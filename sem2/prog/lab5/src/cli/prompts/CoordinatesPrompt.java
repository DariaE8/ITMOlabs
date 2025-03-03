package cli.prompts;

import cli.Terminal;
import models.Coordinates;
import utils.AbstractPrompt;

public class CoordinatesPrompt extends AbstractPrompt<Coordinates> {

    public CoordinatesPrompt(Terminal terminal) {
        super(terminal);
    }

    @Override
    public Coordinates ask() throws Exception {
        try {
            float x = promptFor("coordinates.x", Float::parseFloat, "Ошибка: введите число с плавающей запятой.");
            long y = promptFor("coordinates.y", Long::parseLong, "Ошибка: введите целое число.");
            return new Coordinates(x, y);
        } catch (Exception e) {
            terminal.printError("Ошибка ввода: " + e.getMessage());
            return null;
        }
    }
}
