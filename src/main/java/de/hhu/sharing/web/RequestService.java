package de.hhu.sharing.web;

import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.Request;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

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
        Request request = new Request(startdate, enddate, user);
        requests.save(request);
        itemService.addToRequests(itemId, request);
    }

    public void delete(Long requestId) {
        Request request = this.get(requestId);
        itemService.removeFromRequests(request);
        requests.delete(request);

    }

    public void accept(Long requestId, RedirectAttributes redirectAttributes) {
        Item item = itemService.getFromRequestId(requestId);
        if(item.isAvailable()) {
            item.setAvailable(false);
            Request request = this.get(requestId);
            itemService.removeFromRequests(request);
            userService.addToBorrowedItems(request.getRequester(), item);
            requests.delete(request);
        }
        else{
            redirectAttributes.addFlashAttribute("notAvailable",true);
        }

    }


}