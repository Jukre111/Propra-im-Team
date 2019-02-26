
package de.hhu.sharing.ServiceTests;

import de.hhu.sharing.data.LendableItemRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.LendableItem;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.ItemService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class lendableItemServiceTest {

    @Mock
    private LendableItemRepository items;

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
    public void testCreate(){
        MultipartFile file = null;
        User user = generateUser("dude");
        itemService.create("LendableItem","description",1,1,user, file);
        ArgumentCaptor<LendableItem> captor = ArgumentCaptor.forClass(LendableItem.class);
        Mockito.verify(items, times(1)).save(captor.capture());
        Assert.assertEquals(captor.getAllValues().get(0).getName(), "LendableItem");
        Assert.assertEquals(captor.getAllValues().get(0).getOwner(), user);
    }


    @Test
    public void testEdit(){
        User user1 = generateUser("user1");
        User user2 = generateUser("user2");
        LendableItem lendableItem = generateItem(user1);
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(lendableItem));
        itemService.edit(1L,"LendableItem","description",1,1,user2);
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
        itemService.delete(1L);
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
        Assert.assertTrue(itemService.get(1L).getName().equals("apfel"));
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
        Assert.assertEquals(itemService.getAll(), liste);
    }

    @Test
    public void testGetFromRequestId(){
        User user = generateUser("dude");
        LendableItem lendableItem = generateItem(user);
        Mockito.when(items.findByRequests_id(1L)).thenReturn(Optional.of(lendableItem));
        Assert.assertEquals(itemService.getFromRequestId(1L), lendableItem);
    }

    @Test
    public void testGetAllIPosted(){
        User user = generateUser("dude");
        List<LendableItem> list = generateItemList(user);
        Mockito.when(items.findAllByOwner(user)).thenReturn(list);
        Assert.assertEquals(itemService.getAllIPosted(user), list);
    }

    @Test
    public void testGetAllIRequested(){
        User user = generateUser("dude");
        List<LendableItem> list = generateItemList(user);
        Mockito.when(items.findAllByRequests_requester(user)).thenReturn(list);
        Assert.assertEquals(itemService.getAllIRequested(user),list);
    }


    @Test
    public void testSearchFor(){
        User user = generateUser("dude");
        List<LendableItem> list = generateItemList(user);
        Mockito.when(items.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("lecker", "lecker")).thenReturn(list);
        Assert.assertEquals(itemService.searchFor("lecker"), list);
    }
}
