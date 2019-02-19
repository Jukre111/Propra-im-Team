package de.hhu.sharing.data;

import de.hhu.sharing.model.Conflict;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConflictRepository extends CrudRepository <Conflict, Long> {

    List <Conflict> findAll();
}
