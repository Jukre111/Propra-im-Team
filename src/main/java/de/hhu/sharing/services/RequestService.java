package de.hhu.sharing.services;

import de.hhu.sharing.data.BorrowingProcessRepository;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.model.*;
import de.hhu.sharing.services.ItemService;
import de.hhu.sharing.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class RequestService {

    @Autowired
    private RequestRepository requests;

    @Autowired
    private BorrowingProcessRepository processes;

    @Autowired
    private ItemService itemService;

    public Request get(Long id){
        Request request = this.requests.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Request not found!"));
        return request;
    }

    public void create(Long itemId, LocalDate startdate, LocalDate enddate, User user) {
        Request request = new Request(new Period(startdate, enddate), user);
        requests.save(request);
        itemService.addToRequests(itemId, request);
    }

    public void delete(Long requestId) {
        Request request = this.get(requestId);
        itemService.removeFromRequests(request);
        requests.delete(request);

    }

    public void accept(Long requestId) {
        Request request = this.get(requestId);
        Item item = itemService.getFromRequestId(requestId);
        itemService.addToPeriods(item, request);
        this.createProcessForUsers(item, request);
        this.removeOverlapping(request, item);

    }

    public void removeOverlapping(Request request, Item item) {
        List<Request> requests = new ArrayList<>(item.getRequests());
        for(Request req : requests){
            if(req.getPeriod().overlapsWith(request.getPeriod())){
                this.delete(req.getId());
            }
        }
    }

    private void createProcessForUsers(Item item, Request request) {
        BorrowingProcess process = new BorrowingProcess(item, request.getPeriod());
        processes.save(process);
        request.getRequester().addToBorrowed(process);
        item.getLender().addToLend(process);
    }
}
