package models;

import utils.Validatable;
import utils.ConvertibleToCSV;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс, представляющий место проведения мероприятия.
 * Содержит информацию о месте и методы для работы с ним.
 */
public class Venue implements Serializable, Validatable, ConvertibleToCSV {
    private static long idCounter = 1;

    private long id; // > 0, уникальное
    private final String name; // не null, не пустое
    private final int capacity; // > 0
    private final VenueType type; // не null

    /**
     * Создает новое место проведения.
     *
     * @param id идентификатор (должен быть > 0)
     * @param name название (не может быть null или пустым)
     * @param capacity вместимость (должна быть > 0)
     * @param type тип места (не может быть null)
     * @throws IllegalArgumentException если параметры невалидны
     */
    public Venue(long id, String name, int capacity, VenueType type) {
        this.id = validateId(id);
        this.name = validateName(name);
        this.capacity = validateCapacity(capacity);
        this.type = validateType(type);
        
        updateIdCounter(id);
    }

    /**
     * Конструктор для создания нового места (автоматически генерирует id и дату создания).
    */
    public Venue(String name, int capacity, VenueType type) {
        this(idCounter++, name, capacity, type);
    }

    // Валидаторы полей
    private long validateId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID места должен быть больше 0");
        }
        return id;
    }

    public void updateId() {
        this.id = idCounter;
        idCounter++;
    }

    public static void resetIdCounter() {
        idCounter = 1;
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название места не может быть пустым");
        }
        return name.trim();
    }

    private int validateCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Вместимость должна быть больше 0");
        }
        return capacity;
    }

    private VenueType validateType(VenueType type) {
        return Objects.requireNonNull(type, "Тип места не может быть null");
    }

    private static void updateIdCounter(long id) {
        if (id >= idCounter) {
            idCounter = id + 1;
        }
    }

    /**
     * Создает место проведения из CSV строки.
     *
     * @param csv строка в формате "id,name,capacity,type"
     * @return объект Venue
     * @throws IllegalArgumentException если строка невалидна
     */
    public static Venue fromCSV(String csv) {
        Objects.requireNonNull(csv, "CSV строка не может быть null");
        String[] parts = csv.split(",");
        
        if (parts.length != 4) {
            throw new IllegalArgumentException("Неверный формат CSV строки");
        }

        try {
            long id = Long.parseLong(parts[0]);
            String name = parts[1].replaceAll("\"", "");
            int capacity = Integer.parseInt(parts[2]);
            VenueType type = VenueType.valueOf(parts[3].trim());
            
            return new Venue(id, name, capacity, type);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка парсинга CSV строки", e);
        }
    }
    

    /**
     * Преобразует место проведения в CSV строку.
     *
     * @return строка в формате "id,name,capacity,type"
     */
    @Override
    public String toCSV() {
        return String.join(",",
            String.valueOf(id),
            "\"" + name + "\"",
            String.valueOf(capacity),
            type.name()
        );
    }

    /**
     * Возвращает строковое представление места.
     */
    @Override
    public String toString() {
        return String.format(
            "Venue{id=%d, name='%s', capacity=%d, type=%s}",
            id, name, capacity, type
        );
    }

    /**
     * Проверяет валидность объекта.
     *
     * @return true если объект валиден
     */
    @Override
    public boolean validate() {
        return id > 0 &&
               name != null && !name.isEmpty() &&
               capacity > 0 &&
               type != null;
    }

    /**
     * Возвращает заголовок CSV для места проведения.
     *
     * @return массив названий полей
     */
    public static String[] getCSVHeader() {
        return new String[]{"id", "name", "capacity", "type"};
    }

    // Геттеры
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public VenueType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue venue = (Venue) o;
        return id == venue.id &&
               capacity == venue.capacity &&
               Objects.equals(name, venue.name) &&
               type == venue.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, capacity, type);
    }
}