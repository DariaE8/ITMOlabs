package client;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.lang.IllegalStateException;
import java.util.Scanner;

import utils.Console;

/**
 * Для ввода команд и вывода результата
 */
public class Terminal implements Console {
    private static final String P = "> ";
    private Scanner fileScanner = null;
    private Scanner defScanner = new Scanner(System.in);

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

    /**
     * Выводит сообщение об успешном выполнении операции
     * @param message Сообщение для печати
     */
    public void printSuccess(String message) {
        System.out.println("Success: " + message);
    }

    /**
     * Выводит предупреждающее сообщение
     * @param message Сообщение для печати
     */
    public void printWarning(String message) {
        System.out.println("Warning: " + message);
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

    public void printTable(List<String> table) {
        
        // Split the header and data rows
        String header = table.get(0); // First line is the header
        List<String> dataRows = table.subList(1, table.size()); // Rest are data rows

        // Split the header and data into columns
        String[] headers = header.split(",");
        List<String[]> rows = new ArrayList<>();
        for (String line : dataRows) {
            rows.add(line.split(","));
        }

        // Calculate the maximum width for each column
        int[] columnWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            columnWidths[i] = headers[i].length(); // Initialize with header width
        }
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                if (row[i].length() > columnWidths[i]) {
                    columnWidths[i] = row[i].length();
                }
            }
        }

        // Print the header
        printRow(headers, columnWidths);

        // Print a separator line
        printSeparator(columnWidths);

        // Print the data rows
        for (String[] row : rows) {
            printRow(row, columnWidths);
        }
    }

    // Helper method to print a row with proper formatting
    private void printRow(String[] row, int[] columnWidths) {
        for (int i = 0; i < row.length; i++) {
            String format = "%-" + (columnWidths[i] + 2) + "s"; // Left-align with padding
            System.out.printf(format, row[i]);
        }
        System.out.println();
    }

    // Helper method to print a separator line
    private void printSeparator(int[] columnWidths) {
        for (int width : columnWidths) {
            System.out.print("-".repeat(width + 2)); // Add padding
        }
        System.out.println();
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

    public boolean checkScanner() {
        if (this.fileScanner == null) {
            return true;
        }
        return false;
    }
}
