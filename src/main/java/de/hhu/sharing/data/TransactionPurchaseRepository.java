package de.hhu.sharing.data;

import de.hhu.sharing.model.User;
import de.hhu.sharing.propay.TransactionPurchase;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionPurchaseRepository extends CrudRepository<TransactionPurchase, Long> {
    List<TransactionPurchase> findAll();
    List<TransactionPurchase> findAllBySender(User user);
    List<TransactionPurchase> findAllByReceiver(User user);
}
