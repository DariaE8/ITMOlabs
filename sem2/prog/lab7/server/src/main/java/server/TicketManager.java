package server;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import managers.TicketManagerInterface;
import models.Ticket;
import models.Venue;
import server.database.DatabaseManager;
import server.database.dao.TicketDao;

/**
 * Менеджер для работы с коллекцией билетов.
 * Обеспечивает хранение, обработку и управление коллекцией билетов.
 */
public class TicketManager implements TicketManagerInterface{
    private final TreeMap<Integer, Ticket> tickets = new TreeMap<>();
    private final TicketDao ticketDao;

    /**
     * Создает менеджер с тестовыми данными (5 билетов).
     */
    public TicketManager() {
        ticketDao = new TicketDao(DatabaseManager.getConnection());
        loadFromDB(ticketDao.getAllTickets());
    }

    private void loadFromDB(List<Ticket> dump) {
        try {
            for (int i = 0; i < dump.size(); i++) {
                tickets.put(i, Objects.requireNonNull(dump.get(i), "Билет не может быть null"));
            }
        } catch (Exception e) {
            System.out.println("CANNOT LOAD FROM DB");
        }
    }

    /**
     * Возвращает количество билетов в коллекции.
     *
     * @return количество билетов
     */
    @Override
    public synchronized int size() {
        return tickets.size();
    }

    /**
     * Возвращает тип коллекции.
     *
     * @return класс коллекции
     */
    @Override
    public synchronized Class<?> getCollectionType() {
        return tickets.getClass();
    }

    /**
     * Добавляет билет в коллекцию.
     *
     * @param id идентификатор билета
     * @param ticket билет для добавления
     * @throws IllegalArgumentException если ticket равен null
     */
    @Override
    public synchronized void insert(int key, Ticket ticket) throws Exception{
        tickets.put(key, Objects.requireNonNull(ticket, "Билет не может быть null"));
        ticketDao.insertTicket(ticket);
    }

    /**
     * Удаляет билет по идентификатору.
     *
     * @param id идентификатор билета
     */
    @Override
    public synchronized void remove(int id, int user_id) {
        tickets.remove(id);
        ticketDao.deleteTicket(id, user_id);
    }

    /**
     * Обновляет билет по указанному ID.
     *
     * @param id ID билета
     * @param ticket новые данные билета
     * @return true если билет был обновлен, false если билет не найден
     * @throws IllegalArgumentException если ticket равен null
     */
    // @Override
    // public boolean update(long id, Ticket ticket) throws Exception{
    //     Objects.requireNonNull(ticket, "Билет не может быть null");
    //     tickets.put(id, ticket);
    //     return ticketDao.updateTicket(ticket);
    // }
    @Override
    public synchronized boolean update(long id, Ticket updatedTicket) {
    for (Map.Entry<Integer, Ticket> entry : tickets.entrySet()) {
        if (entry.getValue().getId() == id) {
            tickets.put(entry.getKey(), updatedTicket);
            return true;
        }
    }
    return false; // Не найден билет с таким id
}

    public synchronized Ticket getTicketById(long id) {
        return tickets.values().stream()
                    .filter(ticket -> ticket.getId() == id)
                    .findFirst()
                    .orElse(null);
    }

    @Override
    public synchronized void clear(int ownerId) {
        tickets.entrySet().removeIf(entry -> entry.getValue().getOwnerId() == ownerId);
        // Удалить из базы данных
        try {
            ticketDao.deleteAllByOwner(ownerId);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении билетов из базы данных: " + e.getMessage(), e);
        }
    }

    /**
     * Преобразует коллекцию в CSV формат.
     *
     * @return список строк в CSV формате
     */
    @Override
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
    @Override
    public synchronized Collection<Ticket> getAllTickets() {
        // return Collections.unmodifiableCollection(tickets.values());
        return new ArrayList<>(tickets.values());
    }

    /**
     * Возвращает строковое представление пар ключ-значение.
     *
     * @return строковое представление коллекции
     */
    public synchronized String getKeyValueString() {
        return tickets.entrySet().toString();
    }

    /**
     * Удаляет билеты, превышающие заданный.
     *
     * @param ticket билет для сравнения
     * @throws IllegalArgumentException если ticket равен null
     */
    @Override
    public synchronized int removeGreater(Ticket referenceTicket) {
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
    @Override
    public synchronized void removeGreaterKeys(int key) {
        tickets.tailMap(key, false).clear();
    }

    /**
     * Подсчитывает билеты с указанным местом проведения.
     *
     * @param venue место проведения
     * @return количество совпадений
     * @throws IllegalArgumentException если venue равен null
     */
    @Override
    public synchronized long countByVenue(Venue venue) {
        return tickets.values().stream()
            .filter(t -> t.getVenue().equals(Objects.requireNonNull(venue, "Место проведения не может быть null")))
            .count();
    }

    @Override
    public synchronized long countByOwner(int owner_id) {
        return tickets.values().stream()
            .filter(t -> t.getOwnerId() == owner_id)
            .count();
    }

    /**
     * Фильтрует билеты по началу имени.
     *
     * @param prefix префикс имени
     * @return список подходящих билетов
     * @throws IllegalArgumentException если prefix равен null или пуст
     */
    @Override
    public synchronized Collection<Ticket> filterStartsWithName(String prefix) {
        Objects.requireNonNull(prefix, "Префикс не может быть null");
        if (prefix.isEmpty()) {
            throw new IllegalArgumentException("Префикс не может быть пустым");
        }

        return tickets.values().stream()
            .filter(t -> t.getName().startsWith(prefix))
            .collect(Collectors.toList());
    }
    @Override
    public boolean checkIdExist(long id) {
        return tickets.values().stream()
                    .anyMatch(ticket -> ticket.getId() == id);
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