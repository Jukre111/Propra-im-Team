package de.hhu.sharing.web;

import de.hhu.sharing.model.LendableItem;
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
import java.util.List;

@Controller
public class RequestController {

    @Autowired
    private UserService userService;

    @Autowired
    private LendableItemService lendableItemService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private ProPayService proPayService;

    @Autowired
    private BorrowingProcessService processService;

    @GetMapping("/newRequest")
    public String newRequest(@RequestParam("id") Long id, Model model, Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        if(lendableItemService.isOwner(id, user)){
            redirectAttributes.addFlashAttribute("errMessage","Eigenes Objekt nicht an sich selbst verleihbar.");
            return "redirect:/";
        }
        List<LocalDate> allDates = lendableItemService.allDatesInbetween(lendableItemService.get(id));
        model.addAttribute("allDates", allDates);
        model.addAttribute("lendableItem", lendableItemService.get(id));
        return "request";
    }

    @PostMapping("/saveRequest")
    public String saveRequest(Long id, String startdate, String enddate, Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        LendableItem lendableItem = lendableItemService.get(id);
        if(lendableItemService.isOwner(id, user)){
            redirectAttributes.addFlashAttribute("errMessage","Eigenes Objekt nicht an sich selbst verleihbar.");
            return "redirect:/";
        }
        if(!LocalDate.parse(startdate).isBefore(LocalDate.parse(enddate)) && !LocalDate.parse(startdate).equals(LocalDate.parse(enddate))) {
            redirectAttributes.addFlashAttribute("errMessage", "Startdatum muss vor dem Enddatum sein");
            return "redirect:/newRequest?=" + id;
        }
        if(!proPayService.enoughCredit(user, lendableItem, LocalDate.parse(startdate), LocalDate.parse(enddate))){
            redirectAttributes.addFlashAttribute("errMessage","Nicht genug ProPay-Guthaben.");
            return "redirect:/";
        }
        if(!lendableItemService.isAvailableAt(lendableItem, LocalDate.parse(startdate), LocalDate.parse(enddate))){
            redirectAttributes.addFlashAttribute("errMessage","Objekt zum angegebenen Zeitraum nicht verfügbar.");
            return "redirect:/newRequest?id=" + id;
        }
        requestService.create(id, LocalDate.parse(startdate), LocalDate.parse(enddate), user);
        redirectAttributes.addFlashAttribute("succMessage","Anfrage erfolgreich gestellt.");
        return "redirect:/";
    }

    @GetMapping("/deleteRequest")
    public String deleteRequest(@RequestParam("id") Long id, Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        if(!requestService.isRequester(id, user)){
            redirectAttributes.addFlashAttribute("errMessage","Keine Berechtigung.");
            return "redirect:/messages";
        }
        requestService.delete(id);
        redirectAttributes.addFlashAttribute("succMessage","Anfrage gelöscht.");
        return "redirect:/messages";
    }

    @GetMapping("/acceptRequest")
    public String acceptRequest(@RequestParam("id") Long id, Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        LendableItem lendableItem = lendableItemService.getFromRequestId(id);
        Request request = requestService.get(id);
        if(!requestService.isLender(id, user)){
            redirectAttributes.addFlashAttribute("errMessage","Keine Berechtigung.");
            return "redirect:/messages";
        }
        if(requestService.isOutdated(id)){
            redirectAttributes.addFlashAttribute("errMessage","Anfrage-Zeitraum liegt in der Vergangenheit.");
            return "redirect:/messages";
        }
        if(requestService.isOverlappingWithAvailability(id)){
            redirectAttributes.addFlashAttribute("errMessage", "Anfrage-Zeitraum überschneidet sich mit Objektverfügbarkeit.");
            return "redirect:/messages";
        }
        if(!proPayService.enoughCredit(request.getRequester(), lendableItem, request.getPeriod().getStartdate(), request.getPeriod().getEnddate())){
            redirectAttributes.addFlashAttribute("errMessage","Anfragensteller hat nicht genug ProPay-Guthaben.");
            return "redirect:/messages";
        }
        processService.accept(id);
        redirectAttributes.addFlashAttribute("succMessage","Objekt verliehen. Überschneidende Anfragen wurden gelöscht.");
        return "redirect:/messages";
    }

    @GetMapping("/declineRequest")
    public String declineRequest(@RequestParam("id") Long id , Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        if(!requestService.isLender(id, user)){
            redirectAttributes.addFlashAttribute("errMessage","Keine Berechtigung.");
            return "redirect:/messages";
        }
        requestService.delete(id);
        redirectAttributes.addFlashAttribute("succMessage","Anfrage abgelehnt.");
        return "redirect:/messages";
    }
}
