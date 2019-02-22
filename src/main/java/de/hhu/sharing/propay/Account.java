package de.hhu.sharing.propay;

import de.hhu.sharing.propay.Reservation;
import lombok.Data;

import java.util.ArrayList;

@Data
public class Account {

    private String account;
    private int amount;
    private ArrayList<Reservation> reservations;

    public Account(){
    }

    public Account(String account, int amount, ArrayList<Reservation> reservations){
        this.account = account;
        this.amount = amount;
        this.reservations = reservations;
    }

    public int getLatestReservationId(){
        return reservations.get(reservations.size() - 1).getId();
    }
}
