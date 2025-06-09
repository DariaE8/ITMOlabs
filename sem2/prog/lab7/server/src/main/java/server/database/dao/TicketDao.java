package server.database.dao;

import models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDao {
    private final Connection connection;

    public TicketDao(Connection connection) {
        this.connection = connection;
    }

    public Ticket insertTicket(Ticket ticket) throws SQLException {
        String insertVenue = "INSERT INTO venue (name, capacity, type) VALUES (?, ?, ?) RETURNING id";
        String insertTicket = """
            INSERT INTO ticket (name, x, y, creation_date, price, discount, refundable, type, venue_id, owner_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id
            """;

        connection.setAutoCommit(false);
        try (PreparedStatement venueStmt = connection.prepareStatement(insertVenue);
             PreparedStatement ticketStmt = connection.prepareStatement(insertTicket)) {

            Venue venue = ticket.getVenue();
            venueStmt.setString(1, venue.getName());
            venueStmt.setInt(2, venue.getCapacity());
            venueStmt.setString(3, venue.getType().name());
            ResultSet venueRS = venueStmt.executeQuery();
            if (!venueRS.next()) throw new SQLException("Venue insert failed");
            long venueId = venueRS.getLong(1);
            venue.setId(venueId);

            Coordinates coordinates = ticket.getCoordinates();
            ticketStmt.setString(1, ticket.getName());
            ticketStmt.setFloat(2, coordinates.getX());
            ticketStmt.setLong(3, coordinates.getY());
            ticketStmt.setDate(4, Date.valueOf(ticket.getCreationDate()));
            if (ticket.getPrice() != null) ticketStmt.setInt(5, ticket.getPrice()); else ticketStmt.setNull(5, Types.INTEGER);
            ticketStmt.setDouble(6, ticket.getDiscount());
            if (ticket.getRefundable() != null) ticketStmt.setBoolean(7, ticket.getRefundable()); else ticketStmt.setNull(7, Types.BOOLEAN);
            ticketStmt.setString(8, ticket.getType().name());
            ticketStmt.setLong(9, venueId);
            ticketStmt.setLong(10, ticket.getOwnerId());

            ResultSet ticketRS = ticketStmt.executeQuery();
            if (!ticketRS.next()) throw new SQLException("Ticket insert failed");
            long ticketId = ticketRS.getLong(1);
            ticket.setId(ticketId);

            connection.commit();
            return ticket;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<Ticket> getAllTickets() {
        return getTicketsByQuery("SELECT * FROM ticket");
    }

    public boolean deleteAllByOwner(int userId) throws SQLException {
        String sql = "DELETE FROM ticket WHERE owner_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            int affected = stmt.executeUpdate();
            return affected > 0;
        }
    }

    private List<Ticket> getTicketsByQuery(String query) {
        List<Ticket> tickets = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tickets.add(extractTicket(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public boolean updateTicket(Ticket ticket) {
        String sql = """
            UPDATE ticket SET name=?, x=?, y=?, price=?, discount=?, refundable=?, type=?, venue_id=?
            WHERE id=? AND owner_id=?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ticket.getName());
            stmt.setFloat(2, ticket.getCoordinates().getX());
            stmt.setLong(3, ticket.getCoordinates().getY());
            if (ticket.getPrice() != null) stmt.setInt(4, ticket.getPrice()); else stmt.setNull(4, Types.INTEGER);
            stmt.setDouble(5, ticket.getDiscount());
            if (ticket.getRefundable() != null) stmt.setBoolean(6, ticket.getRefundable()); else stmt.setNull(6, Types.BOOLEAN);
            stmt.setString(7, ticket.getType().name());
            stmt.setLong(8, ticket.getVenue().getId());
            stmt.setLong(9, ticket.getId());
            stmt.setLong(10, ticket.getOwnerId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTicket(int ticketId, int userId) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM ticket WHERE id=? AND owner_id=?")) {
            stmt.setInt(1, ticketId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Ticket extractTicket(ResultSet rs) throws SQLException {
        long venueId = rs.getLong("venue_id");
        Venue venue = fetchVenueById(venueId);

        Coordinates coords = new Coordinates(rs.getFloat("x"), rs.getLong("y"));
        return new Ticket(
                rs.getLong("id"),
                rs.getString("name"),
                coords,
                rs.getDate("creation_date").toLocalDate(),
                rs.getObject("price", Integer.class),
                rs.getDouble("discount"),
                rs.getObject("refundable", Boolean.class),
                TicketType.valueOf(rs.getString("type")),
                venue,
                rs.getInt("owner_id")
        );
    }

    private Venue fetchVenueById(long venueId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM venue WHERE id = ?")) {
            stmt.setLong(1, venueId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) throw new SQLException("Venue not found: " + venueId);
                return new Venue(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("capacity"),
                        VenueType.valueOf(rs.getString("type"))
                );
            }
        }
    }
}
