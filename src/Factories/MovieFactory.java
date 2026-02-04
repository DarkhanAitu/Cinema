package Factories;

import models.Movie;
import models.MovieCategory;

public class MovieFactory {

    public static Movie createMovie(int id, String title, int duration, double price, MovieCategory category) {
        if (category == null) {

            category = MovieCategory.OTHER;
        }
        return new Movie(id, title, duration, price, category);
    }
}