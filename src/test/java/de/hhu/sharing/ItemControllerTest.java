package de.hhu.sharing;

import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.BorrowingProcessService;
import de.hhu.sharing.services.ItemService;
import de.hhu.sharing.services.RequestService;
import de.hhu.sharing.services.UserService;
import de.hhu.sharing.web.ItemController;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ItemService itemService;

    @MockBean
    UserService userService;

    @MockBean
    RequestService requestService;

    @MockBean
    BorrowingProcessService borrowingProcessService;

    @MockBean
    UserRepository userRepo;

    public Item itemCreator(){
        LocalDate date = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User("user","password", "role", "lastnmae", "forname", "email",date,address);
        Item item = new Item("apfel", "lecker",1,1 ,user );
        return item;
    }

    @Test
    @WithMockUser
    public void retrieveStatusDetails() throws Exception{
        Item item = itemCreator();
        Mockito.when(itemService.get(1L)).thenReturn(item);
        mvc.perform(MockMvcRequestBuilders.get("/detailsItem").param("id","1"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    @WithMockUser
    public void retrieveStatusDetailsNewItem() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/newItem"));
    }


    @Test
    @WithMockUser
    public void retrieveStatusEditItem() throws Exception{
        Item item = itemCreator();
        Mockito.when(itemService.isChangeable(1L)).thenReturn(true);
        Mockito.when(userService.get("user")).thenReturn(item.getLender());
        Mockito.when(itemService.get(1L)).thenReturn(item);
        mvc.perform(MockMvcRequestBuilders.get("/editItem").param("id","1"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }


    @Test
    @WithMockUser
    public void retrieveStatusSaveItem() throws Exception{
        LocalDate date = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User("user","password", "role", "lastnmae", "forname", "email",date,address);
        Mockito.when(userService.get("user")).thenReturn(user);
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("name","name");
        map.add("description","desc");
        map.add("rental","42");
        map.add("deposit","41");

        mvc.perform(MockMvcRequestBuilders.post("/saveItem").with(csrf()).params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"));
    }

    @Test
    @WithMockUser
    public void retrieveStatusDeleteItem() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/deleteItem").param("id","1"));
    }

}
