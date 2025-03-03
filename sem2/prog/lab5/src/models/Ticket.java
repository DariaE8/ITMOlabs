package models;

import utils.Element;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Ticket extends Element implements Serializable{
    private static long idCounter = 1;
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer price; //Поле может быть null, Значение поля должно быть больше 0
    private Double discount; //Поле не может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 100
    private Boolean refundable; //Поле может быть null
    private TicketType type; //Поле не может быть null
    private Venue venue; //Поле не может быть null}

    public Ticket(String name, Coordinates coordinates, Integer price, Double discount, Boolean refundable, TicketType type, Venue venue) {
        this.id = idCounter++;
        this.name = Objects.requireNonNull(name);
        this.coordinates = Objects.requireNonNull(coordinates);
        this.creationDate = LocalDate.now();
        this.price = price;
        this.discount = Objects.requireNonNull(discount);
        this.refundable = refundable;
        this.type = Objects.requireNonNull(type);
        this.venue = Objects.requireNonNull(venue);
    }

    // public Ticket(String name, Coordinates coordinates, Integer price, Double discount, Boolean refundable, TicketType type, Venue venue) {
    //     this(name, coordinates, LocalDate.now(), price, discount, refundable, type, venue );
    // }

    public Ticket() {
        this("John", new Coordinates(0, 0), 100, 10.0, true, TicketType.VIP, new Venue(1, "Venue", 100, VenueType.BAR));
    }


    @Override
    public String toString() {
        return "ticket{\"id\": " + id + ", " +
                "\"name\": \"" + name + "\", " +
                "\"creationDate\": \"" + creationDate + "\", " +
                "\"coordinates\": \"" + coordinates + "\", " +
                "\"price\": " + (price == null ? "null" : price) + ", " +
                "\"discount\": " + (discount > 100 ? "100 (max limit exceeded)" : discount) + ", " +
                "\"refundable\": " + (refundable == null ? "null" : "\"" + refundable + "\"") + ", " +
                "\"type\": \"" + type + "\", " +
                "\"venue\": \"" + venue + "\"}";
    }

    @Override
    public boolean validate() {
        if (id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (creationDate == null) return false;
        if (coordinates == null || !coordinates.validate()) return false;
        return true;
    }

    @Override
    public long getId() {
        return id;
    }
    
    @Override
    public int compareTo(Element element) {
        return Long.compare(this.id, element.getId()); // ✅ Correct
    }
}


