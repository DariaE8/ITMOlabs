package models;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Менеджер для работы с коллекцией билетов.
 * Обеспечивает хранение, обработку и управление коллекцией билетов.
 */
public class TicketManager {
    private final TreeMap<Integer, Ticket> tickets = new TreeMap<>();

    /**
     * Создает менеджер с тестовыми данными (5 билетов).
     */
    public TicketManager() {
        initializeTestData();
    }

    /**
     * Создает менеджер из CSV данных.
     *
     * @param dump список строк в CSV формате
     * @throws IllegalArgumentException если dump равен null
     */
    public TicketManager(List<String> dump) {
        Objects.requireNonNull(dump, "CSV данные не могут быть null");
        loadFromCSV(dump);
    }

    private void initializeTestData() {
        for (int i = 1; i <= 5; i++) {
            insert(i, new Ticket());
        }
    }

    private void loadFromCSV(List<String> dump) {
        for (int i = 1; i < dump.size(); i++) {
            String line = dump.get(i);
            if (line != null && !line.trim().isEmpty()) {
                insert(i, Ticket.fromCSV(line));
            }
        }
    }

    /**
     * Возвращает количество билетов в коллекции.
     *
     * @return количество билетов
     */
    public int size() {
        return tickets.size();
    }

    /**
     * Возвращает тип коллекции.
     *
     * @return класс коллекции
     */
    public Class<?> getCollectionType() {
        return tickets.getClass();
    }

    /**
     * Добавляет билет в коллекцию.
     *
     * @param id идентификатор билета
     * @param ticket билет для добавления
     * @throws IllegalArgumentException если ticket равен null
     */
    public void insert(int id, Ticket ticket) {
        tickets.put(id, Objects.requireNonNull(ticket, "Билет не может быть null"));
    }

    /**
     * Удаляет билет по идентификатору.
     *
     * @param id идентификатор билета
     */
    public void remove(int id) {
        tickets.remove(id);
    }

    /**
     * Обновляет билет по указанному ID.
     *
     * @param id ID билета
     * @param ticket новые данные билета
     * @return true если билет был обновлен, false если билет не найден
     * @throws IllegalArgumentException если ticket равен null
     */
    public boolean update(int id, Ticket ticket) {
        Objects.requireNonNull(ticket, "Билет не может быть null");
        if (!tickets.containsKey(id)) {
            return false;
        }
        tickets.put(id, ticket);
        return true;
    }

    /**
     * Очищает коллекцию билетов.
     */
    public void clear() {
        tickets.clear();
    }

    /**
     * Преобразует коллекцию в CSV формат.
     *
     * @return список строк в CSV формате
     */
    public List<String> toCSV() {
        List<String> result = new ArrayList<>();
        result.add(Ticket.getCSVHeader());
        tickets.values().stream()
            .map(Ticket::toCSV)
            .forEach(result::add);
        return result;
    }

    /**
     * Возвращает все билеты в коллекции.
     *
     * @return коллекция билетов
     */
    public Collection<Ticket> getAllTickets() {
        return Collections.unmodifiableCollection(tickets.values());
    }

    /**
     * Возвращает строковое представление пар ключ-значение.
     *
     * @return строковое представление коллекции
     */
    public String getKeyValueString() {
        return tickets.entrySet().toString();
    }

    /**
     * Удаляет билеты, превышающие заданный.
     *
     * @param ticket билет для сравнения
     * @throws IllegalArgumentException если ticket равен null
     */
    public int removeGreater(Ticket referenceTicket) {
        Objects.requireNonNull(referenceTicket, "Билет для сравнения не может быть null");
        
        int initialSize = tickets.size();
        tickets.values().removeIf(ticket -> ticket.compareTo(referenceTicket) > 0);
        return initialSize - tickets.size();
    }
    /**
     * Удаляет билеты с ключами больше заданного.
     *
     * @param key ключ для сравнения
     */
    public void removeGreaterKeys(int key) {
        tickets.tailMap(key, false).clear();
    }

    /**
     * Подсчитывает билеты с указанным местом проведения.
     *
     * @param venue место проведения
     * @return количество совпадений
     * @throws IllegalArgumentException если venue равен null
     */
    public long countByVenue(Venue venue) {
        return tickets.values().stream()
            .filter(t -> t.getVenue().equals(Objects.requireNonNull(venue, "Место проведения не может быть null")))
            .count();
    }

    /**
     * Фильтрует билеты по началу имени.
     *
     * @param prefix префикс имени
     * @return список подходящих билетов
     * @throws IllegalArgumentException если prefix равен null или пуст
     */
    public Collection<Ticket> filterStartsWithName(String prefix) {
        Objects.requireNonNull(prefix, "Префикс не может быть null");
        if (prefix.isEmpty()) {
            throw new IllegalArgumentException("Префикс не может быть пустым");
        }

        return tickets.values().stream()
            .filter(t -> t.getName().startsWith(prefix))
            .collect(Collectors.toList());
    }

    /**
     * Возвращает множество пар ключ-значение.
     *
     * @return множество записей
     */
    public Set<Map.Entry<Integer, Ticket>> entrySet() {
        return Collections.unmodifiableSet(tickets.entrySet());
    }
}