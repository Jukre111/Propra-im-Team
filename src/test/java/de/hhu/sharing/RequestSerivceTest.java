package de.hhu.sharing;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.*;
import de.hhu.sharing.services.ItemService;
import de.hhu.sharing.services.RequestService;
import de.hhu.sharing.services.UserService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.internal.verification.VerificationModeFactory.times;

public class RequestSerivceTest {

    @Mock
    private UserService userService;

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


    public User generateUser() {
        LocalDate date = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User("user","password", "role", "lastnmae", "forname", "email",date,address);
        return user;
    }


    public Item generateItem() {
        User user = generateUser();
        Item item = new Item("apfel", "lecker",1,1 ,user );
        return item;
    }

    @Test
    public void testGet(){
        User requester = generateUser();
        Period period = new Period(LocalDate.of(2000,2,2),LocalDate.of(2000,2,3));
        Request request = new Request(period ,requester);
        Mockito.when(requests.findById(1L)).thenReturn(Optional.of(request));
        Assert.assertTrue(requestService.get(1L).equals(request));
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
