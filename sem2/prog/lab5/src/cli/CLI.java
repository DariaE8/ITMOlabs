package cli;

import cli.prompts.TicketPrompt;
import models.Ticket;
import models.TicketManager;

public class CLI {
    Terminal terminal = new Terminal();
    TicketManager manager = new TicketManager();

    public void run() {
        while (true) {
            terminal.print("Введите команду: ");
            String input = terminal.readln().trim();
            if (input.isEmpty()) continue;
            
            String[] parts = input.split(" ", 2);
            String command = parts[0];
            String arg = parts.length > 1 ? parts[1] : "";
            
            switch (command) {
                case "help": printHelp(); break;
                case "info": printInfo(); break;
                case "insert": insertElement(terminal, arg); break;
                case "show": show(); break;
                case "show_all": showAll(); break;
                case "clear": clear(); break;
                case "save": save(); break;
                case "remove_greater_key": removeGreaterKey(arg); break; 
                case "exit": return;
                default: System.out.println("Неизвестная команда");
            }
        }
    }

    private void printHelp() {
        terminal.println("Доступные команды: help, info, show, exit, history...");
    }

    private void printInfo() {
        // terminal.println("Тип коллекции: TreeMap, Размер: " + tickets.size());
        terminal.println("Тип коллекции: TreeMap, Размер: 5" );
    }

    private void insertElement(Terminal terminal, String id) {
        TicketPrompt tp = new TicketPrompt(terminal);
        int idx = Integer.parseInt(id);
        try{
            Ticket ticket = tp.ask();
            manager.insert(idx, ticket);
        } catch (Exception e) {
            terminal.printError("Ошибка ввода: " + e.getMessage());
        }
    }

    private void show() {
        terminal.println(manager.getValues());
    }

    private void showAll() {
        terminal.println(manager.getKeyValues());
    }

    private void clear() {
        manager.clear();
    }

    private void save() {
        //dump to csv
    }

    private void removeGreaterKey(String key) {
        manager.removeGreaterKey(Integer.parseInt(key));
    }

    // private void removeGreaterKey1(String key) {
    //     manager.removeGreaterKey1(Integer.parseInt(key));
    // }
}
