package cli;

import cli.prompts.TicketPrompt;
import cli.prompts.VenuePrompt;
import models.Ticket;
import models.Venue;
import models.TicketManager;

import java.io.File;
// import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;


//Command line interface
public class CLI {
    Terminal terminal = new Terminal();
    TicketManager tm;
    DumpManager dm;
    private boolean running = true; 

    private final Queue<String> commandHistory = new LinkedList<>();
    private static final int HISTORY_SIZE = 5;

    private final Set<String> executingScripts = new HashSet<>();
    private final int MAX_RECURSION_DEPTH = 10;
    private int currentRecursionDepth = 0;

    public boolean init(String[] args) {
        if (args.length == 0) {
            terminal.println("Введите имя загружаемого файла как аргумент командной строки\n");
            return false;
        }

        if (!checkFileExist(args[0])) {
            terminal.println(args);
            return false;
        }
    
        try {
            this.dm = new DumpManager(args[0]);
            // this.dm = new DumpManager();
            this.tm = new TicketManager(dm.load());
            return true;
        } catch (Exception e) {
            System.err.println("Error loading TicketManager: " + e.getMessage());
            return false;
        }
    }

    public void run() {
        while (running) {
            terminal.print("Введите команду: ");
            String input = terminal.readln().trim();
            if (input.isEmpty()) continue;
            
            String[] parts = input.split(" ", 2);
            String command = parts[0];
            String arg = parts.length > 1 ? parts[1] : "";
            addToHistory(command);
            processCommand(command, arg);
        }
    }

    private boolean checkFileExist(String fname) {
        File file = new File(fname);
        if (!file.exists() || !file.isFile()) {
            terminal.printError("Ошибка: файл " + fname + " не найден.");
            return false;
        }
        return true;

    }

    public void processCommand(String command, String arg) {
        switch (command) {
            case "help": printHelp(); break;
            case "info": printInfo(); break;
            case "insert": insertElement(terminal, arg); break;
            case "show": show(); break;
            case "show_table": showTable(); break;
            case "clear": clear(); break;
            case "save": save(); break;
            case "execute_script": executeScript(arg); break;
            case "load": load(); break;
            case "history": printHistory(); break;
            case "remove_greater_key": removeGreaterKey(arg); break;
            case "count_by_venue": countByVenue(); break;
            case "filter_starts_with_name": filterStartsWithame(arg); break;
            case "print_ascending": printAsc(); break;
            case "exit": 
                running = false;  // Stop the loop in run()
                terminal.println("Выход из программы...");
                break;
            default: terminal.printError("Неизвестная команда: " + command);
        }
    }

    private void printHelp() {
        terminal.println("""
            help : вывести справку по доступным командам
            info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
            show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении
            insert null {element} : добавить новый элемент с заданным ключом
            update id {element} : обновить значение элемента коллекции, id которого равен заданному
            remove_key null : удалить элемент из коллекции по его ключу
            clear : очистить коллекцию
            save : сохранить коллекцию в файл
            execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
            exit : завершить программу (без сохранения в файл)
            remove_greater {element} : удалить из коллекции все элементы, превышающие заданный
            history : вывести последние 5 команд (без их аргументов)
            remove_greater_key null : удалить из коллекции все элементы, ключ которых превышает заданный
            count_by_venue venue : вывести количество элементов, значение поля venue которых равно заданному
            filter_starts_with_name name : вывести элементы, значение поля name которых начинается с заданной подстроки
            print_ascending : вывести элементы коллекции в порядке возрастания
        """);
    }

    private void printInfo() {
        terminal.println("Тип коллекции: " + tm.getType() + ", Размер: " + tm.getSize());
    }

    private void insertElement(Terminal terminal, String id) {
        TicketPrompt tp = new TicketPrompt(terminal);
        int idx = Integer.parseInt(id);
        try{
            Ticket ticket = tp.ask();
            tm.insert(idx, ticket);
        } catch (Exception e) {
            terminal.printError("Ошибка ввода: " + e.getMessage());
        }
    }

    private void show() {
        terminal.println(tm.getValues());
    }

    private void showTable() {
        terminal.printTable(tm.dumpCSV());
    }

    private void clear() {
        tm.clear();
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
        dm.save(tm.dumpCSV());
    }

    private void load(){
        tm = new TicketManager(dm.load());
    }

    //удалить из коллекции все элементы, ключ которых превышает заданный
    private void removeGreaterKey(String key) {
        tm.removeGreaterKey(Integer.parseInt(key));
    }

    //вывести количество элементов, значение поля venue которых равно заданному
    private void countByVenue() {
        VenuePrompt vp = new VenuePrompt(terminal);
        try{
            Venue venue = vp.ask();
            tm.countByVenue(venue);
        } catch (Exception e) {
            terminal.printError("Ошибка ввода: " + e.getMessage());
        }
    }

    //вывести элементы, значение поля name которых начинается с заданной подстроки
    private void filterStartsWithame(String name) {
        terminal.println(tm.filterStartsWithame(name));
    }

    //print_ascending : вывести элементы коллекции в порядке возрастания
    private void printAsc() {
        show();
    }

    public Terminal getTerminal() {
        return terminal;
    }
    

    private void executeScript(String filename) {
        if (currentRecursionDepth >= MAX_RECURSION_DEPTH) {
            terminal.printError("Ошибка: превышена максимальная глубина рекурсии (" + MAX_RECURSION_DEPTH + ").");
            return;
        }

        if (!checkFileExist(filename)){
            return;
        };
 
        if (executingScripts.contains(filename)) {
            terminal.printError("Ошибка: обнаружена рекурсия! Скрипт " + filename + " уже выполняется.");
            return;
        }

        executingScripts.add(filename);  
        currentRecursionDepth++;

        try (Scanner fileScanner = new Scanner(new File(filename))) {
            terminal.selectFileScanner(fileScanner);  

            while (terminal.isCanReadln()) {
                String command = terminal.readln().trim();
                if (!command.isEmpty()) {
                    String[] parts = command.split(" ", 2);
                    String cmd = parts[0];
                    String arg = parts.length > 1 ? parts[1] : "";

                    if ("execute_script".equals(cmd)) {
                        executeScript(arg);  
                    } else {
                        addToHistory(cmd);
                        processCommand(cmd, arg);
                    }
                }
            }
            terminal.selectConsoleScanner();
        } catch (Exception e) {
            terminal.printError("Ошибка: не удалось открыть файл " + filename);
        } finally {
            executingScripts.remove(filename);  
            currentRecursionDepth--;
        }
    }
}
