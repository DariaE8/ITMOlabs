package cli.commands;

import models.Command;

public class FilterStartsWithNameName extends Command {
    public FilterStartsWithNameName() {
        super("filter_by_type", "фильтрует объекты по началу имени");
    }

    @Override
    public void run(String[] args) {
        System.out.println("Фильтрация не реализована!");
    }
}
