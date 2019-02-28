
package de.hhu.sharing.services;

import de.hhu.sharing.data.LendableItemRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.LendableItem;
import de.hhu.sharing.model.Period;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.LendableItemService;
import de.hhu.sharing.storage.StorageService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Null;
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
    public void testCreateFileIsNull(){
        MultipartFile file = null;
        User user = generateUser("dude");
        lendableItemService.create("LendableItem","description",1,1,user, file);
        ArgumentCaptor<LendableItem> captor = ArgumentCaptor.forClass(LendableItem.class);
        Mockito.verify(items, times(1)).save(captor.capture());
        Assert.assertEquals(captor.getAllValues().get(0).getName(), "LendableItem");
        Assert.assertEquals(captor.getAllValues().get(0).getOwner(), user);
    }

    @Test
    public void testCreate(){
        MockMultipartFile jsonFile = new MockMultipartFile("test.gif", "", "image/gif", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        User user = generateUser("dude");
        lendableItemService.create("LendableItem","description",1,1,user, jsonFile);
        ArgumentCaptor<LendableItem> captor = ArgumentCaptor.forClass(LendableItem.class);
        ArgumentCaptor<MultipartFile> captor1 = ArgumentCaptor.forClass(MultipartFile.class);
        Mockito.verify(storageService, times(1)).storeLendableItem(captor1.capture(),captor.capture());

        Assert.assertEquals(captor.getAllValues().get(0).getName(), "LendableItem");
        Assert.assertEquals(captor1.getAllValues().get(0).getName(), "test.gif");


    }


    @Test
    public void testEdit(){
        User user1 = generateUser("user1");
        User user2 = generateUser("user2");
        LendableItem lendableItem = generateItem(user1);
        MockMultipartFile jsonFile = new MockMultipartFile("test.gif", "", "image/gif", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(lendableItem));
        lendableItemService.edit(1L,"LendableItem","description",1,1,user2, jsonFile);
        ArgumentCaptor<LendableItem> captor = ArgumentCaptor.forClass(LendableItem.class);
        Mockito.verify(items, times(1)).save(captor.capture());

        Assert.assertEquals(captor.getAllValues().get(0).getName(), "LendableItem");
        Assert.assertEquals(captor.getAllValues().get(0).getOwner(), user2);
        Assert.assertNotEquals(captor.getAllValues().get(0).getOwner(), user1);
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
        lendableItemService.get(1L).getName();
    }

    @Test
    public void testGetAll(){
        User user = generateUser("dude");
        LendableItem lendableItem = generateItem(user);
        LendableItem lendableItem2 = generateItem(user);
        LendableItem lendableItem3 = generateItem(user);
        ArrayList<LendableItem> liste = new ArrayList<>();
        liste.add(lendableItem);
        liste.add(lendableItem2);
        liste.add(lendableItem3);
        Mockito.when(items.findAll()).thenReturn(liste);
        Assert.assertEquals(lendableItemService.getAll(), liste);
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
    public void testIsAvailableAt(){
        LendableItem item = new LendableItem();
        LocalDate startDate = LocalDate.of(2000,1,1);
        LocalDate endDate = LocalDate.of(2000,2,2);
        Assert.assertTrue(lendableItemService.isAvailableAt(item,startDate,endDate));
    }

   /* @Test
    public void testIsNotAvailableAt(){
        LendableItem item = new LendableItem();
        Period period = new Period(LocalDate.of(2000,1,2),LocalDate.of(2000,1,3));
        item.addToPeriods(period);
        LocalDate startDate = LocalDate.of(2000,1,1);
        LocalDate endDate = LocalDate.of(2000,2,2);
        Assert.assertTrue(lendableItemService.isAvailableAt(item,startDate,endDate));
    }*/

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
