package server.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.Venue;
import models.VenueType;

public class VenueDao {
    private final Connection connection;

    public VenueDao(Connection connection) {
        this.connection = connection;
    }

    public Venue insertVenue(Venue venue) throws SQLException {
        String sql = "INSERT INTO venues (name, capacity, type) VALUES (?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, venue.getName());
            stmt.setInt(2, venue.getCapacity());
            stmt.setString(3, venue.getType().name());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                venue.setId(rs.getInt("id"));
                return venue;
            }
        }
        return null;
    }

    public Venue getVenueById(int id) {
        String sql = "SELECT * FROM venues WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Venue(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("capacity"),
                    VenueType.valueOf(rs.getString("type"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
