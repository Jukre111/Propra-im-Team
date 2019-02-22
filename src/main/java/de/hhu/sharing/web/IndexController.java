package de.hhu.sharing.web;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.ItemService;
import de.hhu.sharing.services.RequestService;
import de.hhu.sharing.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RequestService requestService;

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

    @GetMapping("/messages")
    public String messages(Model model, Principal p){
        User user = userService.get(p.getName());
        requestService.deleteOutdatedRequests();
        model.addAttribute("user", user);
        model.addAttribute("allMyItems", itemService.getAllIPosted(user));
        model.addAttribute("myRequestedItems", itemService.getAllIRequested(user));
        return "messages";
    }

    @GetMapping("/search")
    public String search(@RequestParam final String query, Model model) {
        model.addAttribute("items", itemService.searchFor(query));
        model.addAttribute("query", query);
        return "search";
    }
}
