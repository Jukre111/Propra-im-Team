package de.hhu.sharing.web;

import com.fasterxml.jackson.core.JsonParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.model.Account;
import de.hhu.sharing.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProPayService {

    public String showAccount(String username) throws Exception{
        String URL = "http://localhost:8888/account/" + username + "/";
        String jsonReservation = rt.getForObject(URL, String.class);
        Account account = new Gson().fromJson(jsonReservation, Account.class);
        System.out.println(account.getReservationStrings());
        return account.getReservationStrings();
    }

    public void createAccount(String username) {
        //showAccount(username);
    }

    public void raiseBalance(String username, int amount) {
        String URL = "http://localhost:8888/account/" + username + "?amount=" + Integer.toString(amount);
    }

    public void transferMoney(String usernameSource, String usernameTarget, int amount) {
        String URL = "http://localhost:8888/account/" + usernameSource + "/transfer/" + usernameTarget + "?amount=" + Integer.toString(amount);
    }

    @Autowired
    ItemRepository itemRepo;

    RestTemplate rt = new RestTemplate();

    public void createDeposit(String usernameSource, String usernameTarget) throws IOException {
        //String URL = "http://localhost:8888/reservation/reserve/"+usernameSource+"/"+usernameTarget+"?amount="+Integer.toString(amount);
        String URL = "http://localhost:8888/account/1111?amount=5000";
        String jsonReservation = rt.getForObject(URL, String.class);
        JsonObject reservation = new Gson().fromJson(jsonReservation, JsonObject.class);
        JsonArray reservationArray = reservation.getAsJsonArray("reservations");
        System.out.println(reservationArray.getAsString());
        //callURL(URL, "POST");
        //item.setReservationId();
        //itemRepo.save(item);

    }

    public void cancelDeposit(String usernameSource, Item item) {
        int reserverationId = item.getReservationId();
        String URL = "http://localhost:8888/reservation/release/" + usernameSource + "?reservationId=" + Integer.toString(reserverationId);
        callURL(URL, "POST");
        item.setReservationId(-1);
        itemRepo.save(item);

    }

    public void collectDeposit(String usernameSource, Item item) {
        int reserverationId = item.getReservationId();
        String URL = "http://localhost:8888/reservation/punish/" + usernameSource + "?reservationId=" + Integer.toString(reserverationId);
        callURL(URL, "POST");
        item.setReservationId(-1);
        itemRepo.save(item);
    }

    /*IOException
     * public int createDeposit(String usernameSource, String usernameTarget, int amount) {
        String URL = "http://localhost:8888/reservation/reserve/"+usernameSource+"/"+usernameTarget+"?amount="+Integer.toString(amount);
        //füge hier Code ein der die ReservationID holt
        return reservationID;
     * }
    */

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

}
