package de.hhu.sharing.services;

import de.hhu.sharing.data.BorrowingProcessRepository;
import de.hhu.sharing.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BorrowingProcessService {

    @Autowired
    private BorrowingProcessRepository processes;

    @Autowired
    private RequestService requestService;

    @Autowired
    private LendableItemService lendableItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProPayService proPayService;

    @Autowired
    private TransactionRentalService transactionRentalService;

    @Autowired
    private  ConflictService conflictService;

    public BorrowingProcess get(Long id) {
        return this.processes.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Ausleihprozess nicht gefunden!"));
    }

    public void accept(Long requestId) {
        Request request = requestService.get(requestId);
        LendableItem lendableItem = lendableItemService.getFromRequestId(requestId);
        lendableItem.addToPeriods(request.getPeriod());
        this.createProcess(lendableItem, request);
        requestService.deleteOverlappingRequestsFromItem(request, lendableItem);
    }

    private void createProcess(LendableItem lendableItem, Request request) {
        BorrowingProcess process = new BorrowingProcess(lendableItem, request.getPeriod());
        processes.save(process);
        User borrower = request.getRequester();
        User lender = lendableItem.getOwner();
        transactionRentalService.createTransactionRental(process, borrower, lender);
        borrower.addToBorrowed(process);
        lender.addToLend(process);
    }

    public void itemReturned(Long processId, String condition){
        BorrowingProcess process = this.get(processId);
        Conflict conflict = conflictService.getFromBorrowingProcess(process);
        if(conflict != null){
            conflictService.delete(conflict);
        }
        User borrower = userService.getBorrowerFromBorrowingProcessId(processId);
        if(condition.equals("good")){
            proPayService.releaseDeposit(borrower, transactionRentalService.getFromProcessId(processId));
        }
        else{
            proPayService.punishDeposit(borrower, transactionRentalService.getFromProcessId(processId));
        }
        userService.removeProcessFromProcessLists(process);
        process.getItem().removeFromPeriods(process.getPeriod());
        processes.delete(process);
    }
}