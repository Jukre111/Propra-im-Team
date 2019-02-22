package de.hhu.sharing.web;

import de.hhu.sharing.data.TransactionRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.propay.Account;
import de.hhu.sharing.propay.Transaction;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import de.hhu.sharing.services.ProPayService;

@Controller
public class ProPayController {
    @Autowired
    TransactionRepository transRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    ProPayService proPay;

    @GetMapping("/propayAccount")
    public String showProPayAccount(Model model, Principal p) {

        final User user = this.userRepo.findByUsername(p.getName()).orElseThrow(
                ()-> new RuntimeException("User not found"));
        String username = user.getUsername();

        model.addAttribute("user", user);
        Account account = proPay.showAccount(username);
        model.addAttribute("amount", account.getAmount());

        List<Transaction> sendMoney = transRepo.findBySource(user);
        model.addAttribute("send", sendMoney);

        List<Transaction> receivedMoney = transRepo.findByTarget(user);
        model.addAttribute("receive", receivedMoney);
        return "propayAccount";
    }

    @PostMapping("/savePayIn")
    public String payMoneyIn(Principal p, int sum) {

        final User user = this.userRepo.findByUsername(p.getName()).orElseThrow(
                ()-> new RuntimeException("User not found"));
        String username = user.getUsername();
        proPay.raiseBalance(username, sum);

        return "redirect:/propayAccount";
    }
}
