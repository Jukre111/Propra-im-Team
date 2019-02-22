package de.hhu.sharing.services;

import com.google.gson.Gson;
import de.hhu.sharing.data.TransactionRepository;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import de.hhu.sharing.propay.Account;
import de.hhu.sharing.propay.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class ProPayService {

    @Autowired
    private TransactionRepository transactions;

    private RestTemplate rt = new RestTemplate();
    private String URL = "http://localhost:8888/";

    /*
        ReservationID is definite in ProPay, meaning each ReservationID
        no matter from whom to whom is increase by one.
        Transfer and Reservation from Money only works,
        as long as the SourceUser has enough money, else there is an error message.
        The number of the error message is returned to another service
        which has to deal with it.
     */

    public Account getAccount(User user) {
        String URL = this.URL + "account/" + user.getUsername() + "/";
        this.callURL(URL, "GET");
        String jsonAccount = rt.getForObject(URL, String.class);
        return new Gson().fromJson(jsonAccount, Account.class);
    }

    public boolean checkFinances(User user, Item item, LocalDate startdate, LocalDate enddate){
        int days = (int) DAYS.between(startdate, enddate) + 1;
        int rent = item.getRental() * days;
        int amount = this.getAccount(user).getAmount();
        return amount >= (rent + item.getDeposit());
    }

    public void raiseBalance(User user, int amount) {
        String URL = this.URL + "account/" + user.getUsername() + "?amount=" + amount;
        this.callURL(URL, "POST");
    }

    public void transferMoney(User sender, User receiver, int amount) {
        String URL = this.URL + "account/" + sender.getUsername() + "/transfer/" + receiver.getUsername() + "?amount=" + amount;
        this.callURL(URL, "POST");
    }

    public void createDeposit(User sender, User receiver, Transaction transaction) {
        int amount = transaction.getDeposit();
        String URL = this.URL + "reservation/reserve/" + sender.getUsername() + "/" + receiver.getUsername() + "?amount=" + amount;
        this.callURL(URL, "POST");
    }

    public void cancelDeposit(User sender, Transaction transaction) {
        Long reservationId = transaction.getId();
        String URL = this.URL + "reservation/release/" + sender.getUsername() + "?reservationId=" + reservationId;
        this.callURL(URL, "POST");
    }

    public void collectDeposit(User sender, Transaction transaction) {
        Long reservationId = transaction.getId();
        String URL = this.URL + "reservation/punish/" + sender.getUsername() + "?reservationId=" + reservationId;
        this.callURL(URL, "POST");
        transaction.setDepositRevoked(true);
    }

    private void callURL(String urlString, String method) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.connect();
            connection.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException("ProPay not reachable!");
        }
    }

    public void changeTemplateTo(RestTemplate rt) {
        this.rt = rt;
    }

}
