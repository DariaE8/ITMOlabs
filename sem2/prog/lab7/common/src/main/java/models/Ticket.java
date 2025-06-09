package models;

import utils.Element;
import utils.ConvertibleToCSV;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Класс, представляющий билет.
 * Содержит информацию о билете и методы для работы с ним.
 */
public class Ticket extends Element implements Serializable, ConvertibleToCSV {
    private static long idCounter = 1;
    private long id; // > 0, уникальное
    private final String name; // не null, не пустое
    private final Coordinates coordinates; // не null
    private final LocalDate creationDate; // не null, генерируется автоматически
    private final Integer price; // может быть null, > 0
    private final Double discount; // не null, > 0, <= 100
    private final Boolean refundable; // может быть null
    private final TicketType type; // не null
    private Venue venue; // не null
    private int owner_id;

    /**
     * Основной конструктор билета.
     */
    public Ticket(long id, String name, Coordinates coordinates, LocalDate creationDate,
                 Integer price, Double discount, Boolean refundable, 
                 TicketType type, Venue venue, int owner_id) {
        validateId(id);
        this.id = id;
        this.name = validateName(name);
        this.coordinates = validateCoordinates(coordinates);
        this.creationDate = validateCreationDate(creationDate);
        this.price = validatePrice(price);
        this.discount = validateDiscount(discount);
        this.refundable = refundable;
        this.type = validateType(type);
        this.venue = validateVenue(venue);
        this.owner_id = owner_id;
    }

    /**
     * Конструктор для создания нового билета (автоматически генерирует id и дату создания).
     */
    public Ticket(String name, Coordinates coordinates, Integer price,
                Double discount, Boolean refundable, TicketType type, Venue venue, int owner_id) {
        this(idCounter++, name, coordinates, LocalDate.now(), price, discount, refundable, type, venue, owner_id);
    }

    /**
     * Конструктор по умолчанию (с тестовыми данными).
     */
    public Ticket() {
        this("Default Ticket", new Coordinates(0, 0), 100, 10.0, true, TicketType.VIP, 
            new Venue(1, "Default Venue", 100, VenueType.BAR), 0);
    }

