package repositories;

import data.PostgresDB;
import models.Movie;
import repositories.interfaces.IMovieRepository;

import java.sql.*;
import java.util.*;

public class MovieRepository implements IMovieRepository {

    @Override
    public List<Movie> getAll() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies";

        try (Statement st = PostgresDB.getInstance().getConnection().createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                int duration = rs.getInt("duration");

                System.out.println(id + " | " + title + " (" + duration + " min)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
}