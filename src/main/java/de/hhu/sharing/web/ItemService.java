package de.hhu.sharing.web;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemService{

        @Autowired
        private ItemRepository items;

        public Item get(Long id) {
            Item item = this.items.findById(id)
                    .orElseThrow(
                            () -> new RuntimeException("Item not found!"));
            return item;
        }
}
