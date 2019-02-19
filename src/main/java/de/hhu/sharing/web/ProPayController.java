package de.hhu.sharing.web;

import de.hhu.sharing.data.TransactionRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Account;
import de.hhu.sharing.model.Transaction;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import de.hhu.sharing.services.ProPayService;
import java.util.Optional;

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
        //User user2 = userRepo.findByUsername("user2").get();

        model.addAttribute("user", user);
        Account account = proPay.showAccount(username);
        model.addAttribute("amount", account.getAmount());

        /*Transaction trans1 = new Transaction();
        Transaction trans2 = new Transaction();
        Transaction trans3 = new Transaction();
        trans1.setSource(user1);
        trans1.setTarget(user2);
        trans2.setSource(user1);
        trans2.setTarget(user2);
        trans3.setSource(user2);
        trans3.setTarget(user1);

        transRepo.save(trans1);
        transRepo.save(trans2);
        transRepo.save(trans3);*/

        List<Transaction> sendMoney = transRepo.findBySource(user);
        model.addAttribute("send", sendMoney);

        List<Transaction> receivedMoney = transRepo.findByTarget(user);
        model.addAttribute("receive", receivedMoney);
        //Transaction transaction = new Transaction();
        //model.addAttribute("transactions", transaction);
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
