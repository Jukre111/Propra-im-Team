package de.hhu.sharing.services;

import de.hhu.sharing.data.SellableItemRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.SellableItem;
import de.hhu.sharing.model.User;
import de.hhu.sharing.storage.StorageService;
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

public class SellableItemServiceTest {

    @Mock
    private SellableItemRepository items;

    @Mock
    StorageService storageService;

    @InjectMocks
    private SellableItemService sellableItemService;

    @Before
    public void initialize() {
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

    private List<SellableItem> generateItemList(User user) {
    	SellableItem item = generateItem(user);
    	SellableItem item2 = generateItem(user);
    	SellableItem item3 = generateItem(user);
        ArrayList<SellableItem> list = new ArrayList<>();
        list.add(item);
        list.add(item2);
        list.add(item3);
        return list;
    }

    @Test
    public void testCreateFileNotNullContentTypeNotOctetStream(){
        MockMultipartFile file = new MockMultipartFile("test.gif", "", "image/gif", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        User user = generateUser("dude");

        sellableItemService.create("SellableItem","description",1,user, file);

        ArgumentCaptor<SellableItem> captorSellableItemSave = ArgumentCaptor.forClass(SellableItem.class);
        Mockito.verify(items, times(1)).save(captorSellableItemSave.capture());

        ArgumentCaptor<SellableItem> captorSellableItemStore = ArgumentCaptor.forClass(SellableItem.class);
        ArgumentCaptor<MultipartFile> captorMultipartFile = ArgumentCaptor.forClass(MultipartFile.class);
        Mockito.verify(storageService, times(1)).storeSellableItem(captorMultipartFile.capture(),captorSellableItemStore.capture());

        Assert.assertEquals(captorSellableItemSave.getValue().getName(), "SellableItem");
        Assert.assertEquals(captorSellableItemSave.getValue().getOwner(), user);
        Assert.assertEquals(captorSellableItemStore.getValue().getName(), "SellableItem");
        Assert.assertEquals(captorSellableItemStore.getValue().getOwner(), user);
        Assert.assertEquals(captorMultipartFile.getValue().getName(), "test.gif");
        Assert.assertEquals(captorMultipartFile.getValue(), file);
    }

    @Test
    public void testCreateFileNullContentTypeOctetStream(){
        MockMultipartFile file = new MockMultipartFile("test.gif", "", "application/octet-stream", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        User user = generateUser("dude");

        sellableItemService.create("SellableItem","description",1,user, file);

        ArgumentCaptor<SellableItem> captorSellableItemSave = ArgumentCaptor.forClass(SellableItem.class);
        Mockito.verify(items, times(1)).save(captorSellableItemSave.capture());
        Mockito.verify(storageService, times(0)).storeSellableItem(file ,captorSellableItemSave.getValue());

        Assert.assertEquals(captorSellableItemSave.getValue().getName(), "SellableItem");
        Assert.assertEquals(captorSellableItemSave.getValue().getOwner(), user);
    }


    @Test
    public void testCreateFileNull(){
        MockMultipartFile file = null;
        User user = generateUser("dude");

        sellableItemService.create("SellableItem","description",1,user, file);

        ArgumentCaptor<SellableItem> captorSellableItemSave = ArgumentCaptor.forClass(SellableItem.class);
        Mockito.verify(items, times(1)).save(captorSellableItemSave.capture());
        Mockito.verify(storageService, times(0)).storeSellableItem(file ,captorSellableItemSave.getValue());

        Assert.assertEquals(captorSellableItemSave.getValue().getName(), "SellableItem");
        Assert.assertEquals(captorSellableItemSave.getValue().getOwner(), user);
    }

    @Test
    public void testCreateExceptionThrown(){
        MockMultipartFile file = new MockMultipartFile("test.gif", "", null, "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));

        User user = generateUser("dude");

        sellableItemService.create("SellableItem","description",1,user, file);

        ArgumentCaptor<SellableItem> captorSellableItemSave = ArgumentCaptor.forClass(SellableItem.class);
        Mockito.verify(items, times(1)).save(captorSellableItemSave.capture());
        Mockito.verify(storageService, times(0)).storeSellableItem(file ,captorSellableItemSave.getValue());

        Assert.assertEquals(captorSellableItemSave.getValue().getName(), "SellableItem");
        Assert.assertEquals(captorSellableItemSave.getValue().getOwner(), user);
    }


    @Test
    public void testEditFileNotNullContentTypeNotOctetStream(){
        MockMultipartFile file = new MockMultipartFile("test.gif", "", "image/gif", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        User firstOwner = generateUser("user1");
        User secondOwner = generateUser("user2");
        SellableItem sellableItem = generateItem(firstOwner);
        sellableItem.setId(1L);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(sellableItem));

        sellableItemService.edit(1L,"SellableItem","description",1,secondOwner, file);

        ArgumentCaptor<SellableItem> captorSellableItemSave = ArgumentCaptor.forClass(SellableItem.class);
        Mockito.verify(items, times(1)).save(captorSellableItemSave.capture());

        ArgumentCaptor<SellableItem> captorSellableItemStore = ArgumentCaptor.forClass(SellableItem.class);
        ArgumentCaptor<MultipartFile> captorMultipartFile = ArgumentCaptor.forClass(MultipartFile.class);
        Mockito.verify(storageService, times(1)).storeSellableItem(captorMultipartFile.capture(),captorSellableItemStore.capture());

        Assert.assertEquals(captorSellableItemSave.getValue().getName(), "SellableItem");
        Assert.assertNotEquals(captorSellableItemSave.getValue().getOwner(), firstOwner);
        Assert.assertEquals(captorSellableItemSave.getValue().getOwner(), secondOwner);
        Assert.assertEquals(captorSellableItemStore.getValue().getName(), "SellableItem");
        Assert.assertNotEquals(captorSellableItemStore.getValue().getOwner(), firstOwner);
        Assert.assertEquals(captorSellableItemStore.getValue().getOwner(), secondOwner);
        Assert.assertEquals(captorMultipartFile.getValue().getName(), "test.gif");
        Assert.assertEquals(captorMultipartFile.getValue(), file);
    }

    @Test
    public void testEditFileNullContentTypeOctetStream(){
        MockMultipartFile file = new MockMultipartFile("test.gif", "", "application/octet-stream", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        User firstOwner = generateUser("user1");
        User secondOwner = generateUser("user2");
        SellableItem sellableItem = generateItem(firstOwner);
        sellableItem.setId(1L);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(sellableItem));

        sellableItemService.edit(1L,"SellableItem","description",1,secondOwner, file);

        ArgumentCaptor<SellableItem> captorSellableItemSave = ArgumentCaptor.forClass(SellableItem.class);
        Mockito.verify(items, times(1)).save(captorSellableItemSave.capture());
        Mockito.verify(storageService, times(0)).storeSellableItem(file ,captorSellableItemSave.getValue());

        Assert.assertEquals(captorSellableItemSave.getValue().getName(), "SellableItem");
        Assert.assertNotEquals(captorSellableItemSave.getValue().getOwner(), firstOwner);
        Assert.assertEquals(captorSellableItemSave.getValue().getOwner(), secondOwner);
    }


    @Test
    public void testEditFileNull(){
        MockMultipartFile file = null;
        User firstOwner = generateUser("user1");
        User secondOwner = generateUser("user2");
        SellableItem sellableItem = generateItem(firstOwner);
        sellableItem.setId(1L);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(sellableItem));

        sellableItemService.edit(1L,"SellableItem","description",1,secondOwner, file);

        ArgumentCaptor<SellableItem> captorSellableItemSave = ArgumentCaptor.forClass(SellableItem.class);
        Mockito.verify(items, times(1)).save(captorSellableItemSave.capture());
        Mockito.verify(storageService, times(0)).storeSellableItem(file ,captorSellableItemSave.getValue());

        Assert.assertEquals(captorSellableItemSave.getValue().getName(), "SellableItem");
        Assert.assertEquals(captorSellableItemSave.getValue().getOwner(), secondOwner);
        Assert.assertNotEquals(captorSellableItemSave.getValue().getOwner(), firstOwner);
    }

    @Test
    public void testEditExceptionThrown(){
        MockMultipartFile file = new MockMultipartFile("test.gif", "", null, "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        User firstOwner = generateUser("user1");
        User secondOwner = generateUser("user2");
        SellableItem sellableItem = generateItem(firstOwner);
        sellableItem.setId(1L);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(sellableItem));

        sellableItemService.edit(1L,"SellableItem","description",1,secondOwner, file);

        ArgumentCaptor<SellableItem> captorSellableItemSave = ArgumentCaptor.forClass(SellableItem.class);
        Mockito.verify(items, times(1)).save(captorSellableItemSave.capture());
        Mockito.verify(storageService, times(0)).storeSellableItem(file ,captorSellableItemSave.getValue());

        Assert.assertEquals(captorSellableItemSave.getValue().getName(), "SellableItem");
        Assert.assertEquals(captorSellableItemSave.getValue().getOwner(), secondOwner);
        Assert.assertNotEquals(captorSellableItemSave.getValue().getOwner(), firstOwner);
    }

    @Test
    public void testDelete() {
        User user = generateUser("dude");
        SellableItem item = generateItem(user);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(item));
        sellableItemService.delete(1L);
        ArgumentCaptor<SellableItem> captor = ArgumentCaptor.forClass(SellableItem.class);
        Mockito.verify(items, times(1)).delete(captor.capture());
        Assert.assertEquals(captor.getAllValues().get(0).getName(), "apfel");
        Assert.assertEquals(captor.getAllValues().get(0).getOwner(), user);

    }

    @Test
    public void testGetItemFound() {
        User user = generateUser("dude");
        SellableItem item = generateItem(user);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(item));
        Assert.assertEquals(sellableItemService.get(1L).getName(), "apfel");
    }

    @Test(expected = RuntimeException.class)
    public void testGetItemNotFound() {
        Mockito.when(items.findById(1L)).thenReturn(Optional.empty());
        sellableItemService.get(1L);
    }

    @Test
    public void testGetAll() {
        User user = generateUser("username");
        List<SellableItem> list = generateItemList(user);
        Mockito.when(items.findAll()).thenReturn(list);
        Assert.assertEquals(sellableItemService.getAll(), list);
    }

    @Test
    public void testGetAllIPosted() {
        User user = generateUser("dude");
        List<SellableItem> list = generateItemList(user);
        Mockito.when(items.findAllByOwner(user)).thenReturn(list);
        Assert.assertEquals(sellableItemService.getAllIPosted(user), list);
    }

    @Test
    public void testSearchFor() {
        User user = generateUser("dude");
        List<SellableItem> list = generateItemList(user);
        Mockito.when(items.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("lecker", "lecker")).thenReturn(list);
        Assert.assertEquals(sellableItemService.searchFor("lecker"), list);
    }

    @Test
    public void testIsOwner(){
        User user = generateUser("testman");
        SellableItem item = new SellableItem();
        item.setId(1L);
        item.setOwner(user);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(item));
        Assert.assertTrue(sellableItemService.isOwner(1L,user));
    }

    @Test
    public void testIsNotOwner(){
        User user = generateUser("testman");
        SellableItem item = new SellableItem();
        item.setId(1L);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(item));
        Assert.assertFalse(sellableItemService.isOwner(1L,user));
    }

}

