package de.hhu.sharing.web;

public class ProPayService {

    public void showAccount (String username){

    }
    public void createAccount (String username){
        showAccount(username);
    }
    public void raiseBalance (String username, int amount){
        String URL = "http://localhost:8888/account/"+username+"?amount="+Integer.toString(amount));
    }
    public void transferMoney (String usernameSource, String usernameTarget, int amount){
        String URL = "http://localhost:8888/account/"+usernameSource+"/transfer/"+usernameTarget+"?amount="+Integer.toString(amount));
    }
}
