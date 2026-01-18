package repositories;

import data.PostgresDB;
import models.Movie;
import repositories.interfaces.IMovieRepository;

import java.sql.*;
import java.util.*;

public class MovieRepository implements IMovieRepository {
    @Override
    public boolean existsById(int seatId) {
        String sql = "SELECT COUNT(*) FROM seats WHERE id = ?";
        try (PreparedStatement ps = PostgresDB.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, seatId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    @Override
    public List<Movie> getAll() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies";
        try (Statement st = PostgresDB.getInstance().getConnection().createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Movie movie = new Movie(rs.getInt("id"), rs.getString("title"), rs.getInt("duration"));
                movies.add(movie);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return movies;
    }
}