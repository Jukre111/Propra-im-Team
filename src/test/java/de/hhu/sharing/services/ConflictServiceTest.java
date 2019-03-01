package de.hhu.sharing.services;

import de.hhu.sharing.data.ConflictRepository;
import de.hhu.sharing.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    private User generateUser() {
        LocalDate date = LocalDate.of(2000, 1, 1);
        Address address = new Address("unistrase", "duesseldorf", 40233);
        return new User("user", "password", "role", "lastnmae", "forname", "email", date, address);
    }

    private BorrowingProcess generateProcess(){
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

    private Conflict generateConflict() {
        User lender = generateUser();
        User borrower = generateUser();
        BorrowingProcess process = generateProcess();
        Message message = new Message();
        return new Conflict(lender, borrower, process, message);
    }

    @Test
    public void testGet() {
        Conflict conflict = generateConflict();
        Mockito.when(conflicts.findById(1L)).thenReturn(Optional.of(conflict));
        Conflict conflict2 = conflictService.get(1L);
        Assert.assertEquals(conflict.getLender(), conflict2.getLender());
    }

    @Test(expected = RuntimeException.class)
    public void testGetNotExistent() {
        Mockito.when(conflicts.findById(1L)).thenReturn(Optional.empty());
        conflictService.get(1L);
    }

    @Test
    public void testGetFromBorrowingProcess() {
        User lender = generateUser();
        User borrower = generateUser();
        BorrowingProcess process = generateProcess();
        Message message = new Message();
        Conflict conflict = new Conflict(lender, borrower, process, message);
        Mockito.when(conflicts.findByProcess(process)).thenReturn(conflict);
        Conflict conflict2 = conflictService.getFromBorrowingProcess(process);
        Assert.assertEquals(conflict.getLender(), conflict2.getLender());
    }

    @Test
    public void testGetAll(){
        Conflict conflict1 = generateConflict();
        Conflict conflict2 = generateConflict();
        List<Conflict> conflictList = new ArrayList<>();
        conflictList.add(conflict1);
        conflictList.add(conflict2);
        Mockito.when(conflicts.findAll()).thenReturn(conflictList);
        Assert.assertTrue(conflictService.getAll().contains(conflict1));
        Assert.assertTrue(conflictService.getAll().contains(conflict2));
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
       Assert.assertEquals(captor.getAllValues().get(0).getLender(), lender);
   }

   @Test
   public void testDelete(){
        Conflict conflict = generateConflict();
        conflictService.delete(conflict);
        ArgumentCaptor<Conflict> captor = ArgumentCaptor.forClass(Conflict.class);
        Mockito.verify(conflicts, times(1)).delete(captor.capture());
        Assert.assertEquals(captor.getValue(), conflict);
   }

   @Test
   public void testNoConflictWithFalse(){
        LendableItem lendableItem = generateItem(generateUser());
        Conflict conflict = generateConflict();
        List<Conflict> conflictList = new ArrayList<>();
        conflictList.add(conflict);
        Mockito.when(conflicts.findAllByProcess_Item(lendableItem)).thenReturn(conflictList);
        Assert.assertFalse(conflictService.noConflictWith(lendableItem));
   }

    @Test
    public void testNoConflictWithTrue(){
        LendableItem lendableItem = generateItem(generateUser());
        List<Conflict> conflictList = new ArrayList<>();
        Mockito.when(conflicts.findAllByProcess_Item(lendableItem)).thenReturn(conflictList);
        Assert.assertTrue(conflictService.noConflictWith(lendableItem));
    }

   @Test
   public void testAddToMessages(){
        Conflict conflict = new Conflict();
        User user = generateUser();
        String message = "message";
        conflictService.addToMessages(conflict,user, message);
        ArgumentCaptor<Conflict> captor = ArgumentCaptor.forClass(Conflict.class);
        Mockito.verify(conflicts, times(1)).save(captor.capture());
        Assert.assertEquals(captor.getAllValues().get(0).getMessages().get(0).getContent(),"message");
        Assert.assertEquals(captor.getAllValues().get(0).getMessages().get(0).getAuthor(),"user");
   }

}
