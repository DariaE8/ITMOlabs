package cli.commands;
import models.Command;

public class Clear extends Command {
    public Clear() {
        super("clear", "очистить коллекцию");
    }

    @Override
    public void run(String[] args){
        System.out.println("Коллекция успешно очищена\n");
    }
}