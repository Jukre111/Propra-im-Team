package de.hhu.sharing.services;

import de.hhu.sharing.data.ItemRepository;
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
        return this.requests.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Request not found!"));
    }

    public void create(Long itemId, LocalDate startdate, LocalDate enddate, User user) {
        Request request = new Request(new Period(startdate, enddate), user);
        Item item = itemService.get(itemId);
        item.addToRequests(request);
        requests.save(request);
    }

    public void delete(Long requestId) {
        Request request = this.get(requestId);
        Item item = itemService.getFromRequestId(request.getId());
        item.removeFromRequests(request);
        requests.delete(request);
    }

    public boolean isOverlappingWithAvailability(Long requestId) {
        Request request = this.get(requestId);
        Item item = itemService.getFromRequestId(requestId);
        return !item.isAvailableAt(request.getPeriod());
    }

    public boolean isOutdated(Long requestId) {
        Request request = this.get(requestId);
        return request.getPeriod().isOutdated();
    }

    public boolean isRequester(Long requestId, User user) {
        return this.get(requestId).getRequester() == user;
    }

    public boolean isLender(Long requestId, User user) {
        return itemService.getFromRequestId(requestId).getLender() == user;
    }

    public void deleteOverlappingRequestsFromItem(Request request, Item item) {
        List<Request> requests = new ArrayList<>(item.getRequests());
        for(Request req : requests){
            if(req.getPeriod().overlapsWith(request.getPeriod())){
                this.delete(req.getId());
            }
        }
    }

    public void deleteOutdatedRequests() {
        List<Request> requests = new ArrayList<>(this.requests.findAll());
        for(Request req : requests){
            if(req.getPeriod().isOutdated()){
                this.delete(req.getId());
            }
        }
    }
}
