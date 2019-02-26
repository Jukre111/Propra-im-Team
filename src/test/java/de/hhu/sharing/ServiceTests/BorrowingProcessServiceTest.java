package de.hhu.sharing.ServiceTests;

import de.hhu.sharing.data.BorrowingProcessRepository;
import de.hhu.sharing.model.*;
import de.hhu.sharing.services.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.internal.verification.VerificationModeFactory.times;

public class BorrowingProcessServiceTest {

    @Mock
    private BorrowingProcessRepository processes;

    @Spy
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

    private Period generatePeriod(int startday, int endday){
        LocalDate startdate = LocalDate.of(2019,4,startday);
        LocalDate enddate = LocalDate.of(2019,4, endday);
        return new Period(startdate, enddate);
    }


    @Test
    public void testGet(){
        User user = generateUser("userman");
        Item item = generateItem(user);
        Period period = generatePeriod(4,9);
        BorrowingProcess borrowingProcess = new BorrowingProcess(item, period);
        Mockito.when(processes.findById(1L)).thenReturn(Optional.of(borrowingProcess));

        Assert.assertEquals(BPService.get(1L), borrowingProcess);
    }

    @Test(expected = RuntimeException.class)
    public void testGetItemNotFound(){
        Mockito.when(processes.findById(1L)).thenReturn(Optional.empty());
        BPService.get(1L);
    }

    @Test
    public void testAccept(){
        User requester = generateUser("Karl");
        User lender = generateUser("Jos");
        Period period = generatePeriod(4,9);
        Period periodOverlapping = generatePeriod(3,7);
        Request request = new Request(period, requester);
        Item item = generateItem(lender);
        item.addToPeriods(periodOverlapping);

        //Mockito.when(requestService.get(1L)).thenReturn(request);
        Mockito.doReturn(request).when(requestService).get(1L);
        Mockito.when(itemService.getFromRequestId(1L)).thenReturn(item);

        BPService.accept(1L);

        //get the borrowingProcess, which is saved
        ArgumentCaptor<BorrowingProcess> captor = ArgumentCaptor.forClass(BorrowingProcess.class);
        Mockito.verify(processes, times(1)).save(captor.capture());

        //get which process, borrower, lender are given to createTransactionRental
        ArgumentCaptor<BorrowingProcess> captorProcess = ArgumentCaptor.forClass(BorrowingProcess.class);
        ArgumentCaptor<User> captorBorrower = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<User> captorLender = ArgumentCaptor.forClass(User.class);
        Mockito.verify(transactionService, times(1)).createTransactionRental(captorProcess.capture(), captorBorrower.capture(), captorLender.capture());



        Assert.assertEquals(captor.getValue().getItem(), item);
        Assert.assertEquals(captor.getValue().getPeriod(), period);

        Assert.assertEquals(captorProcess.getValue().getItem(), item);
        Assert.assertEquals(captorProcess.getValue().getPeriod(), period);

        Assert.assertEquals(captorBorrower.getValue(), requester);

        Assert.assertEquals(captorLender.getValue(), lender);

        Assert.assertEquals(requester.getBorrowed().get(0).getItem(), item);
        Assert.assertEquals(requester.getBorrowed().get(0).getPeriod(), period);
    }

    @Test
    public void testItemReturned(){
        User requester = generateUser("Karl");
        Period period = generatePeriod(4,9);
        Request request = new Request(period, requester);
        Mockito.doReturn(request).when(requestService).get(1L);
    }

}
