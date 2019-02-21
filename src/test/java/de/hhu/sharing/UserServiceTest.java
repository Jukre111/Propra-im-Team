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
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.internal.verification.VerificationModeFactory.times;

public class UserServiceTest {

    @Mock
    private UserRepository users;

    @InjectMocks
    private UserService userService;

    @Before public void initialize(){
        MockitoAnnotations.initMocks(this);
    }

    public User generateUser() {
        LocalDate date = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User("user","password", "role", "lastnmae", "forname", "email",date,address);
        return user;
    }

    @Test
    public void testGet(){
        User user = generateUser();
        Mockito.when(users.findByUsername("user")).thenReturn(Optional.of(user));
        User user2 = userService.get("user");
        Assert.assertTrue(user2.equals(user));
    }

    @Test
    public void testAddToBorrowedItems(){
        User user = generateUser();

        Item item = new Item("apfel", "lecker",1,1 ,user );
        Item item2 = new Item("apfel", "lecker",1,1 ,user );
        Item item3 = new Item("apfel", "lecker",1,1 ,user );
        ArrayList<Item> liste = new ArrayList<>();
        liste.add(item);
        liste.add(item2);
        liste.add(item3);
        userService.addToBorrowedItems(user,item);
        userService.addToBorrowedItems(user,item2);
        userService.addToBorrowedItems(user,item3);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(users, times(3)).save(captor.capture());
        Assert.assertTrue(captor.getAllValues().get(0).getBorrowedItems().equals(liste));
    }
}
