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

import java.security.Principal;
import java.time.LocalDate;

@Controller
public class RequestController {

    @Autowired
    private UserRepository users;

    @Autowired
    private ItemRepository items;

    @Autowired
    private RequestRepository requests;

    @GetMapping("/request")
    public String request(@RequestParam(name = "id") Long id, Model model){
        final Item item = this.items.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Item not found!"));
        model.addAttribute("item", item);
        return "request";
    }

    @PostMapping("/request")
    public String saveRequest(Long id, String startdate, String enddate, Principal p){
        final User user = this.users.findByUsername(p.getName())
                .orElseThrow(
                        () -> new RuntimeException("User not found!"));
        Item item = this.items.findById(id).orElseThrow(
                () -> new RuntimeException("Item not found!"));
        Request request = new Request(LocalDate.parse(startdate), LocalDate.parse(enddate), user);
        requests.save(request);
        item.addToRequests(request);
        items.save(item);
        return "redirect:/";
    }

    @GetMapping("/deleteRequest")
    public String deleteRequest(@RequestParam("id") Long id ){
        Request request = this.requests.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Request not found!"));
        requests.delete(request);
        return "redirect:/account";
    }

}
