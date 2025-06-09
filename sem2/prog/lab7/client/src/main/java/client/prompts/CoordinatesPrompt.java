package client.prompts;

import utils.Terminal;
import models.Coordinates;
import utils.AbstractPrompt;

public class CoordinatesPrompt extends AbstractPrompt<Coordinates> {

    public CoordinatesPrompt(Terminal terminal) {
        super(terminal);
    }

    @Override
    public Coordinates ask() throws InputCancelledException {
        try {

            float x = promptFor("coordinates.x", input -> {
                float value = Float.parseFloat(input);
                if (value > 180 || value < -180) {
                    throw new IllegalArgumentException("Широта должна быть в диапазоне [-180..180]");
                }
                return value;
            },
            "Ошибка при вводе x: введите число с плавающей запятой.");
            long y = promptFor("coordinates.y", input -> {
                long value = Long.parseLong(input);
                if (value > 90 || value < -90) {
                    throw new IllegalArgumentException("Долгота должна быть в диапазоне [-90..90]");
                }
                return value;
            },
            "Ошибка при вводе x: введите число с плавающей запятой.");
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

