package de.hhu.sharing.data;

import de.hhu.sharing.model.Request;
import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends CrudRepository<Request, Long> {
}
