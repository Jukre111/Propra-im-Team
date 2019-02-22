package de.hhu.sharing.services;

import de.hhu.sharing.data.BorrowingProcessRepository;
import de.hhu.sharing.data.TransactionRepository;
import de.hhu.sharing.model.BorrowingProcess;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.Request;
import de.hhu.sharing.model.User;
import de.hhu.sharing.propay.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BorrowingProcessService {

    @Autowired
    private BorrowingProcessRepository processes;

    @Autowired
    private RequestService requestService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProPayService proService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transRepo;

    public BorrowingProcess get(Long id) {
        BorrowingProcess process = this.processes.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Process not found!"));
        return process;
    }

    public Item getItemFromProcess(BorrowingProcess process){
        Item item = process.getItem();
        return item;
    }

    public void accept(Long requestId) {
        Request request = requestService.get(requestId);
        Item item = itemService.getFromRequestId(requestId);
        item.addToPeriods(request.getPeriod());
        this.createProcess(item, request);
        requestService.deleteOverlappingRequestsFromItem(request, item);
    }

    private void createProcess(Item item, Request request) {
        BorrowingProcess process = new BorrowingProcess(item, request.getPeriod());
        processes.save(process);
        User borrower = request.getRequester();
        User lender = item.getLender();
        transactionService.createTransaction(process, borrower, lender);
        borrower.addToBorrowed(process);
        lender.addToLend(process);
    }

    public void returnItem(Long processId, User lender){
        BorrowingProcess process = this.get(processId);
        User borrower = userService.getBorrowerFromBorrowingProcessId(processId);

        proService.cancelDeposit(borrower,transRepo.findByProcessId(processId));
        userService.removeProcessFromProcessLists(lender, process);
        process.getItem().removeFromPeriods(process.getPeriod());
        processes.delete(process);

    }
}
