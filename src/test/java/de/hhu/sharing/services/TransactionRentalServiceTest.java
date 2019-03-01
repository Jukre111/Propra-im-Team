package de.hhu.sharing.services;

import static org.mockito.internal.verification.VerificationModeFactory.times;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hhu.sharing.data.LendableItemRepository;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.data.TransactionRentalRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.BorrowingProcess;
import de.hhu.sharing.model.LendableItem;
import de.hhu.sharing.model.Period;
import de.hhu.sharing.model.User;
import de.hhu.sharing.propay.TransactionRental;

public class TransactionRentalServiceTest {
    @Mock
    LendableItemRepository itemRepo;

    @Mock
    RequestRepository reqRepo;
    
    @Mock
    TransactionRentalRepository transactions;

    @Mock
    ProPayService proService;

    @InjectMocks
    TransactionRentalService transService;

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
    
    private LendableItem generateItem(User user) {
        return new LendableItem("apfel", "lecker", 1, 1, user);
    }

    private Period generatePeriod(int startday, int endday){
        LocalDate startdate = LocalDate.of(2019,4,startday);
        LocalDate enddate = LocalDate.of(2019,4, endday);
        return new Period(startdate, enddate);
    }
    
    @Test
    public void testCreateTransactionRental() {
        Period period = generatePeriod(1,2);
        User borrower = generateUser("user");
        User lender = generateUser("user");
        LendableItem item = generateItem(lender);
    	BorrowingProcess process = new BorrowingProcess(item, period);
        process.setId(1L);
        transService.createTransactionRental(process, borrower, lender);
        ArgumentCaptor<TransactionRental> captorProcess = ArgumentCaptor.forClass(TransactionRental.class);
        Mockito.verify(transactions, times(1)).save(captorProcess.capture());
        Assert.assertTrue(captorProcess.getAllValues().get(0).getWholeRent() == 2);
    }

    @Test
    public void testSetDepositRevoked() {
        Period period = generatePeriod(1,2);
        User borrower = generateUser("user");
        User lender = generateUser("user");
        LendableItem item = generateItem(lender);
    	BorrowingProcess process = new BorrowingProcess(item, period);
        TransactionRental transRen = new TransactionRental(1, item.getDeposit(), process.getId(), item, borrower, lender);
        String status = "ja";
        transService.setDepositRevoked(transRen, status);
        ArgumentCaptor<TransactionRental> captorProcess = ArgumentCaptor.forClass(TransactionRental.class);
        Mockito.verify(transactions, times(1)).save(captorProcess.capture());
        Assert.assertTrue(captorProcess.getAllValues().get(0).getDepositRevoked().equals("ja"));
    }

    @Test
    public void testGetAllFromSender() {
        User sender = generateUser("Sender");
        User receiver = generateUser("Receiver");
        TransactionRental rent1 = new TransactionRental(200, 20, 1L, this.generateItem(sender), sender, receiver);
        TransactionRental rent2 = new TransactionRental(200, 20, 1L, this.generateItem(sender), sender, receiver);
        List<TransactionRental> bySender = new ArrayList<>();
        bySender.add(rent1);
        bySender.add(rent2);
        Mockito.when(transactions.findAllBySender(sender)).thenReturn(bySender);
        List<TransactionRental> returnList = transService.getAllFromSender(sender);
        Assertions.assertThat(returnList).isEqualTo(bySender);
    }

    @Test
    public void testGetAllFromReceiver() {
        User sender = generateUser("Sender");
        User receiver = generateUser("Receiver");
        TransactionRental rent1 = new TransactionRental(200, 20, 1L, this.generateItem(sender), sender, receiver);
        TransactionRental rent2 = new TransactionRental(200, 20, 1L, this.generateItem(sender), sender, receiver);
        List<TransactionRental> byReceiver = new ArrayList<>();
        byReceiver.add(rent1);
        byReceiver.add(rent2);
        Mockito.when(transactions.findAllByReceiver(receiver)).thenReturn(byReceiver);
        List<TransactionRental> returnList = transService.getAllFromReceiver(receiver);
        Assertions.assertThat(returnList).isEqualTo(byReceiver);
    }

    @Test
    public void testGetFromProcessId() {
        User sender = generateUser("Sender");
        User receiver = generateUser("Receiver");
        TransactionRental rent1 = new TransactionRental(200, 20, 1L, this.generateItem(sender), sender, receiver);
        Mockito.when(transactions.findByProcessId(1L)).thenReturn((Optional.of(rent1)));
        TransactionRental returnTrans = transService.getFromProcessId(1L);
        Assertions.assertThat(returnTrans).isEqualTo(rent1);
    }

    @Test(expected = RuntimeException.class)
    public void testGetFromProcessIdThrowsException() {
        Mockito.when(transactions.findByProcessId(1L)).thenReturn(Optional.empty());
        transService.getFromProcessId(1L);
    }

}
