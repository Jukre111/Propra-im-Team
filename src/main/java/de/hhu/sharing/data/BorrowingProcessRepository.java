package de.hhu.sharing.data;

import de.hhu.sharing.model.BorrowingProcess;
import de.hhu.sharing.model.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BorrowingProcessRepository extends CrudRepository<BorrowingProcess, Long> {

}
