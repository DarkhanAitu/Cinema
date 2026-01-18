package repositories;

import data.PostgresDB;
import repositories.interfaces.IBookingRepository;

import java.sql.*;

public class BookingRepository implements IBookingRepository {

    @Override
    public boolean doesSeatExist(int seatId) {
        String sql = "SELECT COUNT(*) FROM seats WHERE id = ?";
        try (PreparedStatement ps = PostgresDB.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, seatId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean isSeatTaken(int seatId, int movieId) {
        String sql = """
            SELECT COUNT(*) FROM tickets
            WHERE seat_id = ? AND movie_id = ?
        """;

        try (PreparedStatement ps =
                     PostgresDB.getInstance().getConnection().prepareStatement(sql)) {

            ps.setInt(1, seatId);
            ps.setInt(2, movieId);

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void createBooking(int userId, int movieId, int seatId, double price) {
        try {
            Connection conn = PostgresDB.getInstance().getConnection();

            PreparedStatement booking =
                    conn.prepareStatement(
                            "INSERT INTO bookings(user_id) VALUES (?)",
                            Statement.RETURN_GENERATED_KEYS
                    );

            booking.setInt(1, userId);
            booking.executeUpdate();

            ResultSet keys = booking.getGeneratedKeys();
            keys.next();
            int bookingId = keys.getInt(1);

            PreparedStatement ticket =
                    conn.prepareStatement(
                            "INSERT INTO tickets(booking_id, seat_id, movie_id, price) VALUES (?,?,?,?)"
                    );

            ticket.setInt(1, bookingId);
            ticket.setInt(2, seatId);
            ticket.setInt(3, movieId);
            ticket.setDouble(4, price);
            ticket.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}