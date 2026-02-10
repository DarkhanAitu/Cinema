package repositories;

import data.PostgresDB;
import models.Movie;
import models.MovieCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {

    private final Connection connection;

    public MovieRepository() {
        this.connection = PostgresDB.getInstance().getConnection();
    }

    public void addMovie(Movie movie) {
        String sql = "INSERT INTO movies(title, duration, price, category) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = PostgresDB.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, movie.getTitle());
            ps.setInt(2, movie.getDuration());
            ps.setDouble(3, movie.getPrice());
            ps.setString(4, movie.getCategory().name()); // <-- сохраняем категорию в базу
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

    public List<Movie> getAll() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT id, title, duration, price, category FROM movies ORDER BY id ASC";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("duration"),
                        rs.getDouble("price"),
                        MovieCategory.valueOf(rs.getString("category")) // вот тут подтягиваем категорию
                );
                movies.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
    public List<Movie> getByCategory(MovieCategory category) {
        List<Movie> movies = new ArrayList<>();

        String sql = "SELECT id, title, duration, price, category FROM movies WHERE category = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, category.name());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                movies.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("duration"),
                        rs.getDouble("price"),
                        MovieCategory.valueOf(rs.getString("category"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }
    public boolean updateMovie(int movieId, String title, Integer duration, Double price, MovieCategory category) {
        StringBuilder sql = new StringBuilder("UPDATE movies SET ");
        List<Object> params = new ArrayList<>();

        if (title != null) {
            sql.append("title = ?, ");
            params.add(title);
        }
        if (duration != null) {
            sql.append("duration = ?, ");
            params.add(duration);
        }
        if (price != null) {
            sql.append("price = ?, ");
            params.add(price);
        }
        if (category != null) {
            sql.append("category = ?, ");
            params.add(category.name());
        }

        if (params.isEmpty()) return false;
        sql.setLength(sql.length() - 2);

        sql.append(" WHERE id = ?");
        params.add(movieId);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}