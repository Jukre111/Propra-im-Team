package de.hhu.sharing.data;

import de.hhu.sharing.propay.TransactionRental;
import de.hhu.sharing.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRentalRepository extends CrudRepository<TransactionRental, Long> {
    List<TransactionRental> findAll();
    List<TransactionRental> findAllBySender(User user);
    List<TransactionRental> findAllByReceiver(User user);

    Optional<TransactionRental> findByProcessId(Long id);

}
