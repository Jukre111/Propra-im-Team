package de.hhu.sharing.ServiceTests;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.model.*;
import de.hhu.sharing.services.ItemService;
import de.hhu.sharing.services.RequestService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.times;

public class RequestServiceTest {

    @Mock
    private RequestRepository requests;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private RequestService requestService;

    @Before
    public void initialize(){
        MockitoAnnotations.initMocks(this);
    }


    private User generateUser(String username) {
        LocalDate birthdate = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        return new User(username,"password", "role", "lastname", "forename", "email", birthdate, address);
    }


    private Item generateItem(User user) {
        return new Item("apfel", "lecker",1,1 ,user );
    }

    private Request generateRequest(User requester) {
        Period period = new Period(LocalDate.of(2000,2,2),LocalDate.of(2000,2,3));
        return new Request(period ,requester);
    }

    @Test
    public void testGet(){
        User user = generateUser("Karl");
        Request request = generateRequest(user);
        Mockito.when(requests.findById(1L)).thenReturn(Optional.of(request));
        Assert.assertEquals(requestService.get(1L), request);
    }

    @Test
    public void testCreate(){
        User user = generateUser("Karl");
        Item item = generateItem(user);
        Mockito.when(itemService.get(1L)).thenReturn(item);

        requestService.create(1L, LocalDate.of(2000,2,2),LocalDate.of(2000,2,3),user);

        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        Mockito.verify(requests, times(1)).save(captor.capture());

        Assert.assertEquals(captor.getAllValues().get(0).getRequester(), user);
        Assert.assertEquals(item.getRequests().get(0).getRequester(), user);
    }

    @Test
    public void testDelete(){
        User user1 = generateUser("user");
        User user2 = generateUser("user");
        Request request = generateRequest(user1);
        request.setId(1L);
        Item item = generateItem(user2);
        item.addToRequests(request);
        Mockito.when(requests.findById(1L)).thenReturn(Optional.of(request));
        Mockito.when(itemService.getFromRequestId(1L)).thenReturn(item);

        requestService.delete(1L);

        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        Mockito.verify(requests, times(1)).delete(captor.capture());

        Assert.assertEquals(captor.getAllValues().get(0), request);
        Assert.assertTrue(item.getRequests().isEmpty());
    }

    @Test
    public void testDeleteOverlappingRequestsFromItem(){
        User requester = generateUser("requester");
        User owner = generateUser("lender");
        User otherRequester = generateUser("otherRequester");

        Request request = generateRequest(requester);
        request.setId(1L);
        Request otherRequest = generateRequest(otherRequester);
        otherRequest.setId(2L);
        Item item = generateItem(owner);
        item.addToRequests(otherRequest);

        Mockito.when(requests.findById(1L)).thenReturn(Optional.of(request));
        Mockito.when(requests.findById(2L)).thenReturn(Optional.of(otherRequest));
        Mockito.when(itemService.getFromRequestId(1L)).thenReturn(item);
        Mockito.when(itemService.getFromRequestId(2L)).thenReturn(item);

        requestService.deleteOverlappingRequestsFromItem(request, item);

        Assert.assertTrue(item.getRequests().isEmpty());


    }

}
