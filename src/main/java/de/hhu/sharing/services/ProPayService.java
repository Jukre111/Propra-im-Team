package de.hhu.sharing.services;

import com.google.gson.Gson;
import de.hhu.sharing.data.TransactionRepository;
import de.hhu.sharing.model.User;
import de.hhu.sharing.propay.Account;
import de.hhu.sharing.propay.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ProPayService {

    @Autowired
    private TransactionRepository transactions;

    private RestTemplate rt = new RestTemplate();

    /*
        ReservationID is definite in ProPay, meaning each ReservationID
        no matter from whom to whom is increase by one.
        Transfer and Reservation from Money only works,
        as long as the SourceUser has enough money, else there is an error message.
        The number of the error message is returned to another service
        which has to deal with it.
     */

    public Account getAccount(User user) {
        String URL = "http://localhost:8888/account/" + user.getUsername() + "/";
        String jsonAccount = rt.getForObject(URL, String.class);
        return new Gson().fromJson(jsonAccount, Account.class);
    }

    //returns a http response or in case of an Exception an -1
    public int createAccount(User user) {
        if (this.getAccount(user) == null)
            return -1;
        else
            return 200;
    }

    //returns a http response or in case of an Exception an -1
    public int raiseBalance(User user, int amount) {
        String URL = "http://localhost:8888/account/" + user.getUsername() + "?amount=" + amount;
        int response = this.callURL(URL, "POST");
        return response;
    }

    //returns a http response or in case of an Exception an -1
    public int transferMoney(User sender, User receiver, int amount) {
        String URL = "http://localhost:8888/account/" + sender.getUsername() + "/transfer/" + receiver.getUsername() + "?amount=" + amount;
        int response = this.callURL(URL, "POST");
        return response;
    }

    //returns a http response or in case of an Exception an -1
    public int createDeposit(User sender, User receiver, Transaction transaction) {
        int amount = transaction.getDeposit();
        String URL = "http://localhost:8888/reservation/reserve/" + sender.getUsername() + "/" + receiver.getUsername() + "?amount=" + amount;
        int response = this.callURL(URL, "POST");
        Account account = this.getAccount(sender);
        if (account == null)
            return -1;
        else {
            transaction.setReservationId(account.getLatestReservationId());
            transactions.save(transaction);
        }
        return response;
    }

    //returns a http response or in case of an Exception an -1
    public int cancelDeposit(User sender, Transaction transaction) {
        int reservationId = transaction.getReservationId();
        String URL = "http://localhost:8888/reservation/release/" + sender.getUsername() + "?reservationId=" + reservationId;
        int response = this.callURL(URL, "POST");
        return response;
    }

    //returns a http response or in case of an Exception an -1
    public int collectDeposit(User sender, Transaction transaction) {
        int reservationId = transaction.getReservationId();
        String URL = "http://localhost:8888/reservation/punish/" + sender.getUsername() + "?reservationId=" + reservationId;
        int response = this.callURL(URL, "POST");
        transaction.setDepositRevoked(true);
        return response;
    }

    //returns a http response or in case of an Exception an -1
    public int callURL(String Url, String method) {
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

    public void changeTemplateTo(RestTemplate rt) {
        this.rt = rt;
    }

}
