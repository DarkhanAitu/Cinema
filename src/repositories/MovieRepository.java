package repositories;

import data.PostgresDB;
import models.Movie;
import models.MovieCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class MovieRepository {

    private final Connection connection = PostgresDB.getInstance().getConnection();
    public List<Movie> getAll() {
        List<Movie> movies = new ArrayList<>();
        String sql = """
        SELECT m.id, m.title, m.duration, m.price,
               c.id AS category_id, c.name AS category_name
        FROM movies m
        JOIN categories c ON m.category_id = c.id
    """;

        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                MovieCategory category = new MovieCategory(
                        rs.getInt("category_id"),
                        rs.getString("category_name")
                );

                movies.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("duration"),
                        rs.getDouble("price"),
                        category
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }


    public void addMovie(Movie movie) {
        String sql = "INSERT INTO movies(title, duration, price, category_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) { // <-- try с ресурсами
            ps.setString(1, movie.getTitle());
            ps.setInt(2, movie.getDuration());
            ps.setDouble(3, movie.getPrice());
            ps.setInt(4, movie.getCategory().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public double getPrice(int movieId) {
        String sql = "SELECT price FROM movies WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
