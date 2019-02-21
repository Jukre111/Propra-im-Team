package de.hhu.sharing.web;

import de.hhu.sharing.model.BorrowingProcess;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.BorrowingProcessService;
import de.hhu.sharing.services.ItemService;
import de.hhu.sharing.services.RequestService;
import de.hhu.sharing.services.TransactionService;
import de.hhu.sharing.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;

@Controller
public class RequestController {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RequestService requestService;

    @Autowired
    TransactionService tranService;

    @Autowired
    private BorrowingProcessService processService;

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
        return "redirect:/messages";
    }

    @GetMapping("/accept")
    public String accept(@RequestParam("requestId") Long requestId, @RequestParam("itemId") Long itemId, RedirectAttributes redirectAttributes){
//        if(!item.isAvailable()) {
//            redirectAttributes.addFlashAttribute("notAvailable",true);
//        return "redirect:/messages";
//        }
        int transProPayResponse = tranService.createTransaction(requestId, itemId);
        if(transProPayResponse != 200) {
            return "redirect:/messages";
        } else {
           processService.accept(requestId);
           redirectAttributes.addFlashAttribute("itemAccepted",true);
        }
        return "redirect:/messages";
    }

    @GetMapping("/declineRequest")
    public String declineRequest(@RequestParam("requestId") Long requestId, @RequestParam("itemId") Long itemId ){
        requestService.delete(requestId);
        return "redirect:/messages";
    }
}
