package de.hhu.sharing.services;

import de.hhu.sharing.data.TransactionPurchaseRepository;
import de.hhu.sharing.model.User;
import de.hhu.sharing.propay.TransactionPurchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class TransactionPurchaseService {

    @Autowired
    TransactionPurchaseRepository transactions;


    public List<TransactionPurchase> getAllFromSender(User user){
        return transactions.findAllBySender(user);
    }

    public List<TransactionPurchase> getAllFromReceiver(User user){
        return transactions.findAllByReceiver(user);
    }

    /*public void createTransactionRental(Item item, User borrower, User lender){
        TransactionPurchase transPur = new TransactionPurchase(item.getPrice(), item, borrower, lender);
        transactions.save(transPur);
    }*/

}
