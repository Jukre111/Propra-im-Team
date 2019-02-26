package de.hhu.sharing.ServiceTests;

import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.web.util.NestedServletException;

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
        User testUser = userService.get("user");
        Assert.assertTrue(testUser.equals(user));
    }

    @Test (expected = RuntimeException.class)
    public void testGetNoUserFound(){
        User user = generateUser();
        Mockito.when(users.findByUsername("user")).thenReturn(Optional.empty());
        User testUser = userService.get("user");

    }

    @Test
    public void testGetBorrowerFromBorrowingProcessId(){

        User user = generateUser();
        Mockito.when(users.findByBorrowed_id(1L)).thenReturn(Optional.of(user));
        User testUser = userService.getBorrowerFromBorrowingProcessId(1L);
        Assert.assertTrue(testUser.equals(user));
    }

    @Test (expected = RuntimeException.class)
    public void testGetBorrowerFromBorrowingProcessIdNoUserFound(){

        User user = generateUser();
        Mockito.when(users.findByBorrowed_id(1L)).thenReturn(Optional.empty());
        User testUser = userService.getBorrowerFromBorrowingProcessId(1L);
    }


   // @Test
  //  public void testRemoveProcessFromProcessLists(){

      //  User user = generateUser();
      //  Mockito.when(users.findByBorrowed_id(1L)).thenReturn(Optional.of(user));

   // }
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
