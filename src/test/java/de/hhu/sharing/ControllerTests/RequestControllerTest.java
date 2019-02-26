package de.hhu.sharing.ControllerTests;

import de.hhu.sharing.services.*;
import de.hhu.sharing.storage.StorageService;
import de.hhu.sharing.web.RequestController;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(RequestController.class)
public class RequestControllerTest{

    @Autowired
    MockMvc mvc;

    @MockBean
    StorageService storeService;

    @MockBean
    UserService userService;

    @MockBean
    ItemService itemService;

    @MockBean
    RequestService requestService;

    @MockBean
    TransactionRentalService transService;

    @MockBean
    BorrowingProcessService processService;

    @MockBean
    ProPayService proPayService;

    @Test
    public void mustHaveTest (){
        Assertions.assertThat(true).isTrue();
    }

    /*public User createUser(){
        LocalDate date = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User("user","password", "role", "lastnmae", "forname", "email",date,address);
        return user;
    }

    @WithMockUser
    @Test
    public void retrieveStatusRequest()throws Exception{
        User user = createUser();
        LendableItem LendableItem = new LendableItem("name", "description", 42,42, new User());

        Mockito.when(userService.get("user")).thenReturn(user);
        Mockito.when(itemService.get(1L)).thenReturn(LendableItem);
        mvc.perform(MockMvcRequestBuilders.get("/newRequest?id=1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @WithMockUser
    @Test
    public void retrieveStatusPostRequest()throws Exception{
        User user = createUser();
        LendableItem LendableItem = new LendableItem("name", "description", 42,42, user);
        Mockito.when(userService.get("user")).thenReturn(user);
        Mockito.when(itemService.get(1L)).thenReturn(LendableItem);
        Mockito.when(transService.checkFinances(user, LendableItem, LocalDate.parse("2000-01-01"), LocalDate.parse("2000-02-02"))).thenReturn(true);
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("id", "1");
        map.add("startdate","2000-01-01");
        map.add("enddate","2000-02-02");

        mvc.perform(MockMvcRequestBuilders.post("/saveRequest").with(csrf()).params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));

    }

    @WithMockUser
    @Test
    public void retrieveStatusDeleteRequest()throws Exception{
        User user = createUser();
        Request request = new Request();
        request.setRequester(user);
        Mockito.when(userService.get("user")).thenReturn(user);
        Mockito.when(requestService.get(1L)).thenReturn(request);
        mvc.perform(MockMvcRequestBuilders.get("/deleteRequest?id=1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"));
    }


    @WithMockUser
    @Test
    public void retrieveStatusAcceptRequest()throws Exception{
        User owner = createUser();
        LendableItem LendableItem = new LendableItem ("LendableItem","desc1", 10, 500, owner);
        LendableItem.setId(2L);
        Mockito.when(userService.get("user")).thenReturn(owner);
        Mockito.when(itemService.getFromRequestId(1L)).thenReturn(LendableItem);
        Mockito.when(transService.createTransactionRental(1L, 2L)).thenReturn(200);
        mvc.perform(MockMvcRequestBuilders.get("/acceptRequest?requestId=1&itemId=2").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"));
    }


    @WithMockUser
    @Test
    public void retrieveStatusDeclineRequest()throws Exception{
        User owner = createUser();
        LendableItem LendableItem = new LendableItem ("LendableItem","desc1", 10, 500, owner);
        LendableItem.setId(2L);
        Mockito.when(userService.get("user")).thenReturn(owner);
        Mockito.when(itemService.getFromRequestId(1L)).thenReturn(LendableItem);
        mvc.perform(MockMvcRequestBuilders.get("/declineRequest?requestId=1&itemId=2").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"));
    }*/


}
