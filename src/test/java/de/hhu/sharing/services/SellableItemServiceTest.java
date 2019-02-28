package de.hhu.sharing.services;

import de.hhu.sharing.data.LendableItemRepository;
import de.hhu.sharing.data.SellableItemRepository;
import de.hhu.sharing.data.LendableItemRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.LendableItem;
import de.hhu.sharing.model.Period;
import de.hhu.sharing.model.SellableItem;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.ConflictService;
import de.hhu.sharing.services.LendableItemService;
import de.hhu.sharing.storage.StorageService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;

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
    ConflictService conflictService;

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
    public void testCreateFileExists() {
        MockMultipartFile jsonFile = new MockMultipartFile("test.json", "", "application/json", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        User user = generateUser("dude");

        sellableItemService.create("item", "description", 1, user, jsonFile);

        ArgumentCaptor<SellableItem> captorItem1 = ArgumentCaptor.forClass(SellableItem.class);
        Mockito.verify(items, times(1)).save(captorItem1.capture());

        ArgumentCaptor<SellableItem> captorItem2 = ArgumentCaptor.forClass(SellableItem.class);
        ArgumentCaptor<MockMultipartFile> captorMockMultipartFile = ArgumentCaptor.forClass(MockMultipartFile.class);
        Mockito.verify(storageService, times(1)).storeSellableItem(captorMockMultipartFile.capture(), captorItem2.capture());

        Assert.assertEquals(captorItem1.getAllValues().get(0).getName(), "item");
        Assert.assertEquals(captorItem1.getAllValues().get(0).getOwner(), user);
        Assert.assertEquals(captorItem2.getAllValues().get(0).getName(), "item");
        Assert.assertEquals(captorItem2.getAllValues().get(0).getOwner(), user);
        Assert.assertEquals(captorMockMultipartFile.getValue(), jsonFile);
    }

    @Test
    public void testCreateFileNull() {
        MockMultipartFile jsonFile = null;
        User user = generateUser("dude");
        SellableItem item = new SellableItem("item", "description", 1, user);

        sellableItemService.create("item", "description", 1, user, jsonFile);

        ArgumentCaptor<SellableItem> captorItem1 = ArgumentCaptor.forClass(SellableItem.class);
        Mockito.verify(items, times(1)).save(captorItem1.capture());

        Mockito.verify(storageService, times(0)).storeSellableItem(jsonFile, item);

        Assert.assertEquals(captorItem1.getAllValues().get(0).getName(), "item");
        Assert.assertEquals(captorItem1.getAllValues().get(0).getOwner(), user);

    }


    @Test
    public void testEdit() {
        User user1 = generateUser("user1");
        User user2 = generateUser("user2");
        SellableItem item = generateItem(user1);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(item));
        sellableItemService.edit(1L, "item", "description", 1, user2);
        ArgumentCaptor<SellableItem> captor = ArgumentCaptor.forClass(SellableItem.class);
        Mockito.verify(items, times(1)).save(captor.capture());

        Assert.assertEquals(captor.getAllValues().get(0).getName(), "item");
        Assert.assertEquals(captor.getAllValues().get(0).getOwner(), user2);
        Assert.assertNotEquals(captor.getAllValues().get(0).getOwner(), user1);
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

}

