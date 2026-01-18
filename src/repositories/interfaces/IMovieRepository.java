package repositories.interfaces;

import models.Movie;
import java.util.List;

public interface IMovieRepository {
    List<Movie> getAll();
    boolean existsById(int id);
}
