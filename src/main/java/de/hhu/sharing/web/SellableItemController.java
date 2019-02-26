package de.hhu.sharing.web;

import de.hhu.sharing.model.SellableItem;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.SellableItemService;
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
public class SellableItemController {

    @Autowired
    private SellableItemService sellableItemService;

    @Autowired
    private UserService userService;

    @GetMapping("/sellItem")
    private String sellItem(Model model){
        model.addAttribute("sellableItem", new SellableItem());
        return("sellItem");
    }

    @PostMapping("/saveSellItem")
    private String saveSellItem(Model model , Long id, @RequestParam("name") String name, @RequestParam("price") Integer price, @RequestParam("description") String description,  @RequestParam("file") MultipartFile file , Principal p, RedirectAttributes redirectAttributes){

            User user = userService.get(p.getName());
            if(id == null){
                sellableItemService.create(name, description, price, user, file);
                redirectAttributes.addFlashAttribute("saved",true);
            }
            else {
                sellableItemService.edit(id, name, description, price, user);
                redirectAttributes.addFlashAttribute("edited",true);
            }
            return "redirect:/account";

    }

}
