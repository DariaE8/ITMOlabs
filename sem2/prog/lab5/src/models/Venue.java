package models;

import utils.Validatable;
import utils.ConvertibleToCSV;

public class Venue implements Validatable, ConvertibleToCSV {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Integer capacity; //Поле не может быть null, Значение поля должно быть больше 0
    private VenueType type; //Поле не может быть null

    public Venue (long id, String name, Integer capacity, VenueType type) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.type = type;
    }

    @Override
    public String toCSV() {
        return id + "," + name + "," + capacity + "," + type;
    }

    public static Venue fromCSV(String csv) {
        String[] parts = csv.split(",");
        Long id = Long.parseLong(parts[0].split(":")[1].trim());
        String name = parts[1].split(":")[1].trim().replaceAll("\"", "");
        Integer capacity = parts[2].contains("null") ? null : Integer.parseInt(parts[2].split(":")[1].trim());
        VenueType type = VenueType.valueOf(parts[3].split(":")[1].trim().replaceAll("\"", ""));
        return new Venue(id, name, capacity, type);
    }

    @Override
    public String toString() {
        return "venue{\"id\": " + id + ", " +
                "\"name\": \"" + name + "\", " +
                "\"capacity\": " + (capacity == null ? "null" : capacity) + ", " +
                "\"type\": \"" + type + "\"}";
    }

    public boolean validate() {
        if (id == null || id <= 0) return false; // id не может быть null и должен быть > 0
        if (name == null || name.isEmpty()) return false; // name не может быть null или пустым
        if (capacity == null || capacity <= 0) return false; // capacity не может быть null и должен быть > 0
        if (type == null) return false; // type не может быть null

        return true;
    }

}
