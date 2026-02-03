package repositories;

import data.PostgresDB;

import java.sql.*;

public class BookingRepository {

    private final Connection connection = PostgresDB.getInstance().getConnection();


    public void showSeatsWithStatus(int movieId) {
        String sql = """
        SELECT s.id, s.seat_number, s.seat_type,
               CASE
                   WHEN t.id IS NULL THEN 'FREE'
                   ELSE 'BOOKED'
               END AS status
        FROM seats s
        LEFT JOIN tickets t
            ON s.id = t.seat_id AND t.movie_id = ?
        ORDER BY s.id
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();

            System.out.println("Seats:");
            while (rs.next()) {
                int seatId = rs.getInt("id");
                String seatNumber = rs.getString("seat_number");
                String seatType = rs.getString("seat_type");
                String status = rs.getString("status");

                double price = getSeatPrice(seatId);

                System.out.println(seatId + ": " + seatNumber + " (" + seatType + ") - " + status + " - $" + price);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   public boolean doesSeatExist(int seatId) {
        String sql = "SELECT 1 FROM seats WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, seatId);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isSeatTaken(int seatId, int movieId) {
        String sql = "SELECT 1 FROM tickets WHERE seat_id = ? AND movie_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, seatId);
            ps.setInt(2, movieId);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            return true;
        }
    }

    public double getSeatPrice(int seatId) {
        String sql = "SELECT price FROM seats WHERE id = ?";
        try (PreparedStatement ps = PostgresDB.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, seatId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public void createBooking(int userId, int movieId, int seatId, double price) {
        try {

            PreparedStatement booking = connection.prepareStatement(
                    "INSERT INTO bookings(user_id) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            booking.setInt(1, userId);
            booking.executeUpdate();

            ResultSet keys = booking.getGeneratedKeys();
            keys.next();
            int bookingId = keys.getInt(1);

            PreparedStatement ticket = connection.prepareStatement(
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

    public void getFullBooking(int userId, int movieId) {
        String sql = """
        SELECT b.id AS booking_id,
               u.username,
               u.role,
               m.title,
               m.duration,
               s.seat_number,
               s.seat_type,
               h.name AS hall_name,
               t.price AS ticket_price
        FROM bookings b
        JOIN users u ON b.user_id = u.id
        JOIN tickets t ON t.booking_id = b.id
        JOIN movies m ON t.movie_id = m.id
        JOIN seats s ON t.seat_id = s.id
        JOIN halls h ON s.hall_id = h.id
        WHERE u.id = ? AND m.id = ?
        ORDER BY s.id
    """;

        try (PreparedStatement ps = PostgresDB.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, movieId);

            ResultSet rs = ps.executeQuery();
            boolean hasResult = false;
            System.out.printf("%-10s | %-10s | %-8s | %-20s | %-5s | %-6s | %-8s | %-12s | %-5s%n",
                    "Booking ID", "User", "Role", "Movie", "Min", "Seat", "Type", "Hall", "Price");
            System.out.println("-");

            while (rs.next()) {
                hasResult = true;
                System.out.printf("%-10d | %-10s | %-8s | %-20s | %-5d | %-6s | %-8s | %-12s | %-5.2f%n",
                        rs.getInt("booking_id"),
                        rs.getString("username"),
                        rs.getString("role"),
                        rs.getString("title"),
                        rs.getInt("duration"),
                        rs.getString("seat_number"),
                        rs.getString("seat_type"),
                        rs.getString("hall_name"),
                        rs.getDouble("ticket_price")
                );
            }

            if (!hasResult) {
                System.out.println("No bookings found for this user and movie!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
