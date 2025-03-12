package utils;

import cli.Terminal;
import java.util.function.Function;

public abstract class AbstractPrompt<T> {
    protected final Terminal terminal;

    public AbstractPrompt(Terminal terminal) {
        this.terminal = terminal;
    }

    public abstract T ask() throws Exception;

    protected <R> R promptFor(String prompt, Function<String, R> parser, String errorMessage) throws Exception {
        while (true) {
            if (terminal.checkScanner()) {
                terminal.print(prompt + ": ");
            }
            String line = terminal.readln().trim();
            if (line.equalsIgnoreCase("exit")) throw new Exception("User exited input");
            if (!line.isEmpty()) {
                try {
                    return parser.apply(line);
                } catch (NumberFormatException e) {
                    terminal.printError(errorMessage);
                }
            }
        }
    }
}
