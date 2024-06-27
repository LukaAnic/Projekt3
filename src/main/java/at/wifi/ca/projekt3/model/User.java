package at.wifi.ca.projekt3.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    protected IntegerProperty id;
    protected StringProperty userName;
    protected StringProperty email;
    protected StringProperty password;

    public User(){
     this.id = new SimpleIntegerProperty();
     this.userName = new SimpleStringProperty();
     this.email = new SimpleStringProperty();
     this.password = new SimpleStringProperty();
    }

    public User(int id, String userName, String email, String password) {
        this.id = new SimpleIntegerProperty(id);
        this.userName = new SimpleStringProperty(userName);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getUserName() {
        return userName.get();
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public void setUserName(String firstName) {
        this.userName.set(firstName);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName=" + userName +
                ", email=" + email +
                ", password=" + password +
                '}';
    }
}
