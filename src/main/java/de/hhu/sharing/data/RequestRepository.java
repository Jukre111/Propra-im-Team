package de.hhu.sharing.data;

import de.hhu.sharing.model.Request;
import de.hhu.sharing.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequestRepository extends CrudRepository<Request, Long> {
    List<Request> findAllByRequester(User user);
}
