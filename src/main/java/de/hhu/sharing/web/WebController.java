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
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    private UserRepository users;

    @Autowired
    private ItemRepository items;

    @GetMapping("/")
    public String index(Model model, Principal p) {
        model.addAttribute("items", this.items.findAll());
        return "index";
    }

    @GetMapping("/account")
    public String account(Model model, Principal p) {
        final User user = this.users.findByUsername(p.getName())
                .orElseThrow(
                        () -> new RuntimeException("User not found!"));
        model.addAttribute("user",user);
        LocalDate birthdate = user.getBirthdate();
        String thisBirthdate = birthdate.toString();
        model.addAttribute("birthdate", thisBirthdate);
        return "account";
    }

    @GetMapping("/details")
    public String details(@RequestParam(name = "id") final String id, Model model){
        final Item item = this.items.findById(Long.valueOf(id))
                .orElseThrow(
                        () -> new RuntimeException("Item not found!"));
        model.addAttribute("item", item);
        return "details";
    }

    @GetMapping("/item")
    public String item(Model model, @RequestParam("id") Long id){
        Item newItem = new Item();
        newItem.setId(new Long(-1));
        if(id == -1){
            model.addAttribute("item", newItem);
        }
        else{
            Optional<Item> it = items.findById(id);
            if(it.isPresent()){
                model.addAttribute("item", it.get());
            }
            else{
                model.addAttribute("item", newItem);
            }
        }
        return "item";
    }

    @PostMapping("/saveItem")
    public String saveItem(Model model, Long id, String name, String description, int rental, int deposit, Principal p){
        Item newItem = new Item();
        final User user = this.users.findByUsername(p.getName())
                .orElseThrow(
                        () -> new RuntimeException("User not found!"));
        if(id!=-1){
            newItem.setId(id);
        }
        newItem.setName(name);
        newItem.setDescription(description);
        newItem.setRental(rental);
        newItem.setDeposit(deposit);
        newItem.setLender(user);

        items.save(newItem);
        return "item";

    }


}
