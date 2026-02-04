package models;

public class Movie {
    private int id;
    private String title;
    private int duration;
    private double price;
    private MovieCategory category;

    public Movie(int id, String title, int duration, double price, MovieCategory category) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.price = price;
        this.category = category;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getDuration() { return duration; }
    public double getPrice() { return price; }
    public MovieCategory getCategory() { return category; }

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setPrice(double price) { this.price = price; }
    public void setCategory(MovieCategory category) { this.category = category; }
}