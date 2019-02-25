package de.hhu.sharing.web;

import de.hhu.sharing.model.User;
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
    private TransactionRentalService transactionService;

    @GetMapping("/propayAccount")
    public String showProPayAccount(Model model, Principal p){
        User user = userService.get(p.getName());
        model.addAttribute("user", user);
        model.addAttribute("amount", proPayService.getAccount(user).getAmount());
        model.addAttribute("deposits", proPayService.getDepositSum(proPayService.getAccount(user)));
        model.addAttribute("send", transactionService.getAllFromSender(user));
        model.addAttribute("received", transactionService.getAllFromReceiver(user));
        return "propayAccount";
    }

    @PostMapping("/savePayIn")
    public String payMoneyIn(int sum, Principal p){
        User user = userService.get(p.getName());
        proPayService.rechargeCredit(user, sum);
        return "redirect:/propayAccount";
    }
}
