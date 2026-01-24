package models;

public class Movie {
    private int id;
    private String title;
    private int duration;


    public Movie(int id, String title, int duration) {
        this.id = id;
        this.title = title;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return id + " | " + title + " (" + duration + " min)";
    }
}