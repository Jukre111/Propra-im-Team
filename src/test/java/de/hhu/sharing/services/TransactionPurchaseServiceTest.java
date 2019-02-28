package de.hhu.sharing.services;

import static org.mockito.internal.verification.VerificationModeFactory.times;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import de.hhu.sharing.data.TransactionPurchaseRepository;
import de.hhu.sharing.data.TransactionRentalRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.BorrowingProcess;
import de.hhu.sharing.model.LendableItem;
import de.hhu.sharing.model.Period;
import de.hhu.sharing.model.Request;
import de.hhu.sharing.model.SellableItem;
import de.hhu.sharing.model.User;
import de.hhu.sharing.propay.Account;
import de.hhu.sharing.propay.Reservation;
import de.hhu.sharing.propay.TransactionPurchase;
import de.hhu.sharing.propay.TransactionRental;

public class TransactionPurchaseServiceTest {
    @Mock
    LendableItemRepository itemRepo;

    @Mock
    RequestRepository reqRepo;
    
    @Mock
    TransactionPurchaseRepository transactions;

    @Mock
    ProPayService proService;

    @InjectMocks
    TransactionPurchaseService transService;

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
    
    private SellableItem generateItem(User user) {
        return new SellableItem("apfel", "lecker", 1, user);
    }

    private Period generatePeriod(int startday, int endday){
        LocalDate startdate = LocalDate.of(2019,4,startday);
        LocalDate enddate = LocalDate.of(2019,4, endday);
        return new Period(startdate, enddate);
    }
    
    @Test
    public void testCreateTransactionPurchase() {
        User buyer = generateUser("user");
        User seller = generateUser("user");
        SellableItem item = generateItem(seller);
        transService.createTransactionPurchase(item,buyer,seller);
        ArgumentCaptor<TransactionPurchase> captorProcess = ArgumentCaptor.forClass(TransactionPurchase.class);
        Mockito.verify(transactions, times(1)).save(captorProcess.capture());
        Assert.assertTrue(captorProcess.getAllValues().get(0).getItemName().equals("apfel"));
    }

    @Test
    public void testGetAllFromSender() {
        User sender = generateUser("Sender");
        User receiver = generateUser("Receiver");
        TransactionPurchase buy1 = new TransactionPurchase(this.generateItem(sender), sender, receiver);
        TransactionPurchase buy2 = new TransactionPurchase(this.generateItem(sender), sender, receiver);
        List<TransactionPurchase> bySender = new ArrayList<>();
        bySender.add(buy1);
        bySender.add(buy2);
        Mockito.when(transactions.findAllBySender(sender)).thenReturn(bySender);
        List<TransactionPurchase> returnList = transService.getAllFromSender(sender);
        Assertions.assertThat(returnList).isEqualTo(bySender);
    }

    @Test
    public void testGetAllFromReceiver() {
        User sender = generateUser("Sender");
        User receiver = generateUser("Receiver");

        TransactionPurchase buy1 = new TransactionPurchase(this.generateItem(sender), sender, receiver);
        TransactionPurchase buy2 = new TransactionPurchase(this.generateItem(sender), sender, receiver);
        List<TransactionPurchase> byReceiver = new ArrayList<>();
        byReceiver.add(buy1);
        byReceiver.add(buy2);
        Mockito.when(transactions.findAllByReceiver(receiver)).thenReturn(byReceiver);
        List<TransactionPurchase> returnList = transService.getAllFromReceiver(receiver);
        Assertions.assertThat(returnList).isEqualTo(byReceiver);
    }

}

