package de.hhu.sharing.data;

import de.hhu.sharing.model.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {
    List<Item> findAll();
}