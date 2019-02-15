package de.hhu.sharing.web;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
public class ItemController {

    @Autowired
    private UserRepository users;

    @Autowired
    private ItemRepository items;

    @GetMapping("/details")
    public String details(@RequestParam(name = "id") Long id, Model model){
        final Item item = this.items.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Item not found!"));
        model.addAttribute("item", item);
        return "details";
    }

    @GetMapping("/item")
    public String item(Model model, @RequestParam("id") Optional<Long> id){
        Item item;
        item = id.map(aLong -> this.items.findById(aLong)
                .orElseThrow(
                        () -> new RuntimeException("Item not found!"))).orElseGet(Item::new);
        model.addAttribute("item",item);
        return "item";
    }

    @PostMapping("/saveItem")
    public String saveItem(Long id, String name, String description, Integer rental, Integer deposit, Principal p){
        Item item;
        if(id != null){
            item = this.items.findById(id).orElseThrow(
                    () -> new RuntimeException("Item not found!"));
        }
        else{
            item = new Item();
        }
        final User user = this.users.findByUsername(p.getName())
                .orElseThrow(
                        () -> new RuntimeException("User not found!"));

        item.setName(name);
        item.setDescription(description);
        item.setRental(rental);
        item.setDeposit(deposit);
        item.setLender(user);

        items.save(item);

        return "redirect:/";

    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id ){
        Item item = this.items.findById(id).orElseThrow(
                () -> new RuntimeException("Item not found!"));

        items.delete(item);
        return "redirect:/account";
    }

}
