package models;

public class Movie {
    private int id;
    private String title;
    private int duration; // мин
    private double price; // цена билета
    private MovieCategory category;

    public Movie(int id, String title, int duration, double price, MovieCategory category) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.price = price;
        this.category = category;
    }

    // Геттеры
    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getDuration() { return duration; }
    public double getPrice() { return price; }
    public MovieCategory getCategory() { return category; }

    @Override
    public String toString() {
        return id + " | " + title +
                " (" + duration + " min) - $" + price +
                " | Category: " + (category != null ? category.getName() : "None");
    }
}
