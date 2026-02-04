package data;

import data.interfaces.IDatabase;
import java.sql.*;

public class PostgresDB implements IDatabase {

    private static PostgresDB instance;
    private Connection connection;

    private PostgresDB() {
        String url = "jdbc:postgresql://localhost:5432/cinemadb";
        String user = "postgres";
        String password = "0000";

        try {
            connection = DriverManager.getConnection(
                    url,
                    user,
                    password
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