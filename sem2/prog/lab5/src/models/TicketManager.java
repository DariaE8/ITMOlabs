package models;

import java.util.TreeMap;
import java.util.Collection;
import java.util.SortedMap;

public class TicketManager {
    TreeMap<Integer, Ticket> storage = new TreeMap<>();

    public TicketManager() {
        insert(1, new Ticket());
        insert(2, new Ticket());
        insert(3, new Ticket());
        insert(4, new Ticket());
        insert(5, new Ticket());
    }

    public void insert(int id, Ticket ticket) {
        storage.put(id, ticket);
    }

    public void remove(int id) {
        storage.remove(id);
    }

    public void update(int id, Ticket ticket) {
        storage.replace(id, ticket);
    }

    public void clear() {
        storage.clear();
    }

    public void dump() {
        //dumps to csv file
    }

    public Collection<Ticket> getValues() {
        //вывести в стандартный поток вывода все элементы коллекции в строковом представлении
        return storage.values();
    }

    public String getKeyValues() {
        return storage.entrySet().toString();
    }

    public void removeGreater(Ticket ticket) {
        // удалить из коллекции все элементы, превышающие заданный
        storage.entrySet().removeIf(entry -> entry.getValue().compareTo(ticket) > 0);
    }

    // public void removeGreaterKey(int key) {
    //     //удалить из коллекции все элементы, ключ которых превышает заданный
    //     SortedMap<Integer, Ticket> greater_elements = storage.tailMap(key);
    //     for(Integer k : greater_elements.keySet()) {
    //         remove(k);
    //     }
    // }

    public void removeGreaterKey(int key) {
        //удалить из коллекции все элементы, ключ которых превышает заданный
        storage.tailMap(key, false).clear();
    }

    public long countByVenue(Venue venue) {
        //вывести количество элементов, значение поля venue которых равно заданному
        return storage.values().stream().filter(ticket -> ticket.getVenue().equals(venue)).count();
    }

    public Collection<Ticket> filterStartsWithame(String substring) {
        //вывести элементы, значение поля name которых начинается с заданной подстроки
        return storage.values().stream().filter(ticket -> ticket.getName().startsWith(substring)).toList();
    }

    
}
