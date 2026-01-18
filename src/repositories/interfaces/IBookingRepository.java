package repositories.interfaces;

public interface IBookingRepository {
    boolean isSeatTaken(int seatId, int movieId);
    boolean doesSeatExist(int seatId); // Добавьте эту строку
    void createBooking(int userId, int movieId, int seatId, double price);
}
