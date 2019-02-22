package de.hhu.sharing;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import de.hhu.sharing.security.SecurityConfig;
import de.hhu.sharing.services.*;
import de.hhu.sharing.web.RequestController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import java.security.Principal;
import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@RunWith(SpringRunner.class)
@WebMvcTest(RequestController.class)
public class RequestControllerTest{

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @MockBean
    ItemService itemService;

    @MockBean
    RequestService requestService;

    @MockBean
    TransactionService transService;

    @MockBean
    BorrowingProcessService processService;

    public User createUser(){
        LocalDate date = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User("user","password", "role", "lastnmae", "forname", "email",date,address);
        return user;
    }

    @WithMockUser
    @Test
    public void retrieveStatusRequest()throws Exception{
        User user = createUser();
        Item item = new Item("name", "description", 42,42, new User());

        Mockito.when(userService.get("user")).thenReturn(user);
        Mockito.when(itemService.get(1L)).thenReturn(item);
        mvc.perform(MockMvcRequestBuilders.get("/newRequest?id=1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @WithMockUser
    @Test
    public void retrieveStatusPostRequest()throws Exception{
        User user = createUser();
        Item item = new Item("name", "description", 42,42, user);
        Mockito.when(userService.get("user")).thenReturn(user);
        Mockito.when(itemService.get(1L)).thenReturn(item);
        Mockito.when(transService.checkFinances(user, item, LocalDate.parse("2000-01-01"), LocalDate.parse("2000-02-02"))).thenReturn(true);
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
//        User user = createUser();
//        Mockito.when(userService.get("user")).thenReturn(user);
//        Mockito.when(requestService.get(1L).getRequester()).thenReturn(user);
//        mvc.perform(MockMvcRequestBuilders.get("/deleteRequest?id=1"))
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"));
    }


    @WithMockUser
    @Test
    public void retrieveStatusAcceptRequest()throws Exception{
//        User user = createUser();
//        Mockito.when(userService.get("user")).thenReturn(user);
//        Mockito.when(itemService.getFromRequestId(1L).getLender()).thenReturn(user);
//        Mockito.when(transService.createTransaction(1L, 2L)).thenReturn(200);
//        mvc.perform(MockMvcRequestBuilders.get("/acceptRequest?requestId=1&itemId=2"))
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"));
    }


    @WithMockUser
    @Test
    public void retrieveStatusDeclineRequest()throws Exception{
//        User user = createUser();
//        Mockito.when(userService.get("user")).thenReturn(user);
//        Mockito.when(itemService.getFromRequestId(1L).getLender()).thenReturn(user);
//        mvc.perform(MockMvcRequestBuilders.get("/declineRequest?requestId=1&itemId=2"))
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"));
    }


}
