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
            float x = promptFor("coordinates.x", Float::parseFloat, "Ошибка при вводе x: введите число с плавающей запятой.");
            long y = promptFor("coordinates.y", Long::parseLong, "Ошибка при вводе y: введите целое число.");
            return new Coordinates(x, y);
        } catch (NumberFormatException e) {
            if (terminal.checkScanner()) {
                terminal.printError("Некорректный формат числа. Пожалуйста, попробуйте снова.");
                return null;
            } else {
                throw e;
            }
        } catch (IllegalArgumentException e) {
            if (terminal.checkScanner()) {
                terminal.printError("Ошибка в данных: " + e.getMessage());
                return null;
            } else {
                throw e;
            }
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

