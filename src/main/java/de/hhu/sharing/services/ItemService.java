package de.hhu.sharing.services;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.RentPeriodRepository;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.RentPeriod;
import de.hhu.sharing.model.Request;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItemService{

    @Autowired
    private ItemRepository items;

    @Autowired
    private RentPeriodRepository rentPeriods;

    public void create(String name, String description, Integer rental, Integer deposit, User user) {
        Item item = new Item(name, description, rental, deposit, user);
        items.save(item);
    }

    public void edit(Long id, String name, String description, Integer rental, Integer deposit, User user) {
        Item item = this.get(id);
        item.setName(name);
        item.setDescription(description);
        item.setRental(rental);
        item.setDeposit(deposit);
        item.setLender(user);
        items.save(item);
    }

    public void delete(Long id) {
        Item item = this.get(id);
        items.delete(item);
    }

    public Item get(Long id) {
        Item item = this.items.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Item not found!"));
        return item;
    }

    public List<Item> getAll() {
        return this.items.findAll();
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



    public List<Item> searchFor(String query) {
        return this.items.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query,query);
    }

    public void accept(Item item, Request request) {
        this.addToPeriods(item, request);
        this.removeFromRequests(request);
        this.removeOverlappingRequests(item, request);
    }

    private void removeOverlappingRequests(Item item, Request request) {
        item.removeOverlappingRequests(request);
        items.save(item);
    }

    private void addToPeriods(Item item, Request request) {
        RentPeriod period = new RentPeriod(request.getStartdate(), request.getEnddate(), request.getRequester());
        rentPeriods.save(period);
        item.addToPeriods(period);
        items.save(item);
    }

}
