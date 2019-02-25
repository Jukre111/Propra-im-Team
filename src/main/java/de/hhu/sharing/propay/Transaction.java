package de.hhu.sharing.propay;

import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Transaction {

    @Id
    private Long id;
    private int wholeRent;
    private int deposit;
    private Long processId;
    private boolean depositRevoked = false;

    @ManyToOne
    private Item item;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    public Transaction() {
    }

    public Transaction(int wholeRent, int deposit, Long processId, Item item, User sender, User receiver) {
        this.wholeRent = wholeRent;
        this.deposit = deposit;
        this.processId = processId;
        this.item = item;
        this.sender = sender;
        this.receiver = receiver;
    }
}
