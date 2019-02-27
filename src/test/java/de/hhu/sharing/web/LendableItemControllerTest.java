package de.hhu.sharing.web;

import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.*;
import de.hhu.sharing.services.BorrowingProcessService;
import de.hhu.sharing.services.LendableItemService;
import de.hhu.sharing.services.RequestService;
import de.hhu.sharing.services.UserService;
import de.hhu.sharing.storage.StorageService;
import de.hhu.sharing.web.LendableItemController;
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
@WebMvcTest(LendableItemController.class)
public class LendableItemControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    LendableItemService lendableItemService;

    @MockBean
    UserService userService;

    @MockBean
    RequestService requestService;

    @MockBean
    BorrowingProcessService borrowingProcessService;

    @MockBean
    UserRepository userRepo;

    @MockBean
    StorageService storeService;

    public LendableItem itemCreator(){
        LocalDate date = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User("user","password", "role", "lastnmae", "forname", "email",date,address);
        LendableItem lendableItem = new LendableItem("apfel", "lecker",1,1 ,user );
        return lendableItem;
    }

    /*@Test
    @WithMockUser
    public void retrieveStatusDetails() throws Exception{
        LendableItem lendableItem = itemCreator();
        Mockito.when(lendableItemService.get(1L)).thenReturn(lendableItem);
        mvc.perform(MockMvcRequestBuilders.get("/lendableItemDetails").param("id","1"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }*/

    @Test
    @WithMockUser
    public void retrieveStatusDetailsNewItem() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/newLendableItem"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }


    @Test
    @WithMockUser
    public void retrieveStatusEditItem() throws Exception{
        LendableItem lendableItem = itemCreator();
        Mockito.when(lendableItemService.isChangeable(1L)).thenReturn(true);
        Mockito.when(userService.get("user")).thenReturn(lendableItem.getOwner());
        Mockito.when(lendableItemService.get(1L)).thenReturn(lendableItem);
        mvc.perform(MockMvcRequestBuilders.get("/editLendableItem").param("id","1"))
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

        mvc.perform(MockMvcRequestBuilders.post("/saveLendableItem").with(csrf()).params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"));
    }

    @Test
    @WithMockUser
    public void retrieveStatusDeleteItem() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/deleteLendableItem").param("id","1"));
    }

    /*@Test
    @WithMockUser
    public void retrieveStatusReturnItem() throws Exception{
        LendableItem LendableItem = itemCreator();
        Mockito.when(userService.get("user")).thenReturn(LendableItem.getOwner());
        BorrowingProcess process = new BorrowingProcess(LendableItem, new Period(LocalDate.now(), LocalDate.now().plusDays(2)));
        process.setId(1L);
        Mockito.when(borrowingProcessService.get(1L)).thenReturn(process);
        mvc.perform(MockMvcRequestBuilders.get("/returnItem").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"));
    }*/

}
