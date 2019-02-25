package de.hhu.sharing.web;

import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.lendableItem;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.BorrowingProcessService;
import de.hhu.sharing.services.ItemService;
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
import java.time.LocalDate;
import java.util.List;

@Controller
public class ItemController {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BorrowingProcessService processService;

    @GetMapping("/detailsItem")
    public String details(@RequestParam(name = "id") Long id, Model model){
        lendableItem lendableItem = itemService.get(id);
        User user = lendableItem.getOwner();
        Address address = user.getAddress();
        List <LocalDate> allDates = itemService.allDatesInbetween(lendableItem);
        model.addAttribute("item", lendableItem);
        model.addAttribute("user", user);
        model.addAttribute("address", address);
        model.addAttribute("allDates", allDates);

        return "details";
    }

    @GetMapping("/newItem")
    public String newItem(Model model){
        model.addAttribute("item", new lendableItem());
        return "item";
    }

    @GetMapping("/editItem")
    public String editItem(Model model, @RequestParam("id") Long id, Principal p, RedirectAttributes redirectAttributes){
        if(!itemService.isChangeable(id)){
            redirectAttributes.addFlashAttribute("notChangeable", true);
            return "redirect:/account";
        }
        if(itemService.get(id).getOwner() != userService.get(p.getName())){
            redirectAttributes.addFlashAttribute("notAuthorized",true);
            return "redirect:/account";
        }
        model.addAttribute("item", itemService.get(id));
        return "item";
    }

    @PostMapping("/saveItem")
    public String saveItem(Long id, @RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("rental") Integer rental, @RequestParam("deposit") Integer deposit, @RequestParam("file") MultipartFile file , Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        if(id == null){
            itemService.create(name, description, rental, deposit, user, file);
            redirectAttributes.addFlashAttribute("saved",true);
        }
        else {
            itemService.edit(id,name, description, rental, deposit, user);
            redirectAttributes.addFlashAttribute("edited",true);
        }
        return "redirect:/account";
    }

    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("id") Long id, Principal p, RedirectAttributes redirectAttributes){
        if(!itemService.isChangeable(id)){
            redirectAttributes.addFlashAttribute("notChangeable", true);
            return "redirect:/account";
        }
        if(itemService.get(id).getOwner() != userService.get(p.getName())){
            redirectAttributes.addFlashAttribute("notAuthorized",true);
            return "redirect:/account";
        }
        itemService.delete(id);
        redirectAttributes.addFlashAttribute("deleted",true);
        return "redirect:/account";
    }

    @GetMapping("/itemReturned")
    public String itemReturned(@RequestParam("id") Long id, Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        if(processService.get(id).getLendableItem().getOwner() != user){
            redirectAttributes.addFlashAttribute("notAuthorized",true);
            return "redirect:/account";
        }
        processService.itemReturned(id, "good");
        redirectAttributes.addFlashAttribute("returned",true);
        return "redirect:/account";
    }
}
