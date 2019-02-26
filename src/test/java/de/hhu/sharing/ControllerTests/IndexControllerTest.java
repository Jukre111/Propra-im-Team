package de.hhu.sharing.ControllerTests;

import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.LendableItemService;
import de.hhu.sharing.services.RequestService;
import de.hhu.sharing.services.SellableItemService;
import de.hhu.sharing.services.UserService;
import de.hhu.sharing.web.IndexController;
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

import java.time.LocalDate;
import java.util.ArrayList;


@RunWith(SpringRunner.class)
@WebMvcTest(IndexController.class)
public class IndexControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @MockBean
    LendableItemService lendableItemService;

    @MockBean
    RequestService requestService;

    @MockBean
    SellableItemService sellableItemService;

    private User createUser(String username, String role) {
        return new User(username, "password", role,
                "Nachname", "Vorname", "email@web.de", LocalDate.now(),
                new Address("Strasse", "Stadt", 41460));
    }

    @Test
    @WithMockUser
    public void retrieveStatusIndex() throws Exception{
        Mockito.when(lendableItemService.getAll()).thenReturn(new ArrayList<>());

        mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void retrieveStatusSearch() throws Exception{
        Mockito.when(lendableItemService.searchFor("test")).thenReturn(new ArrayList<>());

        mvc.perform(MockMvcRequestBuilders.get("/search").param("query", "test"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void retrieveStatusAccount() throws Exception{
        User user = createUser("User", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(user);
        Mockito.when(lendableItemService.getAllIPosted(user)).thenReturn(new ArrayList<>());

        mvc.perform(MockMvcRequestBuilders.get("/account"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void retrieveStatusMessages() throws Exception{
        User user = createUser("User", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(user);
        Mockito.when(lendableItemService.getAllIPosted(user)).thenReturn(new ArrayList<>());
        Mockito.when(lendableItemService.getAllIRequested(user)).thenReturn(new ArrayList<>());

        mvc.perform(MockMvcRequestBuilders.get("/messages"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
