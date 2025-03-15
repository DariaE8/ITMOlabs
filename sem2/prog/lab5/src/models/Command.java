package cli.models;

import cli.Terminal;

public abstract class Command {
    private final String name;
    private final String description;
    protected Terminal terminal;
    protected TicketManager tm;


    public Command(String name, String description, Terminal terminal, TicketManager tm) { 
        this.name = name;
        this.description = description;
        this.terminal = terminal;
        this.tm = tm; 
    }

    public abstract void run(String[] args);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
