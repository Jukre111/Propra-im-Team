package de.hhu.sharing.web;

import de.hhu.sharing.model.User;
import de.hhu.sharing.services.TransactionPurchaseService;
import de.hhu.sharing.services.TransactionRentalService;
import de.hhu.sharing.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

import de.hhu.sharing.services.ProPayService;

@Controller
public class ProPayController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProPayService proPayService;

    @Autowired
    private TransactionRentalService transRenService;

    @Autowired
    private TransactionPurchaseService transPurService;

    @GetMapping("/propayAccount")
    public String showProPayAccount(Model model, Principal p){
        User user = userService.get(p.getName());
        model.addAttribute("user", user);
        model.addAttribute("amount", proPayService.getAccount(user).getAmount());
        model.addAttribute("deposits", proPayService.getDepositSum(proPayService.getAccount(user)));
        model.addAttribute("send", transRenService.getAllFromSender(user));
        model.addAttribute("received", transRenService.getAllFromReceiver(user));
        model.addAttribute("receivedPurchase",transPurService.getAllFromReceiver(user));
        model.addAttribute("sendPurchase", transPurService.getAllFromSender(user));
        return "propayAccount";
    }

    @PostMapping("/savePayIn")
    public String payMoneyIn(int sum, Principal p){
        User user = userService.get(p.getName());
        proPayService.rechargeCredit(user, sum);
        return "redirect:/propayAccount";
    }
}
