package de.hhu.sharing.ServiceTests;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.model.*;
import de.hhu.sharing.services.ItemService;
import de.hhu.sharing.services.RequestService;
import de.hhu.sharing.services.TransactionService;
import de.hhu.sharing.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;

public class RequestServiceTest {

    @Mock
    private RequestRepository requests;

    @Mock
    private ItemService itemService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private RequestService requestService;

    @Before
    public void initialize(){
        MockitoAnnotations.initMocks(this);
    }


    private User generateUser() {
        LocalDate birthdate = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        return new User("user","password", "role", "lastname", "forename", "email", birthdate, address);
    }


    public Item generateItem() {
        User user = generateUser();
        return new Item("apfel", "lecker",1,1 ,user );
    }

    public Request generateRequest() {
        User requester = generateUser();
        Period period = new Period(LocalDate.of(2000,2,2),LocalDate.of(2000,2,3));
        return new Request(period ,requester);
    }

    @Test
    public void testGet(){
        Request request = generateRequest();
        Mockito.when(requests.findById(1L)).thenReturn(Optional.of(request));
        Assert.assertEquals(requestService.get(1L), request);
    }
//    Methods changed, to be fixed
//    @Test
//    public void testCreate(){
//        User user = generateUser();
//        requestService.create(1L, LocalDate.of(2000,2,2),LocalDate.of(2000,2,3),user);
//        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
//        Mockito.verify(requests, times(1)).save(captor.capture());
//        Assert.assertTrue(captor.getAllValues().get(0).getRequester().equals(user));
//    }
//
//    @Test
//    public void testDelete(){
//        User requester = generateUser();
//        Period period = new Period(LocalDate.of(2000,2,2),LocalDate.of(2000,2,3));
//        Request request = new Request(period ,requester);
//        Mockito.when(requests.findById(1L)).thenReturn(Optional.of(request));
//        requestService.delete(1L);
//        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
//        Mockito.verify(requests, times(1)).delete(captor.capture());
//        Assert.assertTrue(captor.getAllValues().get(0).getRequester().equals(requester));
//    }

}
