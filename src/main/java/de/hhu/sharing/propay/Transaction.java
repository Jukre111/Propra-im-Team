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
    private int reservationId;
    private int wholeRent;
    private int deposit;
    private long processId;
    private boolean depositRevoked;

    @ManyToOne
    private Item item;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    public Transaction() {
        depositRevoked = false;
    }

    public Transaction(int wholeRent, int deposit, Item item, User sender, User receiver) {
        this.wholeRent = wholeRent;
        this.deposit = deposit;
        this.depositRevoked = false;
        this.item = item;
        this.sender = sender;
        this.receiver = receiver;
    }
}
