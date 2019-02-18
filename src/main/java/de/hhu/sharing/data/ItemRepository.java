package de.hhu.sharing.data;

import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends CrudRepository<Item, Long> {
    List<Item> findAll();
    Optional<Item> findById(Long Id);
    List<Item> findAllByLender(User user);

    List<Item> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String nameQuery, String descriptionQuery);

    Optional<Item> findByIdAndLenderNot(Long id, User user);

    List<Item> findFirst2ByLenderNot(User user);

    Item findByRequests_id(Long id);
}