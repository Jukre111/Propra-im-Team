package de.hhu.sharing.web;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.Request;
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

    public Item getFromRequestId(Long requestId) {
        Item item = this.items.findByRequests_id(requestId)
                .orElseThrow(
                        () -> new RuntimeException("Item not found!"));
        return item;
    }

    public void addToRequests(Long itemId, Request request) {
        Item item = this.get(itemId);
        item.addToRequests(request);
        items.save(item);
    }

    public void removeFromRequests(Request request) {
        Item item = this.getFromRequestId(request.getId());
                item.removeFromRequests(request);
        items.save(item);
    }
}
