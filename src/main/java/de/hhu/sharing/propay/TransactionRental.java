package de.hhu.sharing.propay;

import de.hhu.sharing.model.lendableItem;
import de.hhu.sharing.model.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class TransactionRental {

    @Id
    private Long id;
    private int wholeRent;
    private int deposit;
    private Long processId;
    private String depositRevoked = "offen";

    @ManyToOne
    private lendableItem lendableItem;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    public TransactionRental() {
    }

    public TransactionRental(int wholeRent, int deposit, Long processId, lendableItem lendableItem, User sender, User receiver) {
        this.wholeRent = wholeRent;
        this.deposit = deposit;
        this.processId = processId;
        this.lendableItem = lendableItem;
        this.sender = sender;
        this.receiver = receiver;
    }
}
