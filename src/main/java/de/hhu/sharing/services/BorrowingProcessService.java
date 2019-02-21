package de.hhu.sharing.services;

import de.hhu.sharing.data.BorrowingProcessRepository;
import de.hhu.sharing.model.BorrowingProcess;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.Request;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BorrowingProcessService {

    @Autowired
    private BorrowingProcessRepository processes;

    @Autowired
    private RequestService requestService;

    @Autowired
    private ItemService itemService;

    private BorrowingProcess get(Long id) {
        BorrowingProcess process = this.processes.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Process not found!"));
        return process;
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
        request.getRequester().addToBorrowed(process);
        item.getLender().addToLend(process);
    }

    public void returnItem(Long processId, User borrower){
        BorrowingProcess process = this.get(processId);
        borrower.removeFromBorrowed(process);
        process.getItem().getLender().removeFromLend(process);
        process.getItem().removeFromPeriods(process.getPeriod());
        processes.delete(process);
    }

    public BorrowingProcess getBorrowingProcess(Long id){
    BorrowingProcess borrowingProcess = processes.findById(id).orElseThrow(()-> new RuntimeException("BorrowindProcess not found."));
    return borrowingProcess;
    }
}
