package cli;

import cli.prompts.TicketPrompt;
import cli.prompts.VenuePrompt;
import models.Ticket;
import models.Venue;
import models.TicketManager;

import java.util.LinkedList;
import java.util.Queue;


//Command line interface
public class CLI {
    Terminal terminal = new Terminal();
    TicketManager manager = new TicketManager();
    DumpManager dm = new DumpManager();

    private final Queue<String> commandHistory = new LinkedList<>();
    private static final int HISTORY_SIZE = 5;

    public void run() {
        while (true) {
            terminal.print("Введите команду: ");
            String input = terminal.readln().trim();
            if (input.isEmpty()) continue;
            
            String[] parts = input.split(" ", 2);
            String command = parts[0];
            String arg = parts.length > 1 ? parts[1] : "";
            addToHistory(command);
            
            switch (command) {
                case "help": printHelp(); break;
                case "info": printInfo(); break;
                case "insert": insertElement(terminal, arg); break;
                case "show": show(); break;
                case "show_all": showAll(); break;
                case "clear": clear(); break;
                case "save": save(); break;
                case "execute_script": executeScript(arg);break;
                case "load": load(); break;
                case "history": printHistory(); break;
                case "remove_greater_key": removeGreaterKey(arg); break;
                case "count_by_venue": countByVenue();break;
                case "filter_starts_with_name": filterStartsWithame(arg); break;
                case "print_ascending": printAsc(); break;
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

    private void addToHistory(String command) {
    if (commandHistory.size() >= HISTORY_SIZE) {
            commandHistory.poll(); // Remove the oldest command if the history is full
        }
        commandHistory.offer(command); // Add the new command to the history
    }

        // Print the command history
    private void printHistory() {
        terminal.println("Последние " + HISTORY_SIZE + " команд:");
        for (String cmd : commandHistory) {
            terminal.println(cmd);
        }
    }

    private void save(){
        //dump to csv
        dm.save(manager.dumpCSV());
    }

    private void load(){
        manager = new TicketManager(dm.load());
    }

    //удалить из коллекции все элементы, ключ которых превышает заданный
    private void removeGreaterKey(String key) {
        manager.removeGreaterKey(Integer.parseInt(key));
    }

    //вывести количество элементов, значение поля venue которых равно заданному
    private void countByVenue() {
        VenuePrompt vp = new VenuePrompt(terminal);
        try{
            Venue venue = vp.ask();
            manager.countByVenue(venue);
        } catch (Exception e) {
            terminal.printError("Ошибка ввода: " + e.getMessage());
        }
    }

    //вывести элементы, значение поля name которых начинается с заданной подстроки
    private void filterStartsWithame(String name) {
        terminal.println(manager.filterStartsWithame(name));
    }

    //print_ascending : вывести элементы коллекции в порядке возрастания
    private void printAsc() {
        show();
    }

    private void executeScript(String fname) {
        
    }
}
