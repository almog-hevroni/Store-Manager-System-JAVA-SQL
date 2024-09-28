package dataBasePackage;

import java.sql.*;

public class DatabaseConnectionManager {
	private static final String URL = "jdbc:postgresql://localhost:5432/Store_Project";   
    private static final String USER = "postgres";
    private static final String PASSWORD = "lior626555"; 
    private static Connection connection = null;

    // Load the JDBC driver and establish a connection
 // Load the JDBC driver
    static {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("DB driver loaded successfully");
            connection = getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
        	 e.printStackTrace();
        }
    }

    // Get the connection
    public static Connection getConnection() throws SQLException {
    	if (connection == null) {
    		connection = DriverManager.getConnection(URL, USER, PASSWORD);
    	}
    	return connection;
    }

    // Close the connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
