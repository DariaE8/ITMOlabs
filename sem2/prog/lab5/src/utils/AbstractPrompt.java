package utils;

import cli.Terminal;
import java.util.function.Function;
import java.util.Objects;

public abstract class AbstractPrompt<T> {
    protected final Terminal terminal;
    
    public AbstractPrompt(Terminal terminal) {
        this.terminal = Objects.requireNonNull(terminal, "Terminal не может быть null");
    }

    public abstract T ask() throws InputCancelledException;

    protected <R> R promptFor(String prompt, Function<String, R> parser, String errorMessage) 
            throws InputCancelledException {
        while (true) {
            try {
                if (terminal.checkScanner()) {
                    terminal.print(prompt + ": ");
                }
                
                String line = terminal.readln().trim();
                
                if (line.equalsIgnoreCase("exit")) {
                    throw new InputCancelledException("Пользователь прервал ввод");
                }
                
                if (line.isEmpty()) {
                    terminal.printError("Ввод не может быть пустым. Пожалуйста, попробуйте снова.");
                    continue;
                }
                
                return parser.apply(line);
                
            } catch (NumberFormatException e) {
                terminal.printError(errorMessage);
            } catch (IllegalArgumentException e) {
                terminal.printError(e.getMessage());
            } catch (Exception e) {
                terminal.printError("Произошла непредвиденная ошибка: " + e.getMessage());
                throw new InputCancelledException("Ошибка ввода", e);
            }
        }
    }
    
    public static class InputCancelledException extends Exception {
        public InputCancelledException(String message) {
            super(message);
        }
        
        public InputCancelledException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}