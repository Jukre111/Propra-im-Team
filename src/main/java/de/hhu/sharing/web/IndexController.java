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

import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("items", itemService.getAll());
        return "index";
    }

    @GetMapping("/account")
    public String account(Model model, Principal p) {
        User user = userService.get(p.getName());
        model.addAttribute("user", user);
        model.addAttribute("lendItems", itemService.getAllIPosted(user));
        model.addAttribute("address", user.getAddress());
        return "account";
    }


    @GetMapping("/search")
    public String search(@RequestParam final String query, Model model) {
        model.addAttribute("items", itemService.searchFor(query));
        model.addAttribute("query", query);
        return "search";
    }
}
