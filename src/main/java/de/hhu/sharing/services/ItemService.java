package de.hhu.sharing.services;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import de.hhu.sharing.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItemService{

    @Autowired
    private ItemRepository items;

    @Autowired
    private ConflictService conflictService;
    @Autowired
    private StorageService storageService;


    public void create(String name, String description, Integer rental, Integer deposit, User user, MultipartFile file) {
        Item item = new Item(name, description, rental, deposit, user);
        items.save(item);
        if(file!=null) {
        	storageService.storeItem(file, item);
        }else {
        	System.out.println("No picture");
        }
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

    public List<Item> searchFor(String query) {
        return this.items.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query,query);
    }

    public boolean isChangeable(Long id) {
        Item item = this.get(id);
        return item.noPeriodsAndRequests() && conflictService.noConflictWith(item);
    }
}
