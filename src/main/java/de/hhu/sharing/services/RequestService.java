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
    private LendableItemService lendableItemService;

    public Request get(Long id){
        return this.requests.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Anfrage nicht gefunden!"));
    }

    public void create(Long itemId, LocalDate startdate, LocalDate enddate, User user) {
        Request request = new Request(new Period(startdate, enddate), user);
        LendableItem lendableItem = lendableItemService.get(itemId);
        lendableItem.addToRequests(request);
        requests.save(request);
    }

    public void delete(Long requestId) {
        Request request = this.get(requestId);
        LendableItem lendableItem = lendableItemService.getFromRequestId(request.getId());
        lendableItem.removeFromRequests(request);
        requests.delete(request);
    }

    public boolean isOverlappingWithAvailability(Long requestId) {
        Request request = this.get(requestId);
        LendableItem lendableItem = lendableItemService.getFromRequestId(requestId);
        return !lendableItem.isAvailableAt(request.getPeriod());
    }

    public boolean isOutdated(Long requestId) {
        Request request = this.get(requestId);
        return request.getPeriod().isOutdated();
    }

    public boolean isRequester(Long requestId, User user) {
        return this.get(requestId).getRequester() == user;
    }

    public boolean isLender(Long requestId, User user) {
        return lendableItemService.getFromRequestId(requestId).getOwner() == user;
    }

    public void deleteOverlappingRequestsFromItem(Request request, LendableItem lendableItem) {
        List<Request> requests = new ArrayList<>(lendableItem.getRequests());
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
