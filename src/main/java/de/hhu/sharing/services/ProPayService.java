package de.hhu.sharing.services;

import com.google.gson.Gson;
import de.hhu.sharing.data.TransactionRepository;
import de.hhu.sharing.model.Account;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ProPayService {

    @Autowired
    TransactionRepository transRepo;

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

    //returns a http response or in case of an Exception an -1
    public int createAccount(String username) {
        if (this.showAccount(username) == null)
            return -1;
        else
            return 200;
    }

    //returns a http response or in case of an Exception an -1
    public int raiseBalance(String username, int amount) {
        String URL = "http://localhost:8888/account/" + username + "?amount=" + Integer.toString(amount);
        int response = this.callURL(URL, "POST");
        return response;
    }

    //returns a http response or in case of an Exception an -1
    public int transferMoney(String usernameSource, String usernameTarget, int amount) {
        String URL = "http://localhost:8888/account/" + usernameSource + "/transfer/" + usernameTarget + "?amount=" + Integer.toString(amount);
        int response = this.callURL(URL, "POST");
        return response;
    }

    //returns a http response or in case of an Exception an -1
    public int createDeposit(String usernameSource, String usernameTarget, Transaction trans) {
        int amount = trans.getDeposit();
        String URL = "http://localhost:8888/reservation/reserve/" + usernameSource + "/" + usernameTarget + "?amount=" + Integer.toString(amount);
        int response = this.callURL(URL, "POST");
        Account account = this.showAccount(usernameSource);
        if (account == null)
            return -1;
        else {
            trans.setReservationId(account.getLatestReservationId());
            transRepo.save(trans);
        }
        return response;
    }

    //returns a http response or in case of an Exception an -1
    public int cancelDeposit(String usernameSource, Transaction trans) {
        int reservationId = trans.getReservationId();
        String URL = "http://localhost:8888/reservation/release/" + usernameSource + "?reservationId=" + Integer.toString(reservationId);
        int response = this.callURL(URL, "POST");
        return response;
    }

    //returns a http response or in case of an Exception an -1
    public int collectDeposit(String usernameSource, Transaction trans) {
        int reservationId = trans.getReservationId();
        String URL = "http://localhost:8888/reservation/punish/" + usernameSource + "?reservationId=" + Integer.toString(reservationId);
        int response = this.callURL(URL, "POST");
        trans.setDepositRevoked(true);
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
            //e.printStackTrace();
            //System.err.println("URL bugged/not reachable");
        }
        return -1;
    }

    public RestTemplate changeTemplateTo(RestTemplate rt) {
        return this.rt = rt;
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
