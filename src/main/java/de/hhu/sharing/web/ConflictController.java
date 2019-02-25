package de.hhu.sharing.web;

import de.hhu.sharing.model.BorrowingProcess;
import de.hhu.sharing.model.Conflict;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String conflict(Model model, @RequestParam("id") Long id, Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        BorrowingProcess process = borrowingProcessService.get(id);
        if(!userService.userIsInvolvedToProcess(user, process)){
            redirectAttributes.addFlashAttribute("notAuthorized",true);
            return "redirect:/account";
        }
        Conflict conflict = conflictService.getFromBorrowindProcess(process);
        if(conflict != null){
            return "redirect:/conflictDetails?id=" + conflict.getId();
        }
        model.addAttribute( "borrowingProcess", process);
        model.addAttribute("borrower", userService.getBorrowerFromBorrowingProcessId(id));
        return "conflictNew";
    }

    @PostMapping("/saveConflict")
    public String saveConflict(@RequestParam("id") Long id, String problem, Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        BorrowingProcess process = borrowingProcessService.get(id);
        if(!userService.userIsInvolvedToProcess(user, process)){
            redirectAttributes.addFlashAttribute("notAuthorized",true);
            return "redirect:/account";
        }
        Conflict conflict = conflictService.getFromBorrowindProcess(process);
        if(conflict != null){
            redirectAttributes.addFlashAttribute("conflictExistsAlready",true);
            return "redirect:/conflictDetails?id=" + conflict.getId();
        }
        conflictService.create(process.getItem().getLender(), userService.getBorrowerFromBorrowingProcessId(id), process, user, problem);
        return "redirect:/account";
    }

    @PostMapping("/conflictNewMessage")
    public String addMessageToConflict(@RequestParam("id") Long id, String message, Principal p, RedirectAttributes redirectAttributes){
        User user = userService.get(p.getName());
        Conflict conflict = conflictService.get(id);
        if(!userService.userIsInvolvedToProcess(user, conflict.getProcess())){
            redirectAttributes.addFlashAttribute("notAuthorized",true);
            return "redirect:/account";
        }
        conflictService.addToMessages(conflict, user, message);
        return "redirect:/conflictDetails?id=" + conflict.getId();
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
