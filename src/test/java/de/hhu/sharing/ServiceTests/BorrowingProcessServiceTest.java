package de.hhu.sharing.ServiceTests;

import de.hhu.sharing.data.BorrowingProcessRepository;
import de.hhu.sharing.model.*;
import de.hhu.sharing.services.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;

public class BorrowingProcessServiceTest {

    @Mock
    private BorrowingProcessRepository processes;

    @Mock
    private RequestService requestService;

    @Mock
    private ItemService itemService;

    @Mock
    private UserService userService;

    @Mock
    private ProPayService proPayService;

    @Mock
    private TransactionRentalService transactionService;

    @Mock
    private  ConflictService conflictService;

    @InjectMocks
    private BorrowingProcessService BPService;

    @Before
    public void initialize(){
        MockitoAnnotations.initMocks(this);
    }

    private User generateUser(String username) {
        LocalDate birthdate = LocalDate.of(2000, 1, 1);
        Address address = new Address("unistrase", "duesseldorf", 40233);
        User user = new User(username, "password", "role", "lastname", "forename", "email", birthdate, address);
        return user;
    }

    private Item generateItem(User user) {
        return new Item("apfel", "lecker", 1, 1, user);
    }

    private Period generatePeriod(){
        LocalDate startdate = LocalDate.of(2019,4,5);
        LocalDate enddate = LocalDate.of(2019,4,13);
        return new Period(startdate, enddate);
    }


    @Test
    public void testGet(){
        User user = generateUser("userman");
        Item item = generateItem(user);
        Period period = generatePeriod();
        BorrowingProcess borrowingProcess = new BorrowingProcess(item, period);
        Mockito.when(processes.findById(1L)).thenReturn(Optional.of(borrowingProcess));

        Assert.assertEquals(BPService.get(1L), borrowingProcess);
    }

    @Test(expected = RuntimeException.class)
    public void testGetItemNotFound(){
        Mockito.when(processes.findById(1L)).thenReturn(Optional.empty());
        BPService.get(1L);
    }
    

}
