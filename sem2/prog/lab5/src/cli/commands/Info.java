package cli.commands;
import models.Command;

public class Info extends Command {
    public Info() {
        super("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
    }
    
    
    @Override
    public void run(String[] args) {
        terminal.println("Тип коллекции: " + tm.getType() + ", Размер: " + tm.getSize());
    }
} 