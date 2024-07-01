package at.wifi.ca.projekt3;

import at.wifi.ca.projekt3.model.Message;
import at.wifi.ca.projekt3.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class MasterDAO {
    private static final Connection con;

    static {
        try {
            con = DbConnection.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    ////////////////////// Users /////////////////////
    public static List<User> UsersGetAll() {
        ArrayList<User> users = new ArrayList<>();

        return users;
    }

    public static User UserFindById(int userId) throws SQLException {
        try (
                PreparedStatement pStmt = con.prepareStatement("SELECT * FROM Users WHERE id = ?")
        ) {
            pStmt.setInt(1, userId);

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()){
                return new User(rs.getInt("id"), rs.getString("userName"), rs.getString("email"), rs.getString("password"), rs.getBoolean("alwaysOffline"), rs.getBoolean("currentlyOnline"));
            }
        }

        return null;
    }

    public static User UserFindByEmail(String email) throws SQLException {
        try (
                PreparedStatement pStmt = con.prepareStatement("SELECT * FROM Users WHERE email = ?")
        ) {
            pStmt.setString(1, email);

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()){
                return new User(rs.getInt("id"), rs.getString("userName"), rs.getString("email"), rs.getString("password"), rs.getBoolean("alwaysOffline"), rs.getBoolean("currentlyOnline"));
            }
        }

        return null;
    }

    public static boolean UserCreate(User user) throws SQLException {
        try (
                PreparedStatement pStmt = con.prepareStatement("INSERT INTO Users(userName, email, password, alwaysOffline, currentlyOnline) VALUES(?,?,?,?,?)")
        ) {
            pStmt.setString(1, user.getUserName());
            pStmt.setString(2, user.getEmail());
            pStmt.setString(3, user.getPassword());
            pStmt.setBoolean(4, user.isAlwaysOffline());
            pStmt.setBoolean(5, user.isCurrentlyOnline());

            return pStmt.executeUpdate() > 0;
        }
    }

    public static boolean UserUpdate(User user)throws SQLException {
        try (
                PreparedStatement pStmt = con.prepareStatement("UPDATE Users SET userName = ?, password = ?, alwaysOffline = ?, currentlyOnline = ? WHERE id = ?")
                ){
            pStmt.setString(1, user.getUserName());
            pStmt.setString(2, user.getPassword());
            pStmt.setBoolean(3, user.isAlwaysOffline());
            pStmt.setBoolean(4, user.isCurrentlyOnline());
            pStmt.setInt(5, user.getId());

            return pStmt.executeUpdate() > 0;
        }
    }

    public static void UserDelete(User user) throws SQLException {
        UserDelete(user.getId());
    }

    public static boolean UserDelete(int UserId) throws SQLException {
        try (
                PreparedStatement pStmt = con.prepareStatement("DELETE FROM Users WHERE id = ?")
                ){
            pStmt.setInt(1, UserId);

            return pStmt.executeUpdate() > 0;
        }
    }

    ////////////////// Friends /////////////////////////////

    public static List<User> FriendGetAll(User user) throws SQLException {
        ArrayList<User> friends = new ArrayList<>();

        try(
                PreparedStatement pStmt = con.prepareStatement("SELECT *\n" +
                        "FROM Users\n" +
                        "INNER JOIN Friends AS f1 ON Users.id = f1.user2Id\n" +
                        "WHERE f1.user1Id = ?\n" +
                        "\n" +
                        "UNION\n" +
                        "\n" +
                        "SELECT *\n" +
                        "FROM Users\n" +
                        "INNER JOIN Friends AS f2 ON Users.id = f2.user1Id\n" +
                        "WHERE f2.user2Id = ?;")
                ){
            pStmt.setInt(1, user.getId());
            pStmt.setInt(2, user.getId());

            ResultSet rs = pStmt.executeQuery();

            while (rs.next()){
                User newUser = new User(rs.getInt("id"), rs.getString("userName"), rs.getString("email"), rs.getString("password"), rs.getBoolean("alwaysOffline"), rs.getBoolean("currentlyOnline"));
                friends.add(newUser);
            }

            return friends;
        }
    }

    public static boolean FriendInsert(User user1, User user2) throws SQLException {
        try (
                PreparedStatement pStmt = con.prepareStatement("INSERT INTO Friends (user1Id, user2Id) VALUES (?,?)")
                ){
            pStmt.setInt(1, user1.getId());
            pStmt.setInt(2, user2.getId());

            return pStmt.executeUpdate() > 0;
        }
    }

    public static boolean FriendDelete(User user) throws SQLException {
        try (
                PreparedStatement pStmt = con.prepareStatement("DELETE FROM Friends WHERE user1Id = ? OR user2Id = ?")
                ){
            pStmt.setInt(1, user.getId());
            pStmt.setInt(2, user.getId());

            return pStmt.executeUpdate() > 0;
        }
    }


    ////////////////// Friends /////////////////////////////


    public static int MessageInsert(Message message) throws SQLException {
        try (
                PreparedStatement pStmt = con.prepareStatement("INSERT INTO Messages (text, userId) VALUES (?,?) ", Statement.RETURN_GENERATED_KEYS)
                ){
            pStmt.setString(1, message.getText());
            pStmt.setInt(2, message.getUserId());

            if(pStmt.executeUpdate() > 0){
                //Gibt den Index Key der Nachricht zurück
                return pStmt.getGeneratedKeys().getInt(1);
            }
            return 0;
        }
    }

    public static User MessageGetUser(Message message) throws SQLException {
        return  UserFindById(message.getUserId());
    }
}
