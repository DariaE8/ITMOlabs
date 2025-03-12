package cli.prompts;

import utils.AbstractPrompt;

import java.util.function.Function;

import cli.Terminal;
import models.Coordinates;
import models.Ticket;
import models.TicketType;
import models.Venue;


public class TicketPrompt extends AbstractPrompt<Ticket>{
    CoordinatesPrompt cp;
    VenuePrompt vp;
    

    public TicketPrompt(Terminal terminal) {
        super(terminal);
        this.cp = new CoordinatesPrompt(terminal);
        this.vp = new VenuePrompt(terminal);
    }

    @Override
    public Ticket ask() throws Exception {
        try{
            String name = promptFor("Введите имя", Function.identity(), "Ошибка: введите корректное имя");
            // terminal.println(name);
            Coordinates coordinates = this.cp.ask();
            // terminal.println(coordinates);
            int price = promptFor("Введите цену", Integer::parseInt, "Ошибка: введите корректную цену");
            // terminal.println(price);
            double discount = promptFor("Введите скидку", Double::parseDouble, "Ошибка: введите корректную скидку");
            // terminal.println(discount);
            boolean refundable = promptFor("Можно вернуть?(true,false)", Boolean::parseBoolean, "Ошибка: введите корректную цену");
            // terminal.println(refundable);
            TicketType type = promptFor("Тип билета(VIP, USUAL, BUDGETARY)", TicketType::valueOf, "Ошибка: введите корректный тип Ticket.");
            // terminal.println(type);
            Venue venue = this.vp.ask();
            // terminal.println(venue);
        
            Ticket ticket = new Ticket(name, coordinates, price, discount, refundable, type, venue);
            // terminal.println(ticket);
            return ticket;
        } catch (Exception e) {
            terminal.printError("Ошибка ввода: " + e.getMessage());
            return null;
        }
    }
    
}
