package de.hhu.sharing.data;

import de.hhu.sharing.model.BorrowingProcess;
import de.hhu.sharing.model.Conflict;
import de.hhu.sharing.model.LendableItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConflictRepository extends CrudRepository <Conflict, Long> {
    List<Conflict> findAll();
    List<Conflict> findAllByProcess_Item(LendableItem lendableItem);

    Conflict findByProcess(BorrowingProcess process);
}
