package de.hhu.sharing.services;

import de.hhu.sharing.data.BorrowingProcessRepository;
import de.hhu.sharing.model.BorrowingProcess;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.Request;
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
}
