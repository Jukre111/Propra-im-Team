package de.hhu.sharing.web;

import de.hhu.sharing.model.*;
import de.hhu.sharing.services.ProPayService;
import de.hhu.sharing.services.SellableItemService;
import de.hhu.sharing.services.TransactionPurchaseService;
import de.hhu.sharing.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SellableItemController.class)
public class SellableItemControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    SellableItemService sellableItemService;

    @MockBean
    UserService userService;

    @MockBean
    ProPayService proService;

    @MockBean
    TransactionPurchaseService transactionPurchaseService;

    private User createUser(String username, String role) {
        return new User(username, "password", role,
                "Nachname", "Vorname", "email@web.de", LocalDate.now(),
                new Address("Strasse", "Stadt", 41460));
    }

    private SellableItem createSellableItem(User owner) {
        SellableItem sellableItem = new SellableItem("Item", "Beschreibung", 100, owner);
        sellableItem.setId(1L);
        return sellableItem;
    }

    @Test
    @WithMockUser
    public void retrieveStatusSellableItemDetails() throws Exception{
        User owner = createUser("User", "ROLE_USER");
        SellableItem sellableItem = createSellableItem(owner);
        Mockito.when(sellableItemService.get(1L)).thenReturn(sellableItem);

        mvc.perform(MockMvcRequestBuilders.get("/sellableItemDetails").param("id","1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void retrieveStatusDetailsNewSellableItem() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/newSellableItem"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void retrieveStatusEditSellableItem() throws Exception{
        User owner = createUser("User", "ROLE_USER");
        SellableItem sellableItem = createSellableItem(owner);
        Mockito.when(userService.get("user")).thenReturn(owner);
        Mockito.when(sellableItemService.isOwner(1L, owner)).thenReturn(true);
        Mockito.when(sellableItemService.get(1L)).thenReturn(sellableItem);

        mvc.perform(MockMvcRequestBuilders.get("/editSellableItem").param("id","1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void redirectEditSellableItemWhenNotAuthorized() throws Exception{
        User owner = createUser("User", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(owner);
        Mockito.when(sellableItemService.isOwner(1L, owner)).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.get("/editSellableItem").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errMessage"));
    }

    @Test
    @WithMockUser
    public void retrieveStatusSaveSellableItem() throws Exception{
        User owner = createUser("User", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(owner);
        MockMultipartFile jsonFile = new MockMultipartFile("test.gif", "", "image/gif", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("id", "1");
        map.add("name", "name");
        map.add("description", "desc");
        map.add("price", "42");

        mvc.perform(MockMvcRequestBuilders.multipart("/saveSellableItem").file("file",jsonFile.getBytes()).params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("succMessage"));
    }

    @Test
    @WithMockUser
    public void retrieveStatusSaveSellableItemWithIdNull() throws Exception{
        User owner = createUser("User", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(owner);
        MockMultipartFile jsonFile = new MockMultipartFile("test.gif", "", "image/gif", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("id", null);
        map.add("name", "name");
        map.add("description", "desc");
        map.add("price", "42");

        mvc.perform(MockMvcRequestBuilders.multipart("/saveSellableItem").file("file",jsonFile.getBytes()).params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("succMessage"));
    }

    @Test
    @WithMockUser
    public void redirectDeleteSellableItem() throws Exception{
        User owner = createUser("User", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(owner);
        Mockito.when(sellableItemService.isOwner(1L, owner)).thenReturn(true);


        mvc.perform(MockMvcRequestBuilders.get("/deleteSellableItem").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("succMessage"));
    }

    @Test
    @WithMockUser
    public void redirectDeleteSellableItemWhenNotAuthorized() throws Exception{
        User owner = createUser("User", "ROLE_USER");
        Mockito.when(userService.get("user")).thenReturn(owner);
        Mockito.when(sellableItemService.isOwner(1L, owner)).thenReturn(false);


        mvc.perform(MockMvcRequestBuilders.get("/deleteSellableItem").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errMessage"));
    }

    @Test
    @WithMockUser
    public void redirectBuy() throws Exception{
        User owner = createUser("User", "ROLE_USER");
        SellableItem sellableItem = createSellableItem(owner);
        Mockito.when(userService.get("user")).thenReturn(owner);
        Mockito.when(sellableItemService.get(1L)).thenReturn(sellableItem);
        Mockito.when(sellableItemService.isOwner(1L, owner)).thenReturn(false);
        Mockito.when(proService.enoughCredit(owner, sellableItem)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.get("/buy").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("succMessage"));
    }

    @Test
    @WithMockUser
    public void redirectBuyWhenOwnItem() throws Exception{
        User owner = createUser("User", "ROLE_USER");
        SellableItem sellableItem = createSellableItem(owner);
        Mockito.when(userService.get("user")).thenReturn(owner);
        Mockito.when(sellableItemService.get(1L)).thenReturn(sellableItem);
        Mockito.when(sellableItemService.isOwner(1L, owner)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.get("/buy").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errMessage"));
    }

    @Test
    @WithMockUser
    public void redirectBuyWhenNoCredit() throws Exception{
        User owner = createUser("User", "ROLE_USER");
        SellableItem sellableItem = createSellableItem(owner);
        Mockito.when(userService.get("user")).thenReturn(owner);
        Mockito.when(sellableItemService.get(1L)).thenReturn(sellableItem);
        Mockito.when(sellableItemService.isOwner(1L, owner)).thenReturn(false);
        Mockito.when(proService.enoughCredit(owner, sellableItem)).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.get("/buy").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errMessage"));
    }

    @Test
    @WithMockUser
    public void redirectWhenSellableItemNotFoundException() throws Exception {
        Mockito.when(sellableItemService.get(1L)).thenThrow(new NotFoundException("message"));

        mvc.perform(MockMvcRequestBuilders.get("/sellableItemDetails").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("errMessage", "message"));
    }
}