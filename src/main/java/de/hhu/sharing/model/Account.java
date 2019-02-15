package de.hhu.sharing.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Account {
    String account;
    int amount;
    ArrayList<Reservation> reservations;

    public int getLatestReservationId() {
        return reservations.get(reservations.size() - 1).getId();
    }
}
