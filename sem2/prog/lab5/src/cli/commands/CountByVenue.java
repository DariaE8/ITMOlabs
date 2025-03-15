package cli.commands;
import cli.prompts.VenuePrompt;
import models.Command;
import models.TicketManager;
import models.Venue;

public class CountByVenue extends Command {
    private TicketManager tm;

    public CountByVenue() {
        super("сount_by_venue", "вывести количество элементов, значение поля venue которых равно заданному");
    }

    @Override
     //вывести количество элементов, значение поля venue которых равно заданному
     public void run(String[] args) {
        VenuePrompt vp = new VenuePrompt(terminal);
        try{
            Venue venue = vp.ask();
            tm.countByVenue(venue);
        } catch (Exception e) {
            terminal.printError("Ошибка ввода: " + e.getMessage());
        }
    }
}
