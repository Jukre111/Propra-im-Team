package de.hhu.sharing.services;

import de.hhu.sharing.data.ConflictRepository;
import de.hhu.sharing.model.*;
import de.hhu.sharing.services.ConflictService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.internal.verification.VerificationModeFactory.times;

public class ConflictServiceTest{
    @Mock
    private ConflictRepository conflicts;

    @InjectMocks
    private ConflictService conflictService;

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

    public BorrowingProcess generateProcess(){
        User lender = generateUser();
        User borrower = generateUser();
        LendableItem item = generateItem(lender);

        BorrowingProcess process = new BorrowingProcess();
        process.setId(1L);
        process.setItem(item);
        lender.addToLend(process);
        borrower.addToBorrowed(process);
        return process;
    }

    @Test
    public void testGet() {
        User lender = generateUser();
        User borrower = generateUser();
        BorrowingProcess process = generateProcess();
        Message message = new Message();
        Conflict conflict = new Conflict(lender,borrower,process,message);
        Mockito.when(conflicts.findById(1L)).thenReturn(Optional.of(conflict));
        Conflict conflict2 = conflictService.get(1L);
        Assert.assertTrue(conflict.getLender().equals(conflict2.getLender()));
    }

    @Test
    public void testGetFromBorrowingProcess() {
        User lender = generateUser();
        User borrower = generateUser();
        BorrowingProcess process = generateProcess();
        Message message = new Message();
        Conflict conflict = new Conflict(lender,borrower,process,message);
        Mockito.when(conflicts.findByProcess(process)).thenReturn(conflict);
        Conflict conflict2 = conflictService.getFromBorrowingProcess(process);
        Assert.assertTrue(conflict.getLender().equals(conflict2.getLender()));
    }

   @Test
    public void testCreate(){
       User lender = generateUser();
       User borrower = generateUser();
       User prosecuter = generateUser();
       BorrowingProcess process = generateProcess();
       String problem = "problem";
       conflictService.create(lender,borrower,process,prosecuter, problem);
       ArgumentCaptor<Conflict> captor = ArgumentCaptor.forClass(Conflict.class);
       Mockito.verify(conflicts, times(1)).save(captor.capture());
       Assert.assertTrue(captor.getAllValues().get(0).getLender().equals(lender));

   }

   @Test
   public void testAddToMessages(){
        Conflict conflict = new Conflict();
        User user = generateUser();
        String message = "message";
        conflictService.addToMessages(conflict,user, message);
        ArgumentCaptor<Conflict> captor = ArgumentCaptor.forClass(Conflict.class);
        Mockito.verify(conflicts, times(1)).save(captor.capture());
        Assert.assertTrue(captor.getAllValues().get(0).getMessages().get(0).getContent().equals("message"));


   }

}
