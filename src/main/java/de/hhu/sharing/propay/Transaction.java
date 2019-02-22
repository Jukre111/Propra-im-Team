package de.hhu.sharing.propay;

import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Transaction {
    @Id
    int reservationId;
    int wholeRent;
    int deposit;
    long processId;

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
