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
    TransactionService transService;

    private RestTemplate rt = new RestTemplate();
    private String URL = "http://localhost:8888/";

    public Account getAccount(User user) {
        String URL = this.URL + "account/" + user.getUsername() + "/";
        this.callURL(URL, "GET");
        String jsonAccount = rt.getForObject(URL, String.class);
        return new Gson().fromJson(jsonAccount, Account.class);
    }

    public boolean enoughCredit(User user, Item item, LocalDate startdate, LocalDate enddate){
        int days = (int) DAYS.between(startdate, enddate) + 1;
        int rent = item.getRental() * days;
        int amount = this.getAccount(user).getAmount();
        return amount >= (rent + item.getDeposit()+getDepositSum(this.getAccount(user)));
    }

    public void rechargeCredit(User user, int amount) {
        String URL = this.URL + "account/" + user.getUsername() + "?amount=" + amount;
        this.callURL(URL, "POST");
    }

    public void initiateTransaction(Transaction transaction) {
        String URL = this.URL
                + "account/" + transaction.getSender().getUsername()
                + "/transfer/" + transaction.getReceiver().getUsername()
                + "?amount=" + transaction.getWholeRent();
        this.callURL(URL, "POST");
        URL = this.URL
                + "reservation/reserve/" + transaction.getSender().getUsername()
                + "/" + transaction.getReceiver().getUsername()
                + "?amount=" + transaction.getDeposit();
        this.callURL(URL, "POST");
        Account account = this.getAccount(transaction.getSender());
        transaction.setId(account.getLastReservationId());
    }

    public void releaseDeposit(User sender, Transaction transaction) {
        String URL = this.URL + "reservation/release/" + sender.getUsername() + "?reservationId=" + transaction.getId();
        this.callURL(URL, "POST");
        transService.setDepositRevoked(transaction,"Nein");
    }

    public void punishDeposit(User sender, Transaction transaction) {
        String URL = this.URL + "reservation/punish/" + sender.getUsername() + "?reservationId=" + transaction.getId();
        this.callURL(URL, "POST");
        transService.setDepositRevoked(transaction,"Ja");
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

    public int getDepositSum (Account account){
        int depositSum = 0;
        for(int i = 0; i<account.getReservations().size(); i++) {
            depositSum += account.getReservations().get(i).getAmount();
        }
        return depositSum;
    }
}
