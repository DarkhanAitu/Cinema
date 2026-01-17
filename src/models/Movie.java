package models;

public class Movie {
    private int id;
    private String title;
    private int duration;

    @Override
    public String toString() {
        return id + " | " + title + " (" + duration + " min)";
    }
}
