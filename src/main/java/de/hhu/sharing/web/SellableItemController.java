package de.hhu.sharing.web;

import de.hhu.sharing.data.TransactionPurchaseRepository;
import de.hhu.sharing.model.SellableItem;
import de.hhu.sharing.model.User;
import de.hhu.sharing.propay.TransactionPurchase;
import de.hhu.sharing.services.ProPayService;
import de.hhu.sharing.services.SellableItemService;
import de.hhu.sharing.services.TransactionPurchaseService;
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

    @Autowired
    private ProPayService proService;

    @Autowired
    private TransactionPurchaseService transPurService;

    @Autowired
    private TransactionPurchaseRepository transPurRepo;

    @GetMapping("/sellItemDetails")
    private String sellItemDetails(Model model, @RequestParam("id") Long id){
        SellableItem sellItem = sellableItemService.get(id);
        User user = sellItem.getOwner();
        model.addAttribute("user", user);
        model.addAttribute("sellableItem", sellItem);

        return ("sellItemDetails");
    }




    @GetMapping("/sellItem")
    private String sellItem(Model model){
        model.addAttribute("sellableItem", new SellableItem());
        return("sellItem");
    }


    @GetMapping("/editSellItem")
    public String editSellItem(Model model, @RequestParam("id") Long id, Principal p, RedirectAttributes redirectAttributes){

        if(sellableItemService.get(id).getOwner() != userService.get(p.getName())){
            redirectAttributes.addFlashAttribute("notAuthorized",true);
            return "redirect:/account";
        }
        model.addAttribute("sellableItem", sellableItemService.get(id));
        return "sellItem";
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

    @GetMapping("/deleteSellItem")
    public String deleteSellItem(@RequestParam("id") Long id, Principal p, RedirectAttributes redirectAttributes){

        if(sellableItemService.get(id).getOwner() != userService.get(p.getName())){
            redirectAttributes.addFlashAttribute("notAuthorized",true);
            return "redirect:/account";
        }
        sellableItemService.delete(id);
        redirectAttributes.addFlashAttribute("deleted",true);
        return "redirect:/account";
    }

    @GetMapping("/buy")
    public String buy(@RequestParam("id") Long id, Principal p){
        User buyer = userService.get(p.getName());
        SellableItem sellableItem = sellableItemService.get(id);
        User owner = sellableItem.getOwner();

        // TODO something brilliant

        if(!proService.enoughCredit(buyer,sellableItem)){
            //Popup-message that buyer has not enough money 
            return ("redirect:/");
        }

        transPurService.createTransactionPurchase(sellableItem, owner, buyer);
        TransactionPurchase transPur = transPurRepo.findByItemId(sellableItem.getId());
        proService.initiateTransactionPurchase(transPur);

        sellableItemService.delete(id);

        return ("redirect:/");
    }

}
