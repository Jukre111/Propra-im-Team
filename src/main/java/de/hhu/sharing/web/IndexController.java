package de.hhu.sharing.web;

import de.hhu.sharing.model.User;
import de.hhu.sharing.services.LendableItemService;
import de.hhu.sharing.services.RequestService;
import de.hhu.sharing.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private LendableItemService lendableItemService;

    @Autowired
    private RequestService requestService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("lendableItems", lendableItemService.getAll());
        return "index";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam String query) {
        model.addAttribute("lendableItems", lendableItemService.searchFor(query));
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/account")
    public String account(Model model, Principal p) {
        User user = userService.get(p.getName());
        model.addAttribute("user", user);
        model.addAttribute("lendItems", lendableItemService.getAllIPosted(user));
        model.addAttribute("address", user.getAddress());
        return "account";
    }

    @GetMapping("/messages")
    public String messages(Model model, Principal p){
        User user = userService.get(p.getName());
        requestService.deleteOutdatedRequests();
        model.addAttribute("user", user);
        model.addAttribute("allMyItems", lendableItemService.getAllIPosted(user));
        model.addAttribute("myRequestedItems", lendableItemService.getAllIRequested(user));
        return "messages";
    }
}
