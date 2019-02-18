package de.hhu.sharing.web;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.Request;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class RequestController {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RequestService requestService;

    @GetMapping("/request")
    public String request(@RequestParam(name = "id") Long id, Model model, Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        Item item = itemService.get(id);
        if(item.getLender() == user){
            redirectAttributes.addFlashAttribute("ownItem",true);
            return "redirect:/";
        }
        model.addAttribute("item", item);
        return "request";
    }

    @PostMapping("/saveRequest")
    public String saveRequest(Long id, String startdate, String enddate, Principal p){
        User user = userService.get(p.getName());
        requestService.create(id, LocalDate.parse(startdate), LocalDate.parse(enddate), user);
        return "redirect:/";
    }

    @GetMapping("/deleteRequest")
    public String deleteRequest(@RequestParam("requestId") Long requestId, @RequestParam("itemId") Long itemId){
        requestService.delete(requestId);
        return "redirect:/account";
    }

    @GetMapping("/messages")
    public String messages(Model model, Principal p){
        User user = userService.get(p.getName());
        model.addAttribute("allMyItems", itemService.getAllIPosted(user));
        model.addAttribute("allIRequested", itemService.getAllIRequested(user));
        model.addAttribute("myRequestedItems", itemService.getAllMyRequested(user));
        return "messages";
    }

    @GetMapping("/accept")
    public String accept(@RequestParam("requestId") Long requestId, @RequestParam("itemId") Long itemId, RedirectAttributes redirectAttributes){
        requestService.accept(requestId, redirectAttributes);
        return "redirect:/messages";
    }

    @GetMapping("/declineRequest")
    public String declineRequest(@RequestParam("requestId") Long requestId, @RequestParam("itemId") Long itemId ){
        requestService.delete(requestId);
        return "redirect:/messages";
    }
}
