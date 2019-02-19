package de.hhu.sharing.web;

import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import de.hhu.sharing.data.ItemRepository;
import java.security.Principal;


@Controller
public class ConflictController {

    @Autowired
    private ItemRepository items;

    @Autowired
    private UserRepository users;


    @GetMapping("/conflictPage")
    public String conflictPage(Model model, Principal p, @RequestParam("id") Long id){
        final Item item = this.items.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Item not found!"));

        model.addAttribute("item", items);


        return "redirect:/account";

    }
}
