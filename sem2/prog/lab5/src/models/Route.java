package models;

public class Route {
    private int id;
    private String name;

    public Route(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Route fromArgs(String[] args) {
        if (args.length < 2) throw new IllegalArgumentException("Недостаточно данных");
        int id = Integer.parseInt(args[0]);
        String name = args[1];
        return new Route(id, name);
    }
}
 
    

