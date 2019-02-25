
package de.hhu.sharing.ServiceTests;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.ConflictService;
import de.hhu.sharing.services.ItemService;
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

public class ItemServiceTest {

    @Mock
    private ItemRepository items;

    @Mock
    ConflictService conflictService;

    @Mock
    StorageService storageService;

    @InjectMocks
    private ItemService itemService;

    @Before public void initialize(){
        MockitoAnnotations.initMocks(this);
    }

    private User generateUser(String username) {
        LocalDate birthdate = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User(username,"password", "role", "lastname", "forename", "email", birthdate, address);
        return user;
    }

    private Item generateItem(User user) {
        return new Item("apfel", "lecker", 1, 1, user);
    }

    private List<Item> generateItemList(User user){
        Item item = generateItem(user);
        Item item2 = generateItem(user);
        Item item3 = generateItem(user);
        ArrayList<Item> list = new ArrayList<>();
        list.add(item);
        list.add(item2);
        list.add(item3);
        return list;
    }

    @Test
    public void testCreateFileExists(){
        MockMultipartFile jsonFile = new MockMultipartFile("test.json", "", "application/json", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        User user = generateUser("dude");

        itemService.create("item","description",1,1,user, jsonFile);

        ArgumentCaptor<Item> captorItem1 = ArgumentCaptor.forClass(Item.class);
        Mockito.verify(items, times(1)).save(captorItem1.capture());

        ArgumentCaptor<Item> captorItem2 = ArgumentCaptor.forClass(Item.class);
        ArgumentCaptor<MockMultipartFile> captorMockMultipartFile = ArgumentCaptor.forClass(MockMultipartFile.class);
        Mockito.verify(storageService, times(1)).storeItem(captorMockMultipartFile.capture(),captorItem2.capture());

        Assert.assertEquals(captorItem1.getAllValues().get(0).getName(), "item");
        Assert.assertEquals(captorItem1.getAllValues().get(0).getLender(), user);
        Assert.assertEquals(captorItem2.getAllValues().get(0).getName(), "item");
        Assert.assertEquals(captorItem2.getAllValues().get(0).getLender(), user);
        Assert.assertEquals(captorMockMultipartFile.getValue(), jsonFile);
    }

    @Test
    public void testCreateFileNull(){
        MockMultipartFile jsonFile = null;
        User user = generateUser("dude");
        Item item = new Item("item","description",1,1,user);

        itemService.create("item","description",1,1,user, jsonFile);

        ArgumentCaptor<Item> captorItem1 = ArgumentCaptor.forClass(Item.class);
        Mockito.verify(items, times(1)).save(captorItem1.capture());

        Mockito.verify(storageService, times(0)).storeItem(jsonFile, item);

        Assert.assertEquals(captorItem1.getAllValues().get(0).getName(), "item");
        Assert.assertEquals(captorItem1.getAllValues().get(0).getLender(), user);

    }


    @Test
    public void testEdit(){
        User user1 = generateUser("user1");
        User user2 = generateUser("user2");
        Item item = generateItem(user1);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(item));
        itemService.edit(1L,"item","description",1,1,user2);
        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        Mockito.verify(items, times(1)).save(captor.capture());

        Assert.assertEquals(captor.getAllValues().get(0).getName(), "item");
        Assert.assertEquals(captor.getAllValues().get(0).getLender(), user2);
        Assert.assertNotEquals(captor.getAllValues().get(0).getLender(), user1);
    }

    @Test
    public void testDelete(){
        User user = generateUser("dude");
        Item item = generateItem(user);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(item));
        itemService.delete(1L);
        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        Mockito.verify(items, times(1)).delete(captor.capture());
        Assert.assertEquals(captor.getAllValues().get(0).getName(), "apfel");
        Assert.assertEquals(captor.getAllValues().get(0).getLender(), user);

    }

    @Test
    public void testGet(){
        User user = generateUser("dude");
        Item item = generateItem(user);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(item));
        Assert.assertTrue(itemService.get(1L).getName().equals("apfel"));
    }

    @Test
    public void testGetAll(){
        User user = generateUser("dude");
        Item item = generateItem(user);
        Item item2 = generateItem(user);
        Item item3 = generateItem(user);
        ArrayList<Item> liste = new ArrayList<>();
        liste.add(item);
        liste.add(item2);
        liste.add(item3);
        Mockito.when(items.findAll()).thenReturn(liste);
        Assert.assertEquals(itemService.getAll(), liste);
    }

    @Test
    public void testGetFromRequestId(){
        User user = generateUser("dude");
        Item item = generateItem(user);
        Mockito.when(items.findByRequests_id(1L)).thenReturn(Optional.of(item));
        Assert.assertEquals(itemService.getFromRequestId(1L), item);
    }

    @Test
    public void testGetAllIPosted(){
        User user = generateUser("dude");
        List<Item> list = generateItemList(user);
        Mockito.when(items.findAllByLender(user)).thenReturn(list);
        Assert.assertEquals(itemService.getAllIPosted(user), list);
    }

    @Test
    public void testGetAllIRequested(){
        User user = generateUser("dude");
        List<Item> list = generateItemList(user);
        Mockito.when(items.findAllByRequests_requester(user)).thenReturn(list);
        Assert.assertEquals(itemService.getAllIRequested(user),list);
    }


    @Test
    public void testSearchFor(){
        User user = generateUser("dude");
        List<Item> list = generateItemList(user);
        Mockito.when(items.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("lecker", "lecker")).thenReturn(list);
        Assert.assertEquals(itemService.searchFor("lecker"), list);
    }
}
