package at.wifi.ca.projekt3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    protected static Connection con = null;

    public static Connection getInstance() throws SQLException {
        if (con == null) {
            con = DriverManager.getConnection("jdbc:sqlite:chatDb.db");
        }
        return con;
    }
}
