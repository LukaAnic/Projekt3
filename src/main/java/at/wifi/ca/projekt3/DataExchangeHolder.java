package at.wifi.ca.projekt3;

import at.wifi.ca.projekt3.model.User;

public class DataExchangeHolder {
    private User loggedInUser;

    private static DataExchangeHolder INSTANCE;

    private DataExchangeHolder(){}

    public static DataExchangeHolder getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DataExchangeHolder();
        }
        return INSTANCE;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
