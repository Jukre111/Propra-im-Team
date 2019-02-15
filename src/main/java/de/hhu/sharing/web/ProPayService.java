package de.hhu.sharing.web;

import com.google.gson.Gson;
import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.model.Account;
import de.hhu.sharing.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProPayService {

    @Autowired
    ItemRepository itemRepo;

    RestTemplate rt = new RestTemplate();

    /*
        ReservationID is definite in ProPay, meaning each ReservationID
        no matter from whom to whom is increase by one.
        Transfer and Reservation from Money only works,
        as long as the SourceUser has enough money, else there is an error message.
        The number of the error message is returned to another service
        which has to deal with it.
     */

    public Account showAccount(String username) {
        String URL = "http://localhost:8888/account/" + username + "/";
        String jsonAccount = rt.getForObject(URL, String.class);
        Account account = new Gson().fromJson(jsonAccount, Account.class);
        return account;
    }

    public void createAccount(String username) {
        this.showAccount(username);
    }

    public void raiseBalance(String username, int amount) {
        String URL = "http://localhost:8888/account/" + username + "?amount=" + Integer.toString(amount);
        this.callURL(URL, "POST");
    }

    //returns a http response or in case of an Exception an -1
    public int transferMoney(String usernameSource, String usernameTarget, int amount) {
        String URL = "http://localhost:8888/account/" + usernameSource + "/transfer/" + usernameTarget + "?amount=" + Integer.toString(amount);
        int response = this.callURL(URL, "POST");
        return response;
    }

    //returns a http response or in case of an Exception an -1
    public int createDeposit(String usernameSource, String usernameTarget, Item item) {
        int amount = item.getDeposit();
        String URL = "http://localhost:8888/reservation/reserve/" + usernameSource + "/" + usernameTarget + "?amount=" + Integer.toString(amount);
        int response = this.callURL(URL, "POST");
        Account account = this.showAccount(usernameSource);
        item.setReservationId(account.getLatestReservationId());
        itemRepo.save(item);
        return response;

    }

    public void cancelDeposit(String usernameSource, Item item) {
        int reserverationId = item.getReservationId();
        String URL = "http://localhost:8888/reservation/release/" + usernameSource + "?reservationId=" + Integer.toString(reserverationId);
        this.callURL(URL, "POST");
        item.setReservationId(-1);
        itemRepo.save(item);

    }

    public void collectDeposit(String usernameSource, Item item) {
        int reserverationId = item.getReservationId();
        String URL = "http://localhost:8888/reservation/punish/" + usernameSource + "?reservationId=" + Integer.toString(reserverationId);
        this.callURL(URL, "POST");
        item.setReservationId(-1);
        itemRepo.save(item);
    }

    //returns a http response or in case of an Exception an -1
    private int callURL(String Url, String method) {
        URL url = null;
        try {
            url = new URL(Url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.connect();
            return connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("URL bugged/not reachable");
        }
        return -1;
    }
    /* Possible responses:
    private void checkForLegalRequest (int response) {
        switch (response){
            case (200):
                System.out.println("Execution succsessful.");
                break;
            case (-1):
                System.out.println("IOException: Url or connection failed.");
                break;
            case (402):
                System.out.println("Source has not enough money for transaction.");
                break;
            case (405):
                System.out.println("Wrong request method.");
                break;
            default:
                System.out.println("You fucked up really bad.");
                break;

        }
    }*/
}
