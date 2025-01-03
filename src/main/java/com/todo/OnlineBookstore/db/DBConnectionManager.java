package com.todo.OnlineBookstore.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NoArgsConstructor
@Getter
@Setter
public class DBConnectionManager {

    private String url = "jdbc:mysql://localhost:3306/BookstoreDB?allowPublicKeyRetrieval=true&useSSL=false";
    private String username = "bookstore";
    private String password = "admin"; // Make sure these credentials match your MySQL setup
    

    private Connection connection;

    public void openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // For MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL Driver not found!", e);
            }
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
