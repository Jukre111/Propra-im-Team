package de.hhu.sharing.web;

import de.hhu.sharing.data.TransactionRepository;
import de.hhu.sharing.model.User;
import de.hhu.sharing.propay.Account;
import de.hhu.sharing.services.TransactionService;
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
    private TransactionService transactionService;

    @GetMapping("/propayAccount")
    public String showProPayAccount(Model model, Principal p){
        User user = userService.get(p.getName());
        model.addAttribute("user", user);
        model.addAttribute("amount", proPayService.getAccount(user).getAmount());
        model.addAttribute("send", transactionService.getAllFromSender(user));
        model.addAttribute("received", transactionService.getAllFromReceiver(user));
        return "propayAccount";
    }

    @PostMapping("/savePayIn")
    public String payMoneyIn(int sum, Principal p){
        User user = userService.get(p.getName());
        proPayService.raiseBalance(user, sum);
        return "redirect:/propayAccount";
    }
}
