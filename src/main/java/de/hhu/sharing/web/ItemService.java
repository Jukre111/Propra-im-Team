package de.hhu.sharing.web;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.Request;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public List<Item> getAllIPosted(User user) {
        return this.items.findAllByLender(user);
    }


    public List<Item> getAllIRequested(User user) {
        return this.items.findAllByRequests_requester(user);

    }

    public List<Item> getAllMyRequested(User user) {
        List<Item> myRequestedItems = new ArrayList<>();
        List<Item> allMyItems = this.getAllIPosted(user);
        for(Item item : allMyItems){
            if(!item.getRequests().isEmpty()){
                myRequestedItems.add(item);
            }
        }
        return myRequestedItems;
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
