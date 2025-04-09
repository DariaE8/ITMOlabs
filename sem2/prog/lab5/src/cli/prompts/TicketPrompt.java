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
    public Ticket ask() throws InputCancelledException {
        try{
            String name = promptFor("Введите имя", Function.identity(), "Ошибка: введите корректное имя");
            // terminal.println(name);
            Coordinates coordinates = this.cp.ask();
            // terminal.println(coordinates);
            int price = promptFor("Введите цену", input -> {
                int value = Integer.parseInt(input);
                if (value <= 0) {
                    throw new IllegalArgumentException("Цена должна быть больше 0");
                }
                return value;
            }, "Цена должна быть больше 0");
            // terminal.println(price);
            double discount = promptFor("Введите скидку", input -> {
                double value = Double.parseDouble(input);
                if (value < 0 || value > 100) {
                    throw new IllegalArgumentException("Скидка должна быть в диапазоне от 0 до 100");
                }
                return value;
            }, "Скидка должна быть в диапазоне от 0 до 100");
            // terminal.println(discount);
            boolean refundable = promptFor("Можно вернуть? (true/false)", input -> {
                if (!input.equalsIgnoreCase("true") && !input.equalsIgnoreCase("false")) {
                    throw new IllegalArgumentException("Введите true или false");
                }
                return Boolean.parseBoolean(input);
            }, "Введите true или false");
            // terminal.println(refundable);
            TicketType type = promptFor("Тип билета (VIP, USUAL, BUDGETARY)", input -> {
                try {
                    return TicketType.valueOf(input.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Допустимые значения: VIP, USUAL, BUDGETARY");
                }
            }, "Ошибка: введите корректный тип билета");
            // terminal.println(type);
            Venue venue = this.vp.ask();
            // terminal.println(venue);
        
            Ticket ticket = new Ticket(name, coordinates, price, discount, refundable, type, venue);
            // terminal.println(ticket);
            return ticket;
        } catch (NumberFormatException e) {
            terminal.printError("Некорректный формат числа. Пожалуйста, попробуйте снова.");
            return null;
        } catch (IllegalArgumentException e) {
            terminal.printError("Ошибка в данных: " + e.getMessage());
            return null;
        } catch (Exception e) {
            if (terminal.checkScanner()) {
                terminal.printError("Произошла непредвиденная ошибка: " + e.getMessage());
                return null;
            } else {                
                throw new InputCancelledException(e.getMessage());
            }
        }
    }
    
}
