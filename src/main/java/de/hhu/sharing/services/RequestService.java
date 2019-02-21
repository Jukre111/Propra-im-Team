package de.hhu.sharing.services;

import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.model.*;
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

    public Request get(Long id){
        Request request = this.requests.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Request not found!"));
        return request;
    }

    public void create(Long itemId, LocalDate startdate, LocalDate enddate, User user) {
        Request request = new Request(new Period(startdate, enddate), user);
        requests.save(request);
        Item item = itemService.get(itemId);
        item.addToRequests(request);
    }

    public void delete(Long requestId) {
        Request request = this.get(requestId);
        Item item = itemService.getFromRequestId(request.getId());
        item.removeFromRequests(request);
        requests.delete(request);

    }

    public void deleteOverlappingRequestsFromItem(Request request, Item item) {
        List<Request> requests = new ArrayList<>(item.getRequests());
        for(Request req : requests){
            if(req.getPeriod().overlapsWith(request.getPeriod())){
                this.delete(req.getId());
            }
        }
    }


}
