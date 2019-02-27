package de.hhu.sharing.services;

import de.hhu.sharing.data.TransactionPurchaseRepository;
import de.hhu.sharing.model.SellableItem;
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

    @Autowired
    private ProPayService proPayService;


    public List<TransactionPurchase> getAllFromSender(User user){
        return transactions.findAllBySender(user);
    }

    public List<TransactionPurchase> getAllFromReceiver(User user){
        return transactions.findAllByReceiver(user);
    }

    public void createTransactionPurchase(SellableItem item, User buyer, User seller){
        TransactionPurchase transPur = new TransactionPurchase(item, buyer, seller);
        proPayService.initiateTransactionPurchase(transPur);
        transactions.save(transPur);
    }

}
