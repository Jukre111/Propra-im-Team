package de.hhu.sharing;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ItemControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    WebApplicationContext webContext;

    @MockBean
    ItemRepository itemRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RequestRepository requestRepository;



   @Test
   @WithMockUser
    public void retrieveStatusDetails() throws Exception{
       Mockito.when(itemRepository.findById((long)1)).thenReturn(Optional.of(getItemIdOne()));

       mvc.perform(MockMvcRequestBuilders.get("/details").param("id","1"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test(expected = NestedServletException.class)
    @WithMockUser
    public void retrieveStatusDetailsNotExistent() throws Exception{
        Mockito.when(itemRepository.findById((long)1)).thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get("/details").param("id","1"));
    }


    @Test
    @WithMockUser
    public void retrieveStatusItemWithoutId() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/item"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }


    @Test
    @WithMockUser
    public void retrieveStatusItemWithId() throws Exception{
        Item item = new Item();
        Mockito.when(itemRepository.findById((long)1)).thenReturn(Optional.of(item));

        mvc.perform(MockMvcRequestBuilders.get("/item").param("id","1"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test (expected = NestedServletException.class)
    @WithMockUser
    public void retrieveStatusItemWithIdNotExistent() throws Exception{
        Mockito.when(itemRepository.findById((long)1)).thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get("/item").param("id","1"));
    }



    /*@Test
    @WithMockUser
    public void retrieveStatusSaveItem() throws Exception{

            mvc.perform(MockMvcRequestBuilders.get("/saveItem"))
                    .andExpect(MockMvcResultMatchers.status().is(405));

    }*/

    @Test
    @WithMockUser
    public void retrieveStatusDelete() throws Exception{
        Item item = new Item();

        Mockito.when(itemRepository.findById((long)1)).thenReturn(Optional.of(item));
        mvc.perform(MockMvcRequestBuilders.get("/deleteItem").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"));
    }

    @Test(expected = NestedServletException.class)
    @WithMockUser
    public void retrieveStatusDeleteNotExistent() throws Exception{
        Item item = new Item();

        Mockito.when(itemRepository.findById((long)1)).thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders.get("/deleteItem").param("id","1"));
                //.andExpect(MockMvcResultMatchers.redirectedUrl("/account"));
    }

    private Item getItemIdOne() {
        Item item = new Item();
        item.setId((long)1);
        User user = new User();
        Address address = new Address();
        user.setAddress(address);
        item.setLender(user);
        return item;
    }

}
