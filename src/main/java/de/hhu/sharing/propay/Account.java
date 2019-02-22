package de.hhu.sharing.propay;

import de.hhu.sharing.propay.Reservation;
import lombok.Data;

import java.util.ArrayList;

@Data
public class Account {
    String account;
    int amount;
    ArrayList<Reservation> reservations;

    public Account() {}

    public Account(String account, int amount, ArrayList<Reservation> reservations) {
        this.account = account;
        this.amount = amount;
        this.reservations = reservations;
    }

    public int getLatestReservationId() {
        return reservations.get(reservations.size() - 1).getId();
    }
}
