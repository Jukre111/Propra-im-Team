package de.hhu.sharing.data;

import de.hhu.sharing.model.LendableItem;
import de.hhu.sharing.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LendableItemRepository extends CrudRepository<LendableItem, Long> {
    List<LendableItem> findAll();
    Optional<LendableItem> findById(Long Id);
    List<LendableItem> findAllByOwner(User user);

    List<LendableItem> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String nameQuery, String descriptionQuery);

    List<LendableItem> findFirst2ByOwnerNot(User user);

    Optional <LendableItem> findByRequests_id(Long id);

    Optional <LendableItem> findFirstByOwner(User user);

    List<LendableItem> findAllByRequests_requester(User user);
}