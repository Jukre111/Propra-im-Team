package de.hhu.sharing.propay;

import de.hhu.sharing.model.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class TransactionPurchase {
    @Id
    @GeneratedValue
    private Long id;
    private int price;

    //@ManyToOne
    //private lendableItem lendableItem;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    public TransactionPurchase() {
    }

    public TransactionPurchase(int price, User sender, User receiver) {
        this.price = price;
        //this.lendableItem = lendableItem;
        this.sender = sender;
        this.receiver = receiver;
    }
}
