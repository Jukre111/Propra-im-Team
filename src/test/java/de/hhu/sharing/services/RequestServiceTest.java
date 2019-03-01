
package de.hhu.sharing.services;

import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.model.*;
import de.hhu.sharing.services.LendableItemService;
import de.hhu.sharing.services.RequestService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;

public class RequestServiceTest {

    @Mock
    private RequestRepository requests;

    @Mock
    private LendableItemService lendableItemService;

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


    private LendableItem generateItem(User user) {
        return new LendableItem("apfel", "lecker",1,1 ,user );
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

    @Test(expected = RuntimeException.class)
    public void testGetNotExistent(){

        Mockito.when(requests.findById(1L)).thenReturn(Optional.empty());
        requestService.get(1L);
    }

    @Test
    public void testCreate(){
        User user = generateUser("Karl");
        LendableItem lendableItem = generateItem(user);
        Mockito.when(lendableItemService.get(1L)).thenReturn(lendableItem);

        requestService.create(1L, LocalDate.of(2000,2,2),LocalDate.of(2000,2,3),user);

        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        Mockito.verify(requests, times(1)).save(captor.capture());

        Assert.assertEquals(captor.getAllValues().get(0).getRequester(), user);
        Assert.assertEquals(lendableItem.getRequests().get(0).getRequester(), user);
    }

    @Test
    public void testDelete(){
        User user1 = generateUser("user");
        User user2 = generateUser("user");
        Request request = generateRequest(user1);
        request.setId(1L);
        LendableItem lendableItem = generateItem(user2);
        lendableItem.addToRequests(request);
        Mockito.when(requests.findById(1L)).thenReturn(Optional.of(request));
        Mockito.when(lendableItemService.getFromRequestId(1L)).thenReturn(lendableItem);

        requestService.delete(1L);

        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        Mockito.verify(requests, times(1)).delete(captor.capture());

        Assert.assertEquals(captor.getAllValues().get(0), request);
        Assert.assertTrue(lendableItem.getRequests().isEmpty());
    }

    @Test
    public void testIsOverlappingWithAvailability(){
    	LendableItem item = new LendableItem();
        Request request = new Request();
        Period period = new Period(LocalDate.of(2000,1,1),LocalDate.of(2000,1,2));
        request.setId(1L);
        request.setPeriod(period);
        item.addToPeriods(period);
        Mockito.when(lendableItemService.getFromRequestId(1L)).thenReturn(item);
        Mockito.when(requests.findById(1L)).thenReturn(Optional.of(request));
        boolean result = requestService.isOverlappingWithAvailability(1L);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsNotOverlappingWithAvailability(){
    	LendableItem item = new LendableItem();
        Request request = new Request();
        Period period = new Period(LocalDate.of(2000,1,1),LocalDate.of(2000,1,2));
        Period period2 = new Period(LocalDate.of(2000,1,3),LocalDate.of(2000,1,4));
        request.setId(1L);
        request.setPeriod(period);
        item.addToPeriods(period2);
        Mockito.when(lendableItemService.getFromRequestId(1L)).thenReturn(item);
        Mockito.when(requests.findById(1L)).thenReturn(Optional.of(request));
        boolean result = requestService.isOverlappingWithAvailability(1L);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsOutdated(){
        Request request = new Request();
        Period period = new Period(LocalDate.of(2000,1,1),LocalDate.of(2000,1,2));
        request.setId(1L);
        request.setPeriod(period);
        Mockito.when(requests.findById(1L)).thenReturn(Optional.of(request));
        boolean result = requestService.isOutdated(1L);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsNotOutdated(){
        Request request = new Request();
        Period period = new Period(LocalDate.of(2020,1,1),LocalDate.of(2020,1,2));
        request.setId(1L);
        request.setPeriod(period);
        Mockito.when(requests.findById(1L)).thenReturn(Optional.of(request));
        boolean result = requestService.isOutdated(1L);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsRequester(){
        User user = generateUser("user");
        Request request = new Request();
        request.setRequester(user);
        Period period = new Period(LocalDate.of(2000,1,1),LocalDate.of(2000,1,2));
        request.setId(1L);
        request.setPeriod(period);
        Mockito.when(requests.findById(1L)).thenReturn(Optional.of(request));
        boolean result = requestService.isRequester(1L,user);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsNotRequester(){
        User user = generateUser("user");
        Request request = new Request();
        Period period = new Period(LocalDate.of(2000,1,1),LocalDate.of(2000,1,2));
        request.setId(1L);
        request.setPeriod(period);
        Mockito.when(requests.findById(1L)).thenReturn(Optional.of(request));
        boolean result = requestService.isRequester(1L,user);
        Assert.assertFalse(result);
    }


    @Test
    public void testIsLender(){
        User user = generateUser("user");
        LendableItem item = generateItem(user);
        item.setId(1L);
        Request request = new Request();
        item.addToRequests(request);
        Period period = new Period(LocalDate.of(2000,1,1),LocalDate.of(2000,1,2));
        request.setId(1L);
        request.setPeriod(period);
        Mockito.when(lendableItemService.getFromRequestId(1L)).thenReturn(item);
        boolean result = requestService.isLender(1L,user);
        Assert.assertTrue(result);
    }
    @Test
    public void testIsNotLender(){
        User user = generateUser("user");
        User user2 = generateUser("user2");
        LendableItem item = generateItem(user);
        item.setId(1L);
        Request request = new Request();
        item.addToRequests(request);
        Period period = new Period(LocalDate.of(2000,1,1),LocalDate.of(2000,1,2));
        request.setId(1L);
        request.setPeriod(period);
        Mockito.when(lendableItemService.getFromRequestId(1L)).thenReturn(item);
        boolean result = requestService.isLender(1L,user2);
        Assert.assertFalse(result);
    }


    @Test
    public void testDeleteOverlappingRequestsFromItem(){
        User requester = generateUser("requester");
        User owner = generateUser("owner");
        User otherRequester = generateUser("otherRequester");

        Request request = generateRequest(requester);
        request.setId(1L);
        Request otherRequest = generateRequest(otherRequester);
        otherRequest.setId(2L);
        LendableItem lendableItem = generateItem(owner);
        lendableItem.addToRequests(otherRequest);

        Mockito.when(requests.findById(1L)).thenReturn(Optional.of(request));
        Mockito.when(requests.findById(2L)).thenReturn(Optional.of(otherRequest));
        Mockito.when(lendableItemService.getFromRequestId(1L)).thenReturn(lendableItem);
        Mockito.when(lendableItemService.getFromRequestId(2L)).thenReturn(lendableItem);

        requestService.deleteOverlappingRequestsFromItem(request, lendableItem);

        Assert.assertTrue(lendableItem.getRequests().isEmpty());

    }

    @Test
    public void testDeleteOutdatedRequests() {
        User requester = generateUser("requester");
        User owner = generateUser("owner");
        User otherRequester = generateUser("otherRequester");

        Request request = generateRequest(requester);
        request.setId(1L);
        Request otherRequest = generateRequest(otherRequester);
        otherRequest.setId(2L);
        LendableItem lendableItem = generateItem(owner);

        otherRequest.setPeriod(new Period(LocalDate.now().minusDays(1),LocalDate.now().plusDays(1)));
        request.setPeriod(new Period(LocalDate.now().plusDays(1),LocalDate.now().plusDays(3)));
        lendableItem.addToRequests(otherRequest);
        lendableItem.addToRequests(request);


        Mockito.when(requests.findById(1L)).thenReturn(Optional.of(request));
        Mockito.when(requests.findById(2L)).thenReturn(Optional.of(otherRequest));
        Mockito.when(lendableItemService.getFromRequestId(1L)).thenReturn(lendableItem);
        Mockito.when(lendableItemService.getFromRequestId(2L)).thenReturn(lendableItem);
        ArrayList<Request> requestList = new ArrayList();
        requestList.add(request);
        requestList.add(otherRequest);
        Mockito.when(requests.findAll()).thenReturn(requestList);

        requestService.deleteOutdatedRequests();

        Assert.assertTrue(lendableItem.getRequests().contains(request));
    }

}
