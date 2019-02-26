package de.hhu.sharing.ServiceTests;

import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.Optional;


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

//    Method doesn't exist anymore
//    @Test
//    public void testAddToBorrowedItems(){
//        User user = generateUser();
//
//        LendableItem LendableItem = new LendableItem("apfel", "lecker",1,1 ,user );
//        LendableItem item2 = new LendableItem("apfel", "lecker",1,1 ,user );
//        LendableItem item3 = new LendableItem("apfel", "lecker",1,1 ,user );
//        ArrayList<LendableItem> liste = new ArrayList<>();
//        liste.add(LendableItem);
//        liste.add(item2);
//        liste.add(item3);
//        userService.addToBorrowedItems(user,LendableItem);
//        userService.addToBorrowedItems(user,item2);
//        userService.addToBorrowedItems(user,item3);
//        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
//        Mockito.verify(users, times(3)).save(captor.capture());
//        Assert.assertTrue(captor.getAllValues().get(0).getBorrowedItems().equals(liste));
//    }
}
