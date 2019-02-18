package de.hhu.sharing.web;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.Request;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RequestService {

    @Autowired
    private UserRepository users;

    @Autowired
    private ItemRepository items;

    @Autowired
    private RequestRepository requests;

    public void createRequest(Item item, LocalDate startdate, LocalDate enddate, User user) {
        Request request = new Request(startdate, enddate, user);
        requests.save(request);
        item.addToRequests(request);
        items.save(item);
    }
}
