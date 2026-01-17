package data;

import data.interfaces.IDatabase;
import java.sql.*;

public class PostgresDB implements IDatabase {

    private static PostgresDB instance;
    private Connection connection;

    private PostgresDB() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/cinemadb",
                    "postgres",
                    "5677"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PostgresDB getInstance() {
        if (instance == null) {
            instance = new PostgresDB();
        }
        return instance;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
