package de.hhu.sharing.web;

import de.hhu.sharing.model.User;
import de.hhu.sharing.propay.Account;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProPayController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProPayService proPayService;

    @Autowired
    private TransactionRentalService transactionRentalService;

    @Autowired
    private TransactionPurchaseService transactionPurchaseService;

    @GetMapping("/propayAccount")
    public String proPayAccount(Model model, Principal p){
        User user = userService.get(p.getName());
        Account account = proPayService.getAccount(user);
        model.addAttribute("user", user);
        model.addAttribute("amount", account.getAmount());
        model.addAttribute("deposits", proPayService.getDepositSum(account));
        model.addAttribute("send", transactionRentalService.getAllFromSender(user));
        model.addAttribute("received", transactionRentalService.getAllFromReceiver(user));
        model.addAttribute("sendPurchase", transactionPurchaseService.getAllFromSender(user));
        model.addAttribute("receivedPurchase", transactionPurchaseService.getAllFromReceiver(user));
        return "propayAccount";
    }

    @PostMapping("/savePayIn")
    public String savePayIn(int sum, Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        proPayService.rechargeCredit(user, sum);
        redirectAttributes.addFlashAttribute("succMessage", sum + "€ aufgeladen.");
        return "redirect:/propayAccount";
    }
}
