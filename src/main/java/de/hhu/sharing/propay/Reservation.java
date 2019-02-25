package de.hhu.sharing.propay;

import lombok.Data;

@Data
public class Reservation {

    private Long id;
    private int amount;

    public Reservation(){
    }

    public Reservation(Long id, int amount){
        this.id = id;
        this.amount = amount;
    }
}
