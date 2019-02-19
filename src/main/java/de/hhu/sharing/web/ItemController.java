package de.hhu.sharing.web;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
public class ItemController {

    @Autowired
    private UserRepository users;

    @Autowired
    private ItemRepository items;

    @Autowired
    private ItemService itemService;

    @GetMapping("/detailsItem")
    public String details(@RequestParam(name = "id") Long id, Model model){
        Item item = itemService.get(id);
        User user = item.getLender();
        Address address = user.getAddress();
        model.addAttribute("item", item);
        model.addAttribute("user", user);
        model.addAttribute("address", address);
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
    public String saveItem(Long id, String name, String description, Integer rental, Integer deposit, Principal p, RedirectAttributes redirectAttributes){
        final User user = this.users.findByUsername(p.getName())
                .orElseThrow(
                        () -> new RuntimeException("User not found!"));
        Item item;
        if(id == null){
            item = new Item(name, description, rental, deposit, user);
            redirectAttributes.addFlashAttribute("saved",true);
            items.save(item);
            return "redirect:/account";
        }
        item = this.items.findById(id).orElseThrow(
                () -> new RuntimeException("Item not found!"));
        if(!item.isAvailable()){
            redirectAttributes.addFlashAttribute("notAvailable",true);
            return "redirect:/account";
        }
        item.setName(name);
        item.setDescription(description);
        item.setRental(rental);
        item.setDeposit(deposit);
        item.setLender(user);
        redirectAttributes.addFlashAttribute("edited",true);
        items.save(item);
        return "redirect:/account";

    }

    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        Item item = this.items.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Item not found!"));
        if(item.isAvailable()){
            items.delete(item);
            redirectAttributes.addFlashAttribute("deleted",true);
        }
        else{
            redirectAttributes.addFlashAttribute("notAvailable",true);
        }
        return "redirect:/account";
    }

}
