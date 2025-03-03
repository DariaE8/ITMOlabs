package cli;

import cli.prompts.TicketPrompt;
import models.Ticket;

public class CLI {
    Terminal terminal = new Terminal();

    public void run() {
        while (true) {
            terminal.print("Введите команду: ");
            String input = terminal.readln().trim();
            if (input.isEmpty()) continue;
            
            String[] parts = input.split(" ", 2);
            String command = parts[0];
            String argument = parts.length > 1 ? parts[1] : "";
            
            switch (command) {
                case "help": printHelp(); break;
                case "info": printInfo(); break;
                case "insert": insertElement(terminal); break;
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

    private void insertElement(Terminal terminal) {
        TicketPrompt tp = new TicketPrompt(terminal);
        try{
            Ticket ticket = tp.ask();
            terminal.println(ticket);
        } catch (Exception e) {
            terminal.printError("Ошибка ввода: " + e.getMessage());
        }
    }
}
