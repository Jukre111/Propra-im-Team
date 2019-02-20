package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Transaction {
    @Id
    int reservationId;
    int wholeRent;
    int deposit;
    boolean depositRevoked;

    public Transaction() {
        depositRevoked = false;
    }

    public Transaction(int wholeRent, int deposit, Item item, User source, User target) {
        this.wholeRent = wholeRent;
        this.deposit = deposit;
        this.depositRevoked = false;
        this.item = item;
        this.source = source;
        this.target = target;
    }

    @ManyToOne
    Item item;

    @ManyToOne
    User source;

    @ManyToOne
    User target;
}
