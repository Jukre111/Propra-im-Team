package de.hhu.sharing.propay;

import de.hhu.sharing.model.SellableItem;
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
    private String itemName;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    public TransactionPurchase() {
    }

    public TransactionPurchase(SellableItem sellItem, User sender, User receiver) {
        this.price = sellItem.getPrice();
        this.itemName = sellItem.getName();
        this.sender = sender;
        this.receiver = receiver;
    }
}
