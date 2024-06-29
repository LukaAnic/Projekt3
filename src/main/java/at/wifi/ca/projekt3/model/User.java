package at.wifi.ca.projekt3.model;

import javafx.beans.property.*;

public class User {
    protected IntegerProperty id;
    protected StringProperty userName;
    protected StringProperty email;
    protected StringProperty password;
    protected BooleanProperty alwaysOffline;

    public User(){
     this.id = new SimpleIntegerProperty();
     this.userName = new SimpleStringProperty();
     this.email = new SimpleStringProperty();
     this.password = new SimpleStringProperty();
     this.alwaysOffline = new SimpleBooleanProperty();
    }

    public User(int id, String userName, String email, String password, boolean alwaysOffline) {
        this.id = new SimpleIntegerProperty(id);
        this.userName = new SimpleStringProperty(userName);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.alwaysOffline = new SimpleBooleanProperty(alwaysOffline);
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

    public boolean isAlwaysOffline() {
        return alwaysOffline.get();
    }

    public BooleanProperty alwaysOfflineProperty() {
        return alwaysOffline;
    }

    public void setAlwaysOffline(boolean alwaysOffline) {
        this.alwaysOffline.set(alwaysOffline);
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName=" + userName +
                ", email=" + email +
                ", password=" + password +
                ", alwaysOffline=" + alwaysOffline +
                '}';
    }
}
