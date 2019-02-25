package de.hhu.sharing.data;

import de.hhu.sharing.model.lendableItem;
import de.hhu.sharing.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends CrudRepository<lendableItem, Long> {
    List<lendableItem> findAll();
    Optional<lendableItem> findById(Long Id);
    List<lendableItem> findAllByLender(User user);

    List<lendableItem> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String nameQuery, String descriptionQuery);

    List<lendableItem> findFirst2ByLenderNot(User user);

    Optional <lendableItem> findByRequests_id(Long id);

    Optional <lendableItem> findFirstByLender(User user);

    List<lendableItem> findAllByRequests_requester(User user);
}