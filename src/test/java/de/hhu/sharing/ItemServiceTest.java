package de.hhu.sharing;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.TransactionRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.ItemService;
import de.hhu.sharing.services.ProPayService;
import de.hhu.sharing.services.RequestService;
import de.hhu.sharing.services.UserService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ItemServiceTest {

    @Autowired
    MockMvc mvc;

    @Mock
    private ItemRepository items;

    @InjectMocks
    private ItemService itemService;

    @Mock
    private UserRepository users;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private RequestService requestService;

    @Mock
    private TransactionRepository transRepo;

    @InjectMocks
    private ProPayService proPay;

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

    @Test
    @WithMockUser
    public void testCreate(){
        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        Mockito.verify(items, times(1)).save(captor.capture());

        User user = generateUser();
        itemService.create("item","description",1,1,user);

        Assert.assertTrue(captor.getAllValues().get(0).getName().equals("item"));

    }

}
