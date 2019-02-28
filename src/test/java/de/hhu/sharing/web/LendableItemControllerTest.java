package de.hhu.sharing.web;

import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.*;
import de.hhu.sharing.services.BorrowingProcessService;
import de.hhu.sharing.services.LendableItemService;
import de.hhu.sharing.services.RequestService;
import de.hhu.sharing.services.UserService;
import de.hhu.sharing.storage.StorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;

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

    private User createUser(String username, String role) {
        return new User(username, "password", role,
                "Nachname", "Vorname", "email@web.de", LocalDate.now(),
                new Address("Strasse", "Stadt", 41460));
    }

    private LendableItem createLendableItem(User lender) {
        LendableItem lendableItem = new LendableItem("Item", "Beschreibung", 10, 100, lender);
        lendableItem.setId(1L);
        return lendableItem;
    }

    @Test
    @WithMockUser
    public void retrieveStatusLendableItemDetails() throws Exception{
        User lender = createUser("User", "ROLE_USER");
        LendableItem lendableItem = createLendableItem(lender);
        Mockito.when(lendableItemService.get(1L)).thenReturn(lendableItem);
        Mockito.when(userService.get("user")).thenReturn(lender);
        Mockito.when(lendableItemService.allDatesInbetween(lendableItem)).thenReturn(new ArrayList());

        mvc.perform(MockMvcRequestBuilders.get("/lendableItemDetails").param("id","1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void retrieveStatusDetailsNewLendableItem() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/newLendableItem"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void retrieveStatusEditLendableItem() throws Exception{
        User lender = createUser("User", "ROLE_USER");
        LendableItem lendableItem = createLendableItem(lender);
        Mockito.when(userService.get("user")).thenReturn(lender);
        Mockito.when(lendableItemService.isOwner(1L, lender)).thenReturn(true);
        Mockito.when(lendableItemService.isChangeable(1L)).thenReturn(true);
        Mockito.when(lendableItemService.get(1L)).thenReturn(lendableItem);

        mvc.perform(MockMvcRequestBuilders.get("/editLendableItem").param("id","1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void redirectEditLendableItemWhenNotAuthorized() throws Exception{
        User lender = createUser("User", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(lender);
        Mockito.when(lendableItemService.isOwner(1L, lender)).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.get("/editLendableItem").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errMessage"));
    }

    @Test
    @WithMockUser
    public void redirectEditLendableItemWhenNotChangeable() throws Exception{
        User lender = createUser("User", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(lender);
        Mockito.when(lendableItemService.isOwner(1L, lender)).thenReturn(true);
        Mockito.when(lendableItemService.isChangeable(1L)).thenReturn(false);


        mvc.perform(MockMvcRequestBuilders.get("/editLendableItem").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errMessage"));
    }

    @Test
    @WithMockUser
    public void retrieveStatusSaveLendableItem() throws Exception{
        User lender = createUser("User", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(lender);
        MockMultipartFile jsonFile = new MockMultipartFile("test.gif", "", "image/gif", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("id", "1");
        map.add("name","name");
        map.add("description","desc");
        map.add("rental","42");
        map.add("deposit","41");

        mvc.perform(MockMvcRequestBuilders.multipart("/saveLendableItem").file("file",jsonFile.getBytes()).params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("succMessage"));
    }

    @Test
    @WithMockUser
    public void retrieveStatusSaveLendableItemWithIdNull() throws Exception{
        User lender = createUser("User", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(lender);
        MockMultipartFile jsonFile = new MockMultipartFile("test.gif", "", "image/gif", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("id", null);
        map.add("name","name");
        map.add("description","desc");
        map.add("rental","42");
        map.add("deposit","41");

        mvc.perform(MockMvcRequestBuilders.multipart("/saveLendableItem").file("file",jsonFile.getBytes()).params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("succMessage"));
    }

    @Test
    @WithMockUser
    public void redirectDeleteLendableItem() throws Exception{
        User lender = createUser("User", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(lender);
        Mockito.when(lendableItemService.isOwner(1L, lender)).thenReturn(true);
        Mockito.when(lendableItemService.isChangeable(1L)).thenReturn(true);


        mvc.perform(MockMvcRequestBuilders.get("/deleteLendableItem").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("succMessage"));
    }

    @Test
    @WithMockUser
    public void redirectDeleteLendableItemWhenNotAuthorized() throws Exception{
        User lender = createUser("User", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(lender);
        Mockito.when(lendableItemService.isOwner(1L, lender)).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.get("/deleteLendableItem").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errMessage"));
    }

    @Test
    @WithMockUser
    public void redirectDeleteLendableItemNotChangeable() throws Exception{
        User lender = createUser("User", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(lender);
        Mockito.when(lendableItemService.isOwner(1L, lender)).thenReturn(true);
        Mockito.when(lendableItemService.isChangeable(1L)).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.get("/deleteLendableItem").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errMessage"));
    }

    @Test
    @WithMockUser
    public void redirectItemReturned() throws Exception{
        User lender = createUser("User", "ROLE_USER");
        LendableItem lendableItem = createLendableItem(lender);
        BorrowingProcess process = new BorrowingProcess();
        process.setItem(lendableItem);
        Mockito.when(userService.get("user")).thenReturn(lender);
        Mockito.when(borrowingProcessService.get(1L)).thenReturn(process);
        Mockito.when(lendableItemService.isOwner(1L, lender)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.get("/itemReturned").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("succMessage"));
    }

    @Test
    @WithMockUser
    public void redirectItemReturnedWhenNotAuthorized() throws Exception{
        User lender = createUser("User", "ROLE_USER");
        LendableItem lendableItem = createLendableItem(lender);
        BorrowingProcess process = new BorrowingProcess();
        process.setItem(lendableItem);
        Mockito.when(userService.get("user")).thenReturn(lender);
        Mockito.when(borrowingProcessService.get(1L)).thenReturn(process);
        Mockito.when(lendableItemService.isOwner(1L, lender)).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.get("/itemReturned").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errMessage"));
    }
}
