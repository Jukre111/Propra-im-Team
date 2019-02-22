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
    private TransactionService tranService;

    @Autowired
    private BorrowingProcessService processService;

    @GetMapping("/newRequest")
    public String newRequest(@RequestParam("id") Long id, Model model, Principal p, RedirectAttributes redirectAttributes){
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
    public String saveRequest(Long id, String startdate, String enddate, Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        Item item = itemService.get(id);
        if(!tranService.checkFinances(user, item, LocalDate.parse(startdate), LocalDate.parse(enddate))){
            redirectAttributes.addFlashAttribute("noCredit",true);
            return "redirect:/";
        }
        if(!itemService.checkAvailability(item, LocalDate.parse(startdate), LocalDate.parse(enddate))){
            redirectAttributes.addFlashAttribute("notAvailable",true);
            return "redirect:/newRequest?id=" + id;
        }
        requestService.create(id, LocalDate.parse(startdate), LocalDate.parse(enddate), user);
        redirectAttributes.addFlashAttribute("requested",true);
        return "redirect:/";
    }

    @GetMapping("/deleteRequest")
    public String deleteRequest(@RequestParam("id") Long id, Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        if(requestService.get(id).getRequester() != user){
            redirectAttributes.addFlashAttribute("notRequester",true);
            return "redirect:/messages";
        }
        requestService.delete(id);
        redirectAttributes.addFlashAttribute("deleted",true);
        return "redirect:/messages";
    }

    @GetMapping("/acceptRequest")
    public String acceptRequest(@RequestParam("requestId") Long requestId, @RequestParam("itemId") Long itemId, Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        if(itemService.getFromRequestId(requestId).getLender() != user){
            redirectAttributes.addFlashAttribute("notLender",true);
            return "redirect:/messages";
        }
        if(tranService.createTransaction(requestId, itemId) != 200) {
            redirectAttributes.addFlashAttribute("propayError",true);
            return "redirect:/messages";
        }
        processService.accept(requestId);
        redirectAttributes.addFlashAttribute("accepted",true);
        return "redirect:/messages";
    }

    @GetMapping("/declineRequest")
    public String declineRequest(@RequestParam("id") Long id , Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        if(itemService.getFromRequestId(id).getLender() != user){
            redirectAttributes.addFlashAttribute("notLender",true);
            return "redirect:/messages";
        }
        requestService.delete(id);
        redirectAttributes.addFlashAttribute("declined",true);
        return "redirect:/messages";
    }
}
