package repositories.interfaces;

public interface IBookingRepository {
    boolean isSeatTaken(int seatId, int movieId);
    void createBooking(int userId, int movieId, int seatId, double price);
}
