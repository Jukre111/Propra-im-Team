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
        this.createProcessForUsers(item, request);
        requestService.deleteOverlappingRequestsFromItem(request, item);

    }

    private void createProcessForUsers(Item item, Request request) {
        BorrowingProcess process = new BorrowingProcess(item, request.getPeriod());
        processes.save(process);

        ArrayList <BorrowingProcess> listProcesses = (ArrayList<BorrowingProcess>) processes.findAll();
        BorrowingProcess process1 = listProcesses.get(listProcesses.size()-1);
        List<Transaction> list = transRepo.findAll();
        Transaction trans = list.get(list.size()-1);
        trans.setProcessId(process1.getId());
        transRepo.save(trans);

        request.getRequester().addToBorrowed(process);
        item.getLender().addToLend(process);
    }

    public void returnItem(Long processId, User lender){
        BorrowingProcess process = this.get(processId);
        String borrower = userService.getBorrowerFromBorrowingProcessId(processId).getUsername();

        if(proService.cancelDeposit(borrower,transRepo.findByProcessId(processId))==200){
            userService.removeProcessFromProcessLists(lender, process);
            process.getItem().removeFromPeriods(process.getPeriod());
            processes.delete(process);
        }
    }

    public BorrowingProcess getBorrowingProcess(Long id){
    BorrowingProcess borrowingProcess = processes.findById(id).orElseThrow(()-> new RuntimeException("BorrowindProcess not found."));
    return borrowingProcess;
    }
}
