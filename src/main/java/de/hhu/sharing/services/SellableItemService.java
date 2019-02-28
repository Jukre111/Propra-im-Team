package de.hhu.sharing.services;

import de.hhu.sharing.data.SellableItemRepository;
import de.hhu.sharing.model.LendableItem;
import de.hhu.sharing.model.SellableItem;
import de.hhu.sharing.model.User;
import de.hhu.sharing.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class SellableItemService {

    @Autowired
    private SellableItemRepository items;

    @Autowired
    private StorageService storageService;

    public void create(String name, String description, Integer price, User user, MultipartFile file) {
        SellableItem sellableItem = new SellableItem(name, description, price, user);
        items.save(sellableItem);
        if(file!=null) {
            storageService.storeSellableItem(file, sellableItem);
        }else {
            System.out.println("No picture");
        }
    }

    public void edit(Long id, String name, String description, Integer price, User user,MultipartFile file) {
        SellableItem sellableItem = this.get(id);
        sellableItem.setName(name);
        sellableItem.setDescription(description);
        sellableItem.setPrice(price);
        sellableItem.setOwner(user);
        items.save(sellableItem);
        if(file!=null) {
            storageService.storeSellableItem(file, sellableItem);
        }else {
            System.out.println("No picture");
        }
    }

    public SellableItem get(Long id){
        return this.items.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Objekt nicht gefunden!"));
    }

    public void delete(Long id) {
        SellableItem sellableItem = this.get(id);
        items.delete(sellableItem);
    }

    public List<SellableItem> getAll() {
        return this.items.findAll();
    }

    public List<SellableItem> getAllIPosted(User owner){
        return this.items.findAllByOwner(owner);
    }

    public List<SellableItem> searchFor(String query) {
        return this.items.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query,query);
    }
}
