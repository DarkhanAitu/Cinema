package models;


public enum SeatType {
    STANDARD(150),
    COMFORT(300),
    VIP(500);


    private final double price;


    SeatType(double price) {
        this.price = price;
    }


    public double getPrice() {
        return price;
    }
}
