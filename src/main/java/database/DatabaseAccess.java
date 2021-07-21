package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DatabaseAccess {
    protected static final String DATABASE_NAME = "tasks_data";
    protected static final String DB_USERNAME = "DBM";
    protected static final String DB_PASSWORD = "Tn65z6&dDObh@YJRRt39OwhV";
    protected static final String DB_URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME + "?allowPublicKeyRetrieval=true&useSSL=false";

    protected Connection connection;

    protected void startConnection() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        System.out.println("[+] Connected to database");
    }

    protected void closeConnection() throws SQLException {
        if(connection != null) {
            if (!connection.isClosed()) {
                connection.close();
                System.out.println("[+] Disconnected from database");
            }
        }
    }
}
