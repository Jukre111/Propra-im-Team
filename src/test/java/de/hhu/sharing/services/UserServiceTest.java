package de.hhu.sharing.services;

import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.*;
import de.hhu.sharing.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.internal.verification.VerificationModeFactory.times;


public class UserServiceTest {

    @Mock
    private UserRepository users;

    @InjectMocks
    private UserService userService;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
    }

    private LendableItem generateItem(User user) {
        return new LendableItem("apfel", "lecker", 1, 1, user);
    }

    public User generateUser() {
        LocalDate date = LocalDate.of(2000, 1, 1);
        Address address = new Address("unistrase", "duesseldorf", 40233);
        User user = new User("user", "password", "role", "lastnmae", "forname", "email", date, address);
        return user;
    }

    @Test
    public void testGet() {
        User user = generateUser();
        Mockito.when(users.findByUsername("user")).thenReturn(Optional.of(user));
        User testUser = userService.get("user");
        Assert.assertTrue(testUser.equals(user));
    }

    @Test(expected = RuntimeException.class)
    public void testGetNoUserFound() {
        Mockito.when(users.findByUsername("user")).thenReturn(Optional.empty());
        userService.get("user");
    }

    @Test
    public void testGetBorrowerFromBorrowingProcessId() {

        User user = generateUser();
        Mockito.when(users.findByBorrowed_id(1L)).thenReturn(Optional.of(user));
        User testUser = userService.getBorrowerFromBorrowingProcessId(1L);
        Assert.assertTrue(testUser.equals(user));
    }

    @Test(expected = RuntimeException.class)
    public void testGetBorrowerFromBorrowingProcessIdNoUserFound() {
        Mockito.when(users.findByBorrowed_id(1L)).thenReturn(Optional.empty());
        userService.getBorrowerFromBorrowingProcessId(1L);

    }


    @Test
    public void testRemoveProcessFromProcessLists() {


        User user1 = generateUser();
        User user2 = generateUser();
        LendableItem item = generateItem(user1);

        BorrowingProcess process = new BorrowingProcess();
        process.setId(1L);
        process.setItem(item);
        user1.addToLend(process);
        user2.addToBorrowed(process);
        Mockito.when(users.findByBorrowed_id(1L)).thenReturn(Optional.of(user2));
        userService.removeProcessFromProcessLists(process);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(users, times(2)).save(captor.capture());


        Assert.assertTrue(captor.getAllValues().get(0).getLend().size() == 0);
        Assert.assertTrue(captor.getAllValues().get(1).getBorrowed().size() == 0);

    }

    @Test
    public void testUserIsLender() {
        User user1 = generateUser();
        User user2 = generateUser();
        LendableItem item = generateItem(user1);
        BorrowingProcess process = new BorrowingProcess();
        process.setId(1L);
        process.setItem(item);
        Mockito.when(users.findByBorrowed_id(1L)).thenReturn(Optional.of(user2));

        Assert.assertTrue(userService.userIsInvolvedToProcess(user1, process) == true);
    }

    @Test
    public void testUserIsBorrower() {
        User user1 = generateUser();
        User user2 = generateUser();

        LendableItem item = generateItem(user1);
        BorrowingProcess process = new BorrowingProcess();
        process.setId(1L);
        process.setItem(item);
        user2.addToBorrowed(process);
        Mockito.when(users.findByBorrowed_id(1L)).thenReturn(Optional.of(user2));

        Assert.assertTrue(userService.userIsInvolvedToProcess(user2, process) == true);
    }

    @Test
    public void testUserIsNotInvolved() {
        User user1 = generateUser();
        User user2 = generateUser();

        LendableItem item = generateItem(user1);
        BorrowingProcess process = new BorrowingProcess();
        process.setItem(item);
        process.setId(1L);
        Mockito.when(users.findByBorrowed_id(1L)).thenReturn(Optional.of(user1));

        Assert.assertTrue(userService.userIsInvolvedToProcess(user2, process) == false);
    }

    @Test
    public void testUserHasNotReturnedItems(){
        User user = generateUser();
        LendableItem item = generateItem(user);
        Period period = new Period(LocalDate.of(2000,1,1),LocalDate.of(2000,2,2));
        BorrowingProcess process = new BorrowingProcess(item,period);
        user.addToBorrowed(process);
        userService.userHasNotReturnedItems(user);

        Assert.assertTrue(userService.userHasNotReturnedItems(user) == true);
    }

    @Test
    public void testUserHasNotReturnedItemsButTimeIsNotOver(){
        User user = generateUser();
        LendableItem item = generateItem(user);
        Period period = new Period(LocalDate.of(2020,1,1),LocalDate.of(2020,2,2));
        BorrowingProcess process = new BorrowingProcess(item,period);
        user.addToBorrowed(process);
        userService.userHasNotReturnedItems(user);

        Assert.assertTrue(userService.userHasNotReturnedItems(user) == false);
    }

    

}