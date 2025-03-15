package cli.commands;
import models.Command;

public class PrintAscending extends Command {
    public PrintAscending() {
        super("print_ascending", "вывести элементы коллекции в порядке возрастания");
    }
    

    //print_ascending : вывести элементы коллекции в порядке возрастания
    @Override
    public void run(String[] args) {
        show();
    }

    private void show() {
        terminal.println(tm.getValues());
    }
}
