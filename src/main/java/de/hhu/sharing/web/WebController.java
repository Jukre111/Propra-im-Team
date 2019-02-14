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
    public String item(@RequestParam(name  = "id") Long id, Model model ){

        Item item = new Item();
        
        return "item";
    }


}
