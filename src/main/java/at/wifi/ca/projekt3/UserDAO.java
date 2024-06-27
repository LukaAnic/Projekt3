package at.wifi.ca.projekt3;

import at.wifi.ca.projekt3.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    protected Connection con;

    public UserDAO() throws SQLException {
        this.con = DbConnection.getInstance();
    }

    public List<User> getAll() {
        ArrayList<User> users = new ArrayList<>();

        return users;
    }

    public User findeById(int userId) {

        return null;
    }

    public User findByEmail(String email) throws SQLException {
        try (
                PreparedStatement pStmt = this.con.prepareStatement("SELECT * FROM Users WHERE email = ?")
        ) {
            pStmt.setString(1, email);

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()){
                return new User(rs.getInt("id"), rs.getString("userName"), rs.getString("email"), rs.getString("password"));
            }
        }

        return null;
    }

    public boolean create(User user) throws SQLException {
        try (
                PreparedStatement pStmt = this.con.prepareStatement("INSERT INTO Users(userName, email, password) VALUES(?,?,?)")
        ) {
            pStmt.setString(1, user.getUserName());
            pStmt.setString(2, user.getEmail());
            pStmt.setString(3, user.getPassword());

            pStmt.execute();
            return true;
        }
    }

    public void update(User user) {

    }

    public void delete(User user) {
        this.delete(user.getId());
    }

    public void delete(int UserId) {

    }
}
