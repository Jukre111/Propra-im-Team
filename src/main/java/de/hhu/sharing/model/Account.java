package de.hhu.sharing.model;

import lombok.Data;

@Data
public class Account {
    String account;
    int ammount;
    Reservation[] reservations;

    public int getLatestReservationId() {
        return reservations[reservations.length - 1].getId();
    }
}
