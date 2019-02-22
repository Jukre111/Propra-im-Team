package de.hhu.sharing.web;

import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.Request;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.*;
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
    private TransactionService transactionService;

    @Autowired
    private ProPayService proPayService;

    @Autowired
    private BorrowingProcessService processService;

    @GetMapping("/newRequest")
    public String newRequest(@RequestParam("id") Long id, Model model, Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        if(itemService.isOwner(id, user)){
            redirectAttributes.addFlashAttribute("ownItem",true);
            return "redirect:/";
        }
        model.addAttribute("item", itemService.get(id));
        return "request";
    }

    @PostMapping("/saveRequest")
    public String saveRequest(Long id, String startdate, String enddate, Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        Item item = itemService.get(id);
        if(!proPayService.checkFinances(user, item, LocalDate.parse(startdate), LocalDate.parse(enddate))){
            redirectAttributes.addFlashAttribute("noCredit",true);
            return "redirect:/";
        }
        if(!itemService.isAvailableAt(item, LocalDate.parse(startdate), LocalDate.parse(enddate))){
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
        if(!requestService.isRequester(id, user)){
            redirectAttributes.addFlashAttribute("notRequester",true);
            return "redirect:/messages";
        }
        requestService.delete(id);
        redirectAttributes.addFlashAttribute("deleted",true);
        return "redirect:/messages";
    }

    @GetMapping("/acceptRequest")
    public String acceptRequest(@RequestParam("id") Long id, Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        Item item = itemService.getFromRequestId(id);
        Request request = requestService.get(id);
        if(!requestService.isLender(id, user)){
            redirectAttributes.addFlashAttribute("notLender",true);
            return "redirect:/messages";
        }
        if(requestService.isOutdated(id)){
            redirectAttributes.addFlashAttribute("outdatedRequest",true);
            return "redirect:/messages";
        }
        if(requestService.isOverlappingWithAvailability(id)){
            redirectAttributes.addFlashAttribute("overlappingRequest",true);
            return "redirect:/messages";
        }
        if(!proPayService.checkFinances(request.getRequester(), item, request.getPeriod().getStartdate(), request.getPeriod().getEnddate())){
            redirectAttributes.addFlashAttribute("noCredit",true);
            return "redirect:/messages";
        }
        processService.accept(id);
        redirectAttributes.addFlashAttribute("accepted",true);
        return "redirect:/messages";
    }

    @GetMapping("/declineRequest")
    public String declineRequest(@RequestParam("id") Long id , Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        if(!requestService.isLender(id, user)){
            redirectAttributes.addFlashAttribute("notLender",true);
            return "redirect:/messages";
        }
        requestService.delete(id);
        redirectAttributes.addFlashAttribute("declined",true);
        return "redirect:/messages";
    }
}
