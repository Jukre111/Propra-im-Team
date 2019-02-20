package de.hhu.sharing.services;

import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.Period;
import de.hhu.sharing.model.Request;
import de.hhu.sharing.model.User;
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
    private ItemService itemService;

    @Autowired
    private UserService userService;

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
        itemService.accept(item, request);
        userService.addToBorrowedItems(request.getRequester(), item);
    }

    public void removeOverlapping(Request request, Item item) {
        List<Request> requests = new ArrayList<>(item.getRequests());
        for(Request req : requests){
            if(req.getPeriod().overlapsWith(request.getPeriod()) && !req.equals(request)){
                this.delete(req.getId());
            }
        }
    }
}
