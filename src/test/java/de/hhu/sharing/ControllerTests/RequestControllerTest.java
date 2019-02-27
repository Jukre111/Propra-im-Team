package de.hhu.sharing.ControllerTests;

import de.hhu.sharing.model.*;
import de.hhu.sharing.services.*;
import de.hhu.sharing.web.RequestController;
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

@RunWith(SpringRunner.class)
@WebMvcTest(RequestController.class)
public class RequestControllerTest{

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @MockBean
    LendableItemService lendableItemService;

    @MockBean
    RequestService requestService;

    @MockBean
    ProPayService proPayService;

    @MockBean
    BorrowingProcessService processService;

    private User createUser(String username, String role) {
        return new User(username, "password", role,
                "Nachname", "Vorname", "email@web.de", LocalDate.now(),
                new Address("Strasse", "Stadt", 41460));
    }

    private LendableItem createLendableItem(User lender) {
        return new LendableItem("Item", "Beschreibung", 10, 100, lender);
    }

    private Request createRequest(User requester){
        return new Request(new Period(LocalDate.parse("2000-01-01"), LocalDate.parse("2000-02-02")), requester);
    }


    @Test
    @WithMockUser
    public void retrieveStatusNewRequest() throws Exception{
        User requester = createUser("Requester", "ROLE_USER");
        User owner = createUser("Owner", "ROLE_USER");
        LendableItem item = createLendableItem(owner);
        Mockito.when(userService.get("user")).thenReturn(requester);
        Mockito.when(lendableItemService.isOwner(1L, requester)).thenReturn(false);
        Mockito.when(lendableItemService.get(1L)).thenReturn(item);

        mvc.perform(MockMvcRequestBuilders.get("/newRequest").param("id","1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void redirectNewRequestWithOwnItem() throws Exception{
        User requester = createUser("Requester", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(requester);
        Mockito.when(lendableItemService.isOwner(1L, requester)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.get("/newRequest").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("ownItem", true));
    }


    @Test
    @WithMockUser
    public void redirectSaveRequest() throws Exception{
        User requester = createUser("Requester", "ROLE_USER");
        User owner = createUser("Owner", "ROLE_USER");
        LendableItem item = createLendableItem(owner);
        Mockito.when(userService.get("user")).thenReturn(requester);
        Mockito.when(lendableItemService.get(1L)).thenReturn(item);
        Mockito.when(lendableItemService.isOwner(1L, requester)).thenReturn(false);
        Mockito.when(proPayService.enoughCredit(requester,item, LocalDate.parse("2000-01-01"), LocalDate.parse("2000-02-02"))).thenReturn(true);
        Mockito.when(lendableItemService.isAvailableAt(item, LocalDate.parse("2000-01-01"), LocalDate.parse("2000-02-02"))).thenReturn(true);
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("id", "1");
        map.add("startdate","2000-01-01");
        map.add("enddate","2000-02-02");

        mvc.perform(MockMvcRequestBuilders.post("/saveRequest").params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("requested", true));

    }

    @Test
    @WithMockUser
    public void redirectSaveRequestWithOwnItem()throws Exception{
        User requester = createUser("Requester", "ROLE_USER");
        User owner = createUser("Owner", "ROLE_USER");
        LendableItem item = createLendableItem(owner);
        Mockito.when(userService.get("user")).thenReturn(requester);
        Mockito.when(lendableItemService.get(1L)).thenReturn(item);
        Mockito.when(lendableItemService.isOwner(1L, requester)).thenReturn(true);
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("id", "1");
        map.add("startdate","2000-01-01");
        map.add("enddate","2000-02-02");

        mvc.perform(MockMvcRequestBuilders.post("/saveRequest").params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("ownItem", true));
    }

    @Test
    @WithMockUser
    public void redirectSaveRequestWithNoCredit()throws Exception{
        User requester = createUser("Requester", "ROLE_USER");
        User owner = createUser("Owner", "ROLE_USER");
        LendableItem item = createLendableItem(owner);
        Mockito.when(userService.get("user")).thenReturn(requester);
        Mockito.when(lendableItemService.get(1L)).thenReturn(item);
        Mockito.when(lendableItemService.isOwner(1L, requester)).thenReturn(false);
        Mockito.when(proPayService.enoughCredit(requester,item, LocalDate.parse("2000-01-01"), LocalDate.parse("2000-02-02"))).thenReturn(false);
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("id", "1");
        map.add("startdate","2000-01-01");
        map.add("enddate","2000-02-02");

        mvc.perform(MockMvcRequestBuilders.post("/saveRequest").params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("noCredit", true));
    }

    @Test
    @WithMockUser
    public void redirectSaveRequestWithNotAvailable()throws Exception{
        User requester = createUser("Requester", "ROLE_USER");
        User owner = createUser("Owner", "ROLE_USER");
        LendableItem item = createLendableItem(owner);
        Mockito.when(userService.get("user")).thenReturn(requester);
        Mockito.when(lendableItemService.get(1L)).thenReturn(item);
        Mockito.when(lendableItemService.isOwner(1L, requester)).thenReturn(false);
        Mockito.when(proPayService.enoughCredit(requester,item, LocalDate.parse("2000-01-01"), LocalDate.parse("2000-02-02"))).thenReturn(true);
        Mockito.when(lendableItemService.isAvailableAt(item, LocalDate.parse("2000-01-01"), LocalDate.parse("2000-02-02"))).thenReturn(false);
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("id", "1");
        map.add("startdate","2000-01-01");
        map.add("enddate","2000-02-02");

        mvc.perform(MockMvcRequestBuilders.post("/saveRequest").params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/newRequest?id=1"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("notAvailable", true));
    }

    @Test
    @WithMockUser
    public void redirectDeleteRequest() throws Exception{
        User requester = createUser("Requester", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(requester);
        Mockito.when(requestService.isRequester(1L, requester)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.get("/deleteRequest").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("deleted", true));
    }

    @Test
    @WithMockUser
    public void redirectDeleteRequestNotAuthorized() throws Exception{
        User requester = createUser("Requester", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(requester);
        Mockito.when(requestService.isRequester(1L, requester)).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.get("/deleteRequest").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("notAuthorized", true));
    }

    @Test
    @WithMockUser
    public void redirectAcceptRequest() throws Exception{
        User requester = createUser("Requester", "ROLE_USER");
        User owner = createUser("Owner", "ROLE_USER");
        LendableItem item = createLendableItem(owner);
        Request request = createRequest(requester);
        Mockito.when(userService.get("user")).thenReturn(owner);
        Mockito.when(lendableItemService.getFromRequestId(1L)).thenReturn(item);
        Mockito.when(requestService.get(1L)).thenReturn(request);
        Mockito.when(requestService.isLender(1L, owner)).thenReturn(true);
        Mockito.when(requestService.isOutdated(1L)).thenReturn(false);
        Mockito.when(requestService.isOverlappingWithAvailability(1L)).thenReturn(false);
        Mockito.when(proPayService.enoughCredit(requester, item, LocalDate.parse("2000-01-01"), LocalDate.parse("2000-02-02"))).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.get("/acceptRequest").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("accepted", true));
    }

    @Test
    @WithMockUser
    public void redirectAcceptRequestWhenNotAuthorized() throws Exception{
        User requester = createUser("Requester", "ROLE_USER");
        User owner = createUser("Owner", "ROLE_USER");
        LendableItem item = createLendableItem(owner);
        Request request = createRequest(requester);
        Mockito.when(userService.get("user")).thenReturn(owner);
        Mockito.when(lendableItemService.getFromRequestId(1L)).thenReturn(item);
        Mockito.when(requestService.get(1L)).thenReturn(request);
        Mockito.when(requestService.isLender(1L, owner)).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.get("/acceptRequest").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("notAuthorized", true));
    }

    @Test
    @WithMockUser
    public void redirectAcceptRequestWhenOutdated() throws Exception{
        User requester = createUser("Requester", "ROLE_USER");
        User owner = createUser("Owner", "ROLE_USER");
        LendableItem item = createLendableItem(owner);
        Request request = createRequest(requester);
        Mockito.when(userService.get("user")).thenReturn(owner);
        Mockito.when(lendableItemService.getFromRequestId(1L)).thenReturn(item);
        Mockito.when(requestService.get(1L)).thenReturn(request);
        Mockito.when(requestService.isLender(1L, owner)).thenReturn(true);
        Mockito.when(requestService.isOutdated(1L)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.get("/acceptRequest").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("outdatedRequest", true));
    }

    @Test
    @WithMockUser
    public void redirectAcceptRequestWhenOverlapping() throws Exception{
        User requester = createUser("Requester", "ROLE_USER");
        User owner = createUser("Owner", "ROLE_USER");
        LendableItem item = createLendableItem(owner);
        Request request = createRequest(requester);
        Mockito.when(userService.get("user")).thenReturn(owner);
        Mockito.when(lendableItemService.getFromRequestId(1L)).thenReturn(item);
        Mockito.when(requestService.get(1L)).thenReturn(request);
        Mockito.when(requestService.isLender(1L, owner)).thenReturn(true);
        Mockito.when(requestService.isOutdated(1L)).thenReturn(false);
        Mockito.when(requestService.isOverlappingWithAvailability(1L)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.get("/acceptRequest").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("overlappingRequest", true));
    }

    @Test
    @WithMockUser
    public void redirectAcceptRequestNoCredit() throws Exception{
        User requester = createUser("Requester", "ROLE_USER");
        User owner = createUser("Owner", "ROLE_USER");
        LendableItem item = createLendableItem(owner);
        Request request = createRequest(requester);
        Mockito.when(userService.get("user")).thenReturn(owner);
        Mockito.when(lendableItemService.getFromRequestId(1L)).thenReturn(item);
        Mockito.when(requestService.get(1L)).thenReturn(request);
        Mockito.when(requestService.isLender(1L, owner)).thenReturn(true);
        Mockito.when(requestService.isOutdated(1L)).thenReturn(false);
        Mockito.when(requestService.isOverlappingWithAvailability(1L)).thenReturn(false);
        Mockito.when(proPayService.enoughCredit(requester, item, LocalDate.parse("2000-01-01"), LocalDate.parse("2000-02-02"))).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.get("/acceptRequest").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("noCredit", true));
    }

    @Test
    @WithMockUser
    public void redirectDeclineRequest() throws Exception{
        User requester = createUser("Requester", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(requester);
        Mockito.when(requestService.isLender(1L, requester)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.get("/declineRequest").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("declined", true));
    }

    @Test
    @WithMockUser
    public void redirectDeclineRequestNotAuthorized() throws Exception{
        User requester = createUser("Requester", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(requester);
        Mockito.when(requestService.isLender(1L, requester)).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.get("/declineRequest").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("notAuthorized", true));
    }
}