    // Валидаторы полей
    private void validateId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID должен быть больше 0");
        }
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название не может быть пустым");
        }
        return name.trim();
    }

    private Coordinates validateCoordinates(Coordinates coordinates) {
        return Objects.requireNonNull(coordinates, "Координаты не могут быть null");
    }

    private LocalDate validateCreationDate(LocalDate date) {
        return Objects.requireNonNull(date, "Дата создания не может быть null");
    }

    private Integer validatePrice(Integer price) {
        if (price != null && price <= 0) {
            throw new IllegalArgumentException("Цена должна быть больше 0");
        }
        return price;
    }

    private Double validateDiscount(Double discount) {
        Objects.requireNonNull(discount, "Скидка не может быть null");
        if (discount <= 0 || discount > 100) {
            throw new IllegalArgumentException("Скидка должна быть в диапазоне (0, 100]");
        }
        return discount;
    }

    private TicketType validateType(TicketType type) {
        return Objects.requireNonNull(type, "Тип билета не может быть null");
    }

    private Venue validateVenue(Venue venue) {
        return Objects.requireNonNull(venue, "Место проведения не может быть null");
    }

    /**
     * Создает билет из аргументов командной строки.
     */
    public static Ticket fromArgs(String[] args) {
        if (args == null || args.length < 8) {
            throw new IllegalArgumentException("Недостаточно аргументов для создания билета");
        }

        try {
            String name = args[0];
            Coordinates coordinates = new Coordinates(
                Float.parseFloat(args[1]), 
                Long.parseLong(args[2])
            );
            Integer price = args[3].equals("null") ? null : Integer.parseInt(args[3]);
            Double discount = Double.parseDouble(args[4]);
            Boolean refundable = args[5].equals("null") ? null : Boolean.parseBoolean(args[5]);
            TicketType type = TicketType.valueOf(args[6]);
            Venue venue = new Venue(
                Long.parseLong(args[7]), 
                args[8], 
                Integer.parseInt(args[9]), 
                VenueType.valueOf(args[10])
            );

            return new Ticket(name, coordinates, price, discount, refundable, type, venue, 0);
        } catch (Exception e) {
            throw new IllegalArgumentException("Некорректные аргументы для создания билета", e);
        }
    }
    
    /**
     * Преобразует билет в CSV строку.
     */
    @Override
    public String toCSV() {
        return String.join(",",
            String.valueOf(id),
            name,
            coordinates.toCSV(),
            creationDate.toString(),
            price == null ? "null" : String.valueOf(price),
            String.valueOf(discount),
            refundable == null ? "null" : String.valueOf(refundable),
            type.name(),
            venue.toCSV()
        );
    }

    /**
     * Создает билет из CSV строки.
     */
    public static Ticket fromCSV(String csv) {
        try {
            String[] fields = csv.split(",");
            long id = Long.parseLong(fields[0]);
            updateIdCounter(id);
            
            return new Ticket(
                id,
                fields[1].replaceAll("\"", ""),
                new Coordinates(
                    Float.parseFloat(fields[2]), 
                    Long.parseLong(fields[3])
                ),
                LocalDate.parse(fields[4]),
                fields[5].equals("null") ? null : Integer.parseInt(fields[5]),
                Double.parseDouble(fields[6]),
                fields[7].equals("null") ? null : Boolean.parseBoolean(fields[7]),
                TicketType.valueOf(fields[8].replaceAll("\"", "")),
                new Venue(
                    Long.parseLong(fields[9]), 
                    fields[10].replaceAll("\"", ""), 
                    Integer.parseInt(fields[11]), 
                    VenueType.valueOf(fields[12])
                ),0
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка парсинга CSV строки", e);
        }
    }

    private static void updateIdCounter(long id) {
        if (id >= idCounter) {
            idCounter = id + 1;
        }
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setOwnerId(int owner_id) {
        this.owner_id = owner_id;
    }

    public void updateId() {
        this.id = idCounter;
        this.venue.updateId();
        idCounter++;
    }

    public static void resetIdCounter() {
        idCounter = 1;
        Venue.resetIdCounter();
    }

    /**
     * Возвращает заголовок CSV файла.
     */
    public static String getCSVHeader() {
        List<String> fields = new ArrayList<>();
        fields.add("id");
        fields.add("name");
        fields.addAll(Arrays.asList(Coordinates.getCSVHeader())); // Исправлено здесь
        fields.add("creationDate");
        fields.add("price");
        fields.add("discount");
        fields.add("refundable");
        fields.add("type");
        fields.addAll(Arrays.asList(Venue.getCSVHeader())); // И здесь
        
        return String.join(",", fields);
    }

    @Override
    public String toString() {
        return String.format(
            "Ticket{id=%d, name='%s', coordinates=%s, creationDate=%s, price=%s, " +
            "discount=%.2f, refundable=%s, type=%s, venue=%s}",
            id, name, coordinates, creationDate, price, 
            discount, refundable, type, venue
        );
    }

    @Override
    public boolean validate() {
        return id > 0 &&
               name != null && !name.isEmpty() &&
               coordinates != null && coordinates.validate() &&
               creationDate != null &&
               (price == null || price > 0) &&
               discount != null && discount > 0 && discount <= 100 &&
               type != null &&
               venue != null;
    }
    
    @Override
    public int compareTo(Element element) {
        return Long.compare(this.id, element.getId());
    }

    // Геттеры
    @Override
    public long getId() { return id; }
    public String getName() { return name; }
    public Coordinates getCoordinates() { return coordinates; }
    public LocalDate getCreationDate() { return creationDate; }
    public Integer getPrice() { return price; }
    public Double getDiscount() { return discount; }
    public Boolean getRefundable() { return refundable; }
    public TicketType getType() { return type; }
    public Venue getVenue() { return venue; }
    public int getOwnerId() {return owner_id; }
}