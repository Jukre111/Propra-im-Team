package de.hhu.sharing.ServiceTests;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.TransactionRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.ItemService;
import de.hhu.sharing.services.ProPayService;
import de.hhu.sharing.services.RequestService;
import de.hhu.sharing.services.UserService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.internal.verification.VerificationModeFactory.times;

public class ItemServiceTest {

    @Mock
    private ItemRepository items;

    @InjectMocks
    private ItemService itemService;

    @Before public void initialize(){
        MockitoAnnotations.initMocks(this);
    }

    public User generateUser() {
        LocalDate date = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User("user","password", "role", "lastnmae", "forname", "email",date,address);
         return user;
    }

    @Test
    public void testCreate(){
        User user = generateUser();
        itemService.create("item","description",1,1,user);
        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        Mockito.verify(items, times(1)).save(captor.capture());
        Assert.assertTrue(captor.getAllValues().get(0).getName().equals("item"));
    }


    @Test
    public void testEdit(){
        User user = generateUser();
        Item item = new Item("apfel", "lecker",1,1 ,user );
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(item));
        itemService.edit(1L,"item","description",1,1,user);
        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        Mockito.verify(items, times(1)).save(captor.capture());
        Assert.assertTrue(captor.getAllValues().get(0).getName().equals("item"));
    }

    @Test
    public void testDelete(){
        User user = generateUser();
        Item item = new Item("apfel", "lecker",1,1 ,user );
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(item));
        itemService.delete(1L);
        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        Mockito.verify(items, times(1)).delete(captor.capture());
        Assert.assertTrue(captor.getAllValues().get(0).getName().equals("apfel"));

    }

    @Test
    public void testGet(){
        User user = generateUser();
        Item item = new Item("apfel", "lecker",1,1 ,user );
        Mockito.when(items.findById(1L)).thenReturn(Optional.of(item));
        Assert.assertTrue(itemService.get(1L).getName().equals("apfel"));
    }

    @Test
    public void testGetAll(){
        User user = generateUser();
        Item item = new Item("apfel", "lecker",1,1 ,user );
        Item item2 = new Item("apfel", "lecker",1,1 ,user );
        Item item3 = new Item("apfel", "lecker",1,1 ,user );
        ArrayList<Item> liste = new ArrayList<>();
        liste.add(item);
        liste.add(item2);
        liste.add(item3);
        Mockito.when(items.findAll()).thenReturn(liste);
        Assert.assertTrue(itemService.getAll().equals(liste));
    }

    @Test
    public void testGetFromRequestId(){

        User user = generateUser();
        Item item = new Item("apfel", "lecker",1,1 ,user );
        Mockito.when(items.findByRequests_id(1L)).thenReturn(Optional.of(item));
        Assert.assertTrue(itemService.getFromRequestId(1L).equals(item));
    }

    @Test
    public void testGetAllIPosted(){
        User user = generateUser();
        Item item = new Item("apfel", "lecker",1,1 ,user );
        Item item2 = new Item("apfel", "lecker",1,1 ,user );
        Item item3 = new Item("apfel", "lecker",1,1 ,user );
        ArrayList<Item> liste = new ArrayList<>();
        liste.add(item);
        liste.add(item2);
        liste.add(item3);
        Mockito.when(items.findAllByLender(user)).thenReturn(liste);
        Assert.assertTrue(itemService.getAllIPosted(user).equals(liste));
    }

    @Test
    public void testGetAllIRequested(){
        User user = generateUser();
        Item item = new Item("apfel", "lecker",1,1 ,user );
        Item item2 = new Item("apfel", "lecker",1,1 ,user );
        Item item3 = new Item("apfel", "lecker",1,1 ,user );
        ArrayList<Item> liste = new ArrayList<>();
        liste.add(item);
        liste.add(item2);
        liste.add(item3);
        Mockito.when(items.findAllByRequests_requester(user)).thenReturn(liste);
        Assert.assertTrue(itemService.getAllIRequested(user).equals(liste));
    }
}
