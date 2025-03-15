package models;

import utils.Element;
import utils.ConvertibleToCSV;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.lang.reflect.Field;
import java.util.List;
import java.util.ArrayList;

public class Ticket extends Element implements Serializable, ConvertibleToCSV{
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

    public Ticket(long id, String name, Coordinates coordinates, java.time.LocalDate creationDate, Integer price, Double discount, Boolean refundable, TicketType type, Venue venue) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.coordinates = Objects.requireNonNull(coordinates);
        this.creationDate = creationDate;
        this.price = price;
        this.discount = Objects.requireNonNull(discount);
        this.refundable = refundable;
        this.type = Objects.requireNonNull(type);
        this.venue = Objects.requireNonNull(venue);
    }

    public Ticket(String name, Coordinates coordinates, Integer price, Double discount, Boolean refundable, TicketType type, Venue venue) {
        this(idCounter++, name, coordinates, LocalDate.now(), price, discount, refundable, type, venue);
    }

    public Ticket() {
        this("John", new Coordinates(0, 0), 100, 10.0, true, TicketType.VIP, new Venue(1, "Venue", 100, VenueType.BAR));
    }

    public static Ticket fromArgs(String[] args) {
        if (args == null || args.length < 8) {
            throw new IllegalArgumentException("Insufficient arguments to create Ticket.");
        }

        // Parse the arguments
        String name = args[0];
        Coordinates coordinates = new Coordinates(Float.parseFloat(args[1]), Long.parseLong(args[2]));  // Assuming coordinates are in args[1] and args[2]
        Integer price = args[3].equals("null") ? null : Integer.parseInt(args[3]);
        Double discount = Double.parseDouble(args[4]);
        Boolean refundable = args[5].equals("null") ? null : Boolean.parseBoolean(args[5]);
        TicketType type = TicketType.valueOf(args[6]);
        Venue venue = new Venue(Long.parseLong(args[7]), args[8], Integer.parseInt(args[9]), VenueType.valueOf(args[10]));

        return new Ticket(name, coordinates, price, discount, refundable, type, venue);
    }
    
    public String toCSV() {
        return id + "," + name + "," + coordinates.toCSV() + "," + creationDate + "," + (price == null ? "null" : price) + "," + (discount > 100 ? "100 (max limit exceeded)" : discount) + "," + (refundable == null ? "null" : "\"" + refundable + "\"") + "," + type + "," + venue.toCSV();
    }

    public static Ticket fromCSV(String csv) {
        String[] fields = csv.split(",");

        // 1,John,0.0,0,2025-03-11,100,10.0,"true",VIP,1,Venue,100,BAR
        long id = Long.parseLong(fields[0]);
        String name = fields[1].replaceAll("\"", ""); // Remove quotes
        Coordinates coordinates = new Coordinates(Float.parseFloat(fields[2]), Long.parseLong(fields[3]));
        LocalDate creationDate = LocalDate.parse(fields[4]);
        Integer price = fields[4].equals("null") ? null : Integer.parseInt(fields[5]);
        Double discount = Double.parseDouble(fields[6]);
        Boolean refundable = fields[6].equals("null") ? null : Boolean.parseBoolean(fields[7]);
        TicketType type = TicketType.valueOf(fields[8].replaceAll("\"", ""));
        Venue venue = new Venue(Long.parseLong(fields[9]), fields[10], Integer.parseInt(fields[11]), VenueType.valueOf(fields[12]));
    
        return new Ticket(id, name, coordinates, creationDate, price, discount, refundable, type, venue);
    }

    // вернет id,name,coordinates.x,coordinates.y,creationDate,price,discount,refundable...
    private static List<String> getFieldNames(Class<?> clazz, String prefix) {
        List<String> fieldNames = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = prefix.isEmpty() ? field.getName() : prefix + "." + field.getName();
            if (isComplexType(field.getType())) {
                fieldNames.addAll(getFieldNames(field.getType(), fieldName));
            } else {
                fieldNames.add(fieldName);
            }
        }
        return fieldNames;
    }

    private static boolean isComplexType(Class<?> type) {
        return !type.isPrimitive() && !type.getName().startsWith("java.") && !type.isEnum();
    }

    // вернет id,name,coordinates.x,coordinates.y,creationDate,price,discount,refundable...
    public static List<String> getFieldNames(Class<?> clazz) {
        return getFieldNames(clazz, "");
    }

    public static String getFieldNamesAsCsv() {
        List<String> fields = getFieldNames(Ticket.class);
        fields.remove(0); //удаление idCounter
        return String.join(",", fields);
    }

    @Override
    public String toString() {
        return "ticket{\"id\": " + id + "; " +
                "\"name\": \"" + name + "\"; " +
                "\"creationDate\": \"" + creationDate + "\"; " +
                "\"coordinates\": \"" + coordinates + "\"; " +
                "\"price\": " + (price == null ? "null" : price) + "; " +
                "\"discount\": " + (discount > 100 ? "100 (max limit exceeded)" : discount) + "; " +
                "\"refundable\": " + (refundable == null ? "null" : "\"" + refundable + "\"") + "; " +
                "\"type\": \"" + type + "\"; " +
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
    public int compareTo(Element element) {
        return Long.compare(this.id, element.getId());
    }

    
    @Override
    public long getId() {
        return id;
    }

    public String getName(){
        return this.name;
    }

    public Coordinates getCoordinates(){
        return this.coordinates;
    }

    public java.time.LocalDate getCreationDate() {
        return creationDate;
    }

    public Integer getPrice() {
        return price;
    }

    public Double getDiscount() {
        return discount;
    }

    public Boolean getRefundable() {
        return refundable;
    }

    public TicketType getType() {
        return type;
    }

    public Venue getVenue() {
        return venue;
    }
}


