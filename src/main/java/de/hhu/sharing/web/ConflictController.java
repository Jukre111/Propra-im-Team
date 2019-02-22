package de.hhu.sharing.web;

import de.hhu.sharing.model.BorrowingProcess;
import de.hhu.sharing.model.Conflict;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;

@Controller
public class ConflictController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConflictService conflictService;

    @Autowired
    private BorrowingProcessService borrowingProcessService;

    @Autowired
    private ProPayService proService;

    @Autowired
    private  TransactionService transactionService;

    @GetMapping("/conflict")
    public String conflictPage(Model model, Principal p, @RequestParam("id") Long id){

        BorrowingProcess borrowingProcess = borrowingProcessService.get(id);
        model.addAttribute( "borrowingProcess", borrowingProcess);
        User borrower = userService.getBorrowerFromBorrowingProcessId(id);
        model.addAttribute("borrower", borrower);
        return "conflict";
    }

    @PostMapping("/saveConflict")
    public String saveConflict(@RequestParam("id") Long id, String problem){
        BorrowingProcess process = borrowingProcessService.get(id);
        Item item =  process.getItem();
        conflictService.create(problem, item, item.getLender(), userService.getBorrowerFromBorrowingProcessId(id), process);
        return "redirect:/account";
    }

    @GetMapping("/conflictView")
    public String conflictView(Model model){
        model.addAttribute("allConflicts", conflictService.getAll());
        return "conflictView";
    }

    @GetMapping("/conflictDetails")
    public String conflictDetails (Model model, @RequestParam("id") Long id){
        model.addAttribute("conflict", conflictService.get(id));
        return "conflictDetails";
    }

    @GetMapping("/borrower")
    public String borrower(@RequestParam("id") Long id){
        Conflict conflict = conflictService.get(id);
        User borrower = conflict.getBorrower();
        User owner = conflict.getOwner();

        BorrowingProcess process = conflict.getProcess();

        proService.releaseDeposit(borrower, transactionService.getFromProcessId(process.getId()));
        conflictService.removeConflict(conflict);
        borrowingProcessService.returnItem(process.getId(), owner);


        return "redirect:/conflictView";
    }


    @GetMapping("/owner")
    public String owner(@RequestParam("id") Long id){
        Conflict conflict = conflictService.get(id);
        User owner = conflict.getOwner();
        User borrower = conflict.getBorrower();

        BorrowingProcess process = conflict.getProcess();

        proService.punishDeposit(borrower, transactionService.getFromProcessId(process.getId()));
        conflictService.removeConflict(conflict);
        borrowingProcessService.returnItem(process.getId(), owner);

        return "redirect:/conflictView";
    }
}
