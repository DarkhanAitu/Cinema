package repositories.interfaces;

import models.Movie;
import models.MovieCategory;

import java.util.List;

public interface IMovieRepository {
    List<Movie> getAll();
    List<Movie> getByCategory(MovieCategory category);
    boolean existsById(int id);
}
