package de.hhu.sharing.propay;

import lombok.Data;

@Data
public class Reservation {
    int id;
    int amount;

    public Reservation() {}

    public Reservation(int reservationId, int amount) {
        this.amount = amount;
        this.id = reservationId;
    }

}
