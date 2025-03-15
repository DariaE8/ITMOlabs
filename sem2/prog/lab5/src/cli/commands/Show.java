package cli.commands;

import models.Command;

public class Show extends Command {
    public Show() {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении"); 
}

@Override
public void run(String[] args) {
        terminal.println(tm.getValues());
    }
}