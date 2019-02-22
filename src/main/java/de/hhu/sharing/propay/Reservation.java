package de.hhu.sharing.propay;

import lombok.Data;

@Data
public class Reservation {

    private int id;
    private int amount;

    public Reservation(){
    }

    public Reservation(int reservationId, int amount){
        this.amount = amount;
        this.id = reservationId;
    }
}
