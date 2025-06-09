package managers;

import java.util.Collection;
import java.util.List;

import models.Ticket;
import models.Venue;

public interface TicketManagerInterface {
    public int size();
    public void clear(int ownerId);
    public long countByVenue(Venue venue);
    public long countByOwner(int owner_id);
    public Collection<Ticket> filterStartsWithName(String prefix);
    public Class<?> getCollectionType();
    public void insert(int key, Ticket ticket) throws Exception;
    public int removeGreater(Ticket referenceTicket);
    public void removeGreaterKeys(int key);
    public Collection<Ticket> getAllTickets();
    public List<String> toCSV();
    public boolean checkIdExist(long id);
    public boolean update(long id, Ticket ticket) throws Exception;
    public void remove(int id, int user_id);
    public Ticket getTicketById(long id);
}