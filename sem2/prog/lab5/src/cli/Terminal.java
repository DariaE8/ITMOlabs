package cli;

import java.util.NoSuchElementException;
import java.lang.IllegalStateException;
import java.util.Scanner;

import utils.Console;

/**
 * Для ввода команд и вывода результата
 */
public class Terminal implements Console {
    private static final String P = "> ";
    private static Scanner fileScanner = null;
    private static Scanner defScanner = new Scanner(System.in);

    /**
     * Выводит obj.toString() в консоль
     * @param obj Объект для печати
     */
    public void print(Object obj) {
        System.out.print(obj);
    }

    /**
     * Выводит obj.toString() + \n в консоль
     * @param obj Объект для печати
     */
    public void println(Object obj) {
        System.out.println(obj);
    }

    /**
     * Выводит ошибка: obj.toString() в консоль
     * @param obj Ошибка для печати
     */
    public void printError(Object obj) {
        System.err.println("Error: " + obj);
    }

    public String readln() throws NoSuchElementException, IllegalStateException {
        return (fileScanner!=null?fileScanner:defScanner).nextLine();
    }

    public boolean isCanReadln() throws IllegalStateException {
        return (fileScanner!=null?fileScanner:defScanner).hasNextLine();
    }

    public void printKV(Object key, Object value) {
        System.out.printf(" %-35s%-1s%n", key, value);
    }
    /**
     * Выводит prompt текущей консоли
     */
    public void prompt() {
        print(P);
    }

    /**
     * @return prompt текущей консоли
     */
    public String getPrompt() {
        return P;
    }

    public void selectFileScanner(Scanner scanner) {
        this.fileScanner = scanner;
    }

    public void selectConsoleScanner() {
        this.fileScanner = null;
    }
} 