package de.hhu.sharing.web;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    private UserRepository users;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/")
    public String index(Model model, Principal p) {

        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "index";
    }
    @GetMapping("/account")
    public String account(Model model, Principal p) {
        final User user = this.users.findByUsername(p.getName())
                .orElseThrow(
                        () -> new RuntimeException("User not found!"));
        model.addAttribute("user",user);
        return "account";
    }
    @GetMapping("/detail")
    public String detail(@RequestParam(name = "id") final String id, Model model){
        Long thisId = new Long(id);
        Optional<Item> item = itemRepository.findById(thisId);
        if(!item.isPresent()){
            throw new ItemNotFound();
        }
        User user = item.get().getLender();
        model.addAttribute("item", item.get());
        model.addAttribute("user", user);

        return "index";
    }


}
