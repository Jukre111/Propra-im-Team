package de.hhu.sharing.web;

import de.hhu.sharing.model.*;
import de.hhu.sharing.services.LendableItemService;
import de.hhu.sharing.services.RequestService;
import de.hhu.sharing.services.SellableItemService;
import de.hhu.sharing.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private LendableItemService lendableItemService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private SellableItemService sellableItemService;


    @GetMapping("/")
    public String index(Model model, RedirectAttributes redirectAttributes, Principal p) {
        List<SellableItem> sellItems = sellableItemService.getAll();
        model.addAttribute("lendableItems", lendableItemService.getAll());
        model.addAttribute("sellItems", sellItems);
        User user = userService.get(p.getName());
        if(userService.userHasNotReturnedItems(user)){
            redirectAttributes.addFlashAttribute("returnYourItems", true);
            return "redirect:/";
        }

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
        model.addAttribute("lendLendableItems", lendableItemService.getAllIPosted(user));
        model.addAttribute("address", user.getAddress());
        model.addAttribute("sellItems", sellableItemService.getMySellables(user));
        return "account";
    }

    @GetMapping("/messages")
    public String messages(Model model, Principal p){
        User user = userService.get(p.getName());
        requestService.deleteOutdatedRequests();
        model.addAttribute("user", user);
        model.addAttribute("allMyLendableItems", lendableItemService.getAllIPosted(user));
        model.addAttribute("myRequestedLendableItems", lendableItemService.getAllIRequested(user));
        return "messages";
    }
}
