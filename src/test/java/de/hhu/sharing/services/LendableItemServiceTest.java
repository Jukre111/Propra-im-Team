
package de.hhu.sharing.services;

import de.hhu.sharing.data.LendableItemRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.LendableItem;
import de.hhu.sharing.model.Period;
import de.hhu.sharing.model.User;
import de.hhu.sharing.storage.StorageService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class LendableItemServiceTest {

    @Mock
    private LendableItemRepository items;

    @Mock
    private ConflictService conflictService;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private LendableItemService lendableItemService;

    @Before public void initialize(){
        MockitoAnnotations.initMocks(this);
    }

    private User generateUser(String username) {
        LocalDate birthdate = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User(username,"password", "role", "lastname", "forename", "email", birthdate, address);
        return user;
    }

    private LendableItem generateItem(User user) {
        return new LendableItem("apfel", "lecker", 1, 1, user);
    }

    private List<LendableItem> generateItemList(User user){
        LendableItem lendableItem = generateItem(user);
        LendableItem lendableItem2 = generateItem(user);
        LendableItem lendableItem3 = generateItem(user);
        ArrayList<LendableItem> list = new ArrayList<>();
        list.add(lendableItem);
        list.add(lendableItem2);
        list.add(lendableItem3);
        return list;
    }

    @Test
    public void testCreateFileNotNullContentTypeNotOctetStream(){
        MockMultipartFile file = new MockMultipartFile("test.gif", "", "image/gif", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        User user = generateUser("dude");

        lendableItemService.create("LendableItem","description",1,1,user, file);

        ArgumentCaptor<LendableItem> captorLendableItemSave = ArgumentCaptor.forClass(LendableItem.class);
        Mockito.verify(items, times(1)).save(captorLendableItemSave.capture());

        ArgumentCaptor<LendableItem> captorLendableItemStore = ArgumentCaptor.forClass(LendableItem.class);
        ArgumentCaptor<MultipartFile> captorMultipartFile = ArgumentCaptor.forClass(MultipartFile.class);
        Mockito.verify(storageService, times(1)).storeLendableItem(captorMultipartFile.capture(),captorLendableItemStore.capture());

        Assert.assertEquals(captorLendableItemSave.getValue().getName(), "LendableItem");
        Assert.assertEquals(captorLendableItemSave.getValue().getOwner(), user);
        Assert.assertEquals(captorLendableItemStore.getValue().getName(), "LendableItem");
        Assert.assertEquals(captorLendableItemStore.getValue().getOwner(), user);
        Assert.assertEquals(captorMultipartFile.getValue().getName(), "test.gif");
        Assert.assertEquals(captorMultipartFile.getValue(), file);
    }

    @Test
    public void testCreateFileNullContentTypeOctetStream(){
        MockMultipartFile file = new MockMultipartFile("test.gif", "", "application/octet-stream", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        User user = generateUser("dude");

        lendableItemService.create("LendableItem","description",1,1,user, file);

        ArgumentCaptor<LendableItem> captorLendableItemSave = ArgumentCaptor.forClass(LendableItem.class);
        Mockito.verify(items, times(1)).save(captorLendableItemSave.capture());
        Mockito.verify(storageService, times(0)).storeLendableItem(file ,captorLendableItemSave.getValue());

        Assert.assertEquals(captorLendableItemSave.getValue().getName(), "LendableItem");
        Assert.assertEquals(captorLendableItemSave.getValue().getOwner(), user);
    }


    @Test
    public void testCreateFileNull(){
        MockMultipartFile file = null;
        User user = generateUser("dude");

        lendableItemService.create("LendableItem","description",1,1,user, file);

        ArgumentCaptor<LendableItem> captorLendableItemSave = ArgumentCaptor.forClass(LendableItem.class);
        Mockito.verify(items, times(1)).save(captorLendableItemSave.capture());
        Mockito.verify(storageService, times(0)).storeLendableItem(file ,captorLendableItemSave.getValue());

        Assert.assertEquals(captorLendableItemSave.getValue().getName(), "LendableItem");
        Assert.assertEquals(captorLendableItemSave.getValue().getOwner(), user);
    }

    @Test
    public void testCreateExceptionThrown(){
        MockMultipartFile file = new MockMultipartFile("test.gif", "", null, "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));

        User user = generateUser("dude");

        lendableItemService.create("LendableItem","description",1,1,user, file);

        ArgumentCaptor<LendableItem> captorLendableItemSave = ArgumentCaptor.forClass(LendableItem.class);
        Mockito.verify(items, times(1)).save(captorLendableItemSave.capture());
        Mockito.verify(storageService, times(0)).storeLendableItem(file ,captorLendableItemSave.getValue());

        Assert.assertEquals(captorLendableItemSave.getValue().getName(), "LendableItem");
        Assert.assertEquals(captorLendableItemSave.getValue().getOwner(), user);
    }

    @Test
    public void testEditFileNotNullContentTypeNotOctetStream(){
        MockMultipartFile file = new MockMultipartFile("test.gif", "", "image/gif", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        User firstOwner = generateUser("user1");
        User secondOwner = generateUser("user2");
        LendableItem lendableItem = generateItem(firstOwner);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(lendableItem));

        lendableItemService.edit(1L,"LendableItem","description",1,1, secondOwner, file);

        ArgumentCaptor<LendableItem> captorLendableItemSave = ArgumentCaptor.forClass(LendableItem.class);
        Mockito.verify(items, times(1)).save(captorLendableItemSave.capture());

        ArgumentCaptor<LendableItem> captorLendableItemStore = ArgumentCaptor.forClass(LendableItem.class);
        ArgumentCaptor<MultipartFile> captorMultipartFile = ArgumentCaptor.forClass(MultipartFile.class);
        Mockito.verify(storageService, times(1)).storeLendableItem(captorMultipartFile.capture(),captorLendableItemStore.capture());

        Assert.assertEquals(captorLendableItemSave.getValue().getName(), "LendableItem");
        Assert.assertEquals(captorLendableItemSave.getValue().getOwner(), secondOwner);
        Assert.assertNotEquals(captorLendableItemSave.getValue().getOwner(), firstOwner);
        Assert.assertEquals(captorLendableItemStore.getValue().getName(), "LendableItem");
        Assert.assertEquals(captorLendableItemStore.getValue().getOwner(), secondOwner);
        Assert.assertNotEquals(captorLendableItemStore.getValue().getOwner(), firstOwner);
        Assert.assertEquals(captorMultipartFile.getValue(), file);
    }

    @Test
    public void testEditFileNotNullContentTypeOctetStream(){
        MockMultipartFile file = new MockMultipartFile("test.gif", "", "application/octet-stream", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        User firstOwner = generateUser("user1");
        User secondOwner = generateUser("user2");
        LendableItem lendableItem = generateItem(firstOwner);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(lendableItem));

        lendableItemService.edit(1L,"LendableItem","description",1,1, secondOwner, file);

        ArgumentCaptor<LendableItem> captorLendableItemSave = ArgumentCaptor.forClass(LendableItem.class);
        Mockito.verify(items, times(1)).save(captorLendableItemSave.capture());

        Mockito.verify(storageService, times(0)).storeLendableItem(file,captorLendableItemSave.getValue());

        Assert.assertEquals(captorLendableItemSave.getValue().getName(), "LendableItem");
        Assert.assertEquals(captorLendableItemSave.getValue().getOwner(), secondOwner);
        Assert.assertNotEquals(captorLendableItemSave.getValue().getOwner(), firstOwner);
    }

    @Test
    public void testEditFileNull(){
        MockMultipartFile file = new MockMultipartFile("test.gif", "", null, "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        User firstOwner = generateUser("user1");
        User secondOwner = generateUser("user2");
        LendableItem lendableItem = generateItem(firstOwner);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(lendableItem));

        lendableItemService.edit(1L,"LendableItem","description",1,1, secondOwner, file);

        ArgumentCaptor<LendableItem> captorLendableItemSave = ArgumentCaptor.forClass(LendableItem.class);
        Mockito.verify(items, times(1)).save(captorLendableItemSave.capture());

        Mockito.verify(storageService, times(0)).storeLendableItem(file,captorLendableItemSave.getValue());

        Assert.assertEquals(captorLendableItemSave.getValue().getName(), "LendableItem");
        Assert.assertEquals(captorLendableItemSave.getValue().getOwner(), secondOwner);
        Assert.assertNotEquals(captorLendableItemSave.getValue().getOwner(), firstOwner);
    }

    @Test
    public void testEditException(){
        MockMultipartFile file =null;
        User firstOwner = generateUser("user1");
        User secondOwner = generateUser("user2");
        LendableItem lendableItem = generateItem(firstOwner);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(lendableItem));

        lendableItemService.edit(1L,"LendableItem","description",1,1, secondOwner, file);

        ArgumentCaptor<LendableItem> captorLendableItemSave = ArgumentCaptor.forClass(LendableItem.class);
        Mockito.verify(items, times(1)).save(captorLendableItemSave.capture());

        Mockito.verify(storageService, times(0)).storeLendableItem(file,captorLendableItemSave.getValue());

        Assert.assertEquals(captorLendableItemSave.getValue().getName(), "LendableItem");
        Assert.assertEquals(captorLendableItemSave.getValue().getOwner(), secondOwner);
        Assert.assertNotEquals(captorLendableItemSave.getValue().getOwner(), firstOwner);
    }

    @Test
    public void testDelete(){
        User user = generateUser("dude");
        LendableItem lendableItem = generateItem(user);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(lendableItem));
        lendableItemService.delete(1L);
        ArgumentCaptor<LendableItem> captor = ArgumentCaptor.forClass(LendableItem.class);
        Mockito.verify(items, times(1)).delete(captor.capture());
        Assert.assertEquals(captor.getAllValues().get(0).getName(), "apfel");
        Assert.assertEquals(captor.getAllValues().get(0).getOwner(), user);

    }

    @Test
    public void testGet(){
        User user = generateUser("dude");
        LendableItem lendableItem = generateItem(user);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(lendableItem));
        Assert.assertTrue(lendableItemService.get(1L).getName().equals("apfel"));
    }

    @Test (expected = RuntimeException.class)
    public void testGetNotExistent(){
        Mockito.when(items.findById(1L)).thenReturn(Optional.empty());
        lendableItemService.get(1L);
    }

    @Test
    public void testGetAll(){
        User user = generateUser("testman");
        List<LendableItem> liste = generateItemList(user);
        Mockito.when(items.findAll()).thenReturn(liste);
        Assert.assertEquals(lendableItemService.getAll(), liste);
    }

    @Test
    public void testGetFromRequestId(){
        User user = generateUser("dude");
        LendableItem lendableItem = generateItem(user);
        Mockito.when(items.findByRequests_id(1L)).thenReturn(Optional.of(lendableItem));
        Assert.assertEquals(lendableItemService.getFromRequestId(1L), lendableItem);
    }

    @Test(expected = RuntimeException.class)
    public void testGetFromRequestIdNotExistent(){
        Mockito.when(items.findByRequests_id(1L)).thenReturn(Optional.empty());
        lendableItemService.getFromRequestId(1L);
    }

    @Test
    public void testGetAllIPosted(){
        User user = generateUser("dude");
        List<LendableItem> list = generateItemList(user);
        Mockito.when(items.findAllByOwner(user)).thenReturn(list);
        Assert.assertEquals(lendableItemService.getAllIPosted(user), list);
    }


    @Test
    public void testGetAllIRequested(){
        User user = generateUser("dude");
        List<LendableItem> list = generateItemList(user);
        Mockito.when(items.findAllByRequests_requester(user)).thenReturn(list);
        Assert.assertEquals(lendableItemService.getAllIRequested(user),list);
    }

    @Test
    public void testSearchFor(){
        User user = generateUser("dude");
        List<LendableItem> list = generateItemList(user);
        Mockito.when(items.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("lecker", "lecker")).thenReturn(list);
        Assert.assertEquals(lendableItemService.searchFor("lecker"), list);
    }

    @Test
    public void testIsChangeable() {
        LendableItem item = new LendableItem();
        item.setId(1L);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(item));
        Mockito.when(conflictService.noConflictWith(item)).thenReturn(true);
        Assert.assertTrue(lendableItemService.isChangeable(1L));
    }

    @Test
    public void testIsChangeableWithPeriod() {
        LendableItem item = new LendableItem();
        item.setId(1L);
        item.addToPeriods(new Period(LocalDate.of(2000,1,1),LocalDate.of(2000,2,2)));
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(item));
        Mockito.when(conflictService.noConflictWith(item)).thenReturn(true);
        Assert.assertFalse(lendableItemService.isChangeable(1L));
    }

    @Test
    public void testIsChangeableWithConflict() {
        LendableItem item = new LendableItem();
        item.setId(1L);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(item));
        Mockito.when(conflictService.noConflictWith(item)).thenReturn(false);
        Assert.assertFalse(lendableItemService.isChangeable(1L));
    }

    @Test
    public void testIsChangeableWithPeriodWithConflict() {
        LendableItem item = new LendableItem();
        item.setId(1L);
        item.addToPeriods(new Period(LocalDate.of(2000,1,1),LocalDate.of(2000,2,2)));
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(item));
        Mockito.when(conflictService.noConflictWith(item)).thenReturn(false);
        Assert.assertFalse(lendableItemService.isChangeable(1L));
    }


    @Test
    public void testIsAvailableAt(){
        LendableItem item = new LendableItem();
        LocalDate startDate = LocalDate.of(2000,1,1);
        LocalDate endDate = LocalDate.of(2000,2,2);
        Assert.assertTrue(lendableItemService.isAvailableAt(item,startDate,endDate));
    }

   @Test
    public void testIsNotAvailableAt(){
        LendableItem item = new LendableItem();
        Period period = new Period(LocalDate.of(2000,1,2),LocalDate.of(2000,1,3));
        item.addToPeriods(period);
        LocalDate startDate = LocalDate.of(2000,1,1);
        LocalDate endDate = LocalDate.of(2000,2,2);
        Assert.assertFalse(lendableItemService.isAvailableAt(item,startDate,endDate));
    }

    @Test
    public void testIsOwner(){
        User user = generateUser("testman");
        LendableItem item = new LendableItem();
        item.setId(1L);
        item.setOwner(user);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(item));
        Assert.assertTrue(lendableItemService.isOwner(1L,user));
    }

    @Test
    public void testIsNotOwner(){
        User user = generateUser("testman");
        LendableItem item = new LendableItem();
        item.setId(1L);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(item));
        Assert.assertFalse(lendableItemService.isOwner(1L,user));
    }

    @Test
    public void testAllDatesInbetween(){

        LendableItem item = new LendableItem();
        Period period = new Period(LocalDate.of(2000,1,1),LocalDate.of(2000,1,3));
        item.addToPeriods(period);

        LocalDate date1 = LocalDate.of(2000,1,1);
        LocalDate date2 = LocalDate.of(2000,1,2);
        LocalDate date3 = LocalDate.of(2000,1,3);

        List <LocalDate> allDates = new ArrayList<>();
        allDates.add(date1);
        allDates.add(date2);
        allDates.add(date3);

        Assertions.assertThat(lendableItemService.allDatesInbetween(item)).isEqualTo(allDates);


    }
}
