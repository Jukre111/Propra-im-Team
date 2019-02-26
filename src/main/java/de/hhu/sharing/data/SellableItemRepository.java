package de.hhu.sharing.data;

import de.hhu.sharing.model.SellableItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SellableItemRepository extends CrudRepository<SellableItem, Long> {

    List<SellableItem> findAll();

    List<SellableItem> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String nameQuery, String descriptionQuery);


}
