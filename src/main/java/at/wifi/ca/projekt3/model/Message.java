package at.wifi.ca.projekt3.model;

import javafx.beans.property.*;
import javafx.css.SizeUnits;

import java.io.*;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected  IntegerProperty id;
    protected  StringProperty text;
    protected  IntegerProperty userId;
    protected BooleanProperty toDelete;

    public Message() {
        this.id = new SimpleIntegerProperty();
        this.text = new SimpleStringProperty();
        this.userId = new SimpleIntegerProperty();
        this.toDelete = new SimpleBooleanProperty();
    }

    public Message(int id, String text, int userId, boolean toDelete) {
        this.id = new SimpleIntegerProperty(id);
        this.text = new SimpleStringProperty(text);
        this.userId = new SimpleIntegerProperty(userId);
        this.toDelete = new SimpleBooleanProperty(toDelete);
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

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public int getUserId() {
        return userId.get();
    }

    public IntegerProperty userIdProperty() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public boolean isToDelete() {
        return toDelete.get();
    }

    public BooleanProperty toDeleteProperty() {
        return toDelete;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete.set(toDelete);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(id.get());
        out.writeObject(text.get());
        out.writeObject(userId.get());
        out.writeObject(toDelete.get());
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        id = new SimpleIntegerProperty((Integer) in.readObject());
        text = new SimpleStringProperty((String) in.readObject());
        userId = new SimpleIntegerProperty((Integer) in.readObject());
        toDelete = new SimpleBooleanProperty((Boolean) in.readObject());
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", text=" + text +
                ", userId=" + userId +
                '}';
    }
}
