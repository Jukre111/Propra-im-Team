package de.hhu.sharing.web;

import de.hhu.sharing.model.LendableItem;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.BorrowingProcessService;
import de.hhu.sharing.services.LendableItemService;
import de.hhu.sharing.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class LendableItemController {

    @Autowired
    private UserService userService;

    @Autowired
    private LendableItemService lendableItemService;

    @Autowired
    private BorrowingProcessService processService;

    @GetMapping("/lendableItemDetails")
    public String lendableItemDetails(@RequestParam(name = "id") Long id, Model model){
        LendableItem lendableItem = lendableItemService.get(id);
        User user = lendableItem.getOwner();
        model.addAttribute("lendableItem", lendableItem);
        model.addAttribute("user", user);
        model.addAttribute("allDates", lendableItemService.allDatesInbetween(lendableItem));
        return "lendableItemDetails";
    }

    @GetMapping("/newLendableItem")
    public String newLendableItem(Model model){
        model.addAttribute("lendableItem", new LendableItem());
        return "lendableItem";
    }

    @GetMapping("/editLendableItem")
    public String editLendableItem(Model model, @RequestParam("id") Long id, Principal p, RedirectAttributes redirectAttributes){
        if(!lendableItemService.isChangeable(id)){
            redirectAttributes.addFlashAttribute("notChangeable", true);
            return "redirect:/account";
        }
        if(lendableItemService.get(id).getOwner() != userService.get(p.getName())){
            redirectAttributes.addFlashAttribute("notAuthorized",true);
            return "redirect:/account";
        }
        model.addAttribute("lendableItem", lendableItemService.get(id));
        return "lendableItem";
    }

    @PostMapping("/saveLendableItem")
    public String saveLendableItem(Long id, @RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("rental") Integer rental, @RequestParam("deposit") Integer deposit, @RequestParam("file") MultipartFile file , Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        if(id == null){
            lendableItemService.create(name, description, rental, deposit, user, file);
            redirectAttributes.addFlashAttribute("saved",true);
        }
        else {
            lendableItemService.edit(id,name, description, rental, deposit, user);
            redirectAttributes.addFlashAttribute("edited",true);
        }
        return "redirect:/account";
    }

    @GetMapping("/deleteLendableItem")
    public String deleteLendableItem(@RequestParam("id") Long id, Principal p, RedirectAttributes redirectAttributes){
        if(!lendableItemService.isChangeable(id)){
            redirectAttributes.addFlashAttribute("notChangeable", true);
            return "redirect:/account";
        }
        if(lendableItemService.get(id).getOwner() != userService.get(p.getName())){
            redirectAttributes.addFlashAttribute("notAuthorized",true);
            return "redirect:/account";
        }
        lendableItemService.delete(id);
        redirectAttributes.addFlashAttribute("deleted",true);
        return "redirect:/account";
    }

    @GetMapping("/itemReturned")
    public String itemReturned(@RequestParam("id") Long id, Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        if(processService.get(id).getItem().getOwner() != user){
            redirectAttributes.addFlashAttribute("notAuthorized",true);
            return "redirect:/account";
        }
        processService.itemReturned(id, "good");
        redirectAttributes.addFlashAttribute("returned",true);
        return "redirect:/account";
    }
}
