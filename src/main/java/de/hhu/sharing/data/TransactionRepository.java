package de.hhu.sharing.data;

import de.hhu.sharing.propay.Transaction;
import de.hhu.sharing.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    List<Transaction> findAll();
    List<Transaction> findAllBySender(User user);
    List<Transaction> findAllByReceiver(User user);

    Transaction findByProcessId(long id);

}
