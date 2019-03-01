package de.hhu.sharing.web;

import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.LendableItem;
import de.hhu.sharing.model.NotFoundException;
import de.hhu.sharing.model.User;
import de.hhu.sharing.propay.Account;
import de.hhu.sharing.services.*;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ProPayController.class)
public class ProPayControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @MockBean
    ProPayService proPayService;

    @MockBean
    TransactionRentalService transactionRentalService;

    @MockBean
    TransactionPurchaseService transactionPurchaseService;

    @Test
    @WithMockUser
    public void retrieveProPayAccount() throws Exception{
        User user = new User("User", "password", "ROLE_USER",
                "Nachname", "Vorname", "email@web.de", LocalDate.now(),
                new Address("Strasse", "Stadt", 41460));
        Account account = new Account("User", 100, new ArrayList<>());
        Mockito.when(userService.get("user")).thenReturn(user);
        Mockito.when(proPayService.getAccount(user)).thenReturn(account);
        Mockito.when(proPayService.getDepositSum(account)).thenReturn(42);
        Mockito.when(transactionRentalService.getAllFromSender(user)).thenReturn(new ArrayList<>());
        Mockito.when(transactionRentalService.getAllFromReceiver(user)).thenReturn(new ArrayList<>());
        Mockito.when(transactionPurchaseService.getAllFromSender(user)).thenReturn(new ArrayList<>());
        Mockito.when(transactionPurchaseService.getAllFromReceiver(user)).thenReturn(new ArrayList<>());

        mvc.perform(MockMvcRequestBuilders.get("/propayAccount"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void redirectSavePayIn() throws Exception{
        User user = new User("User", "password", "ROLE_USER",
                "Nachname", "Vorname", "email@web.de", LocalDate.now(),
                new Address("Strasse", "Stadt", 41460));
        Mockito.when(userService.get("user")).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.post("/savePayIn").param("sum", "100"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/propayAccount"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("succMessage"));
    }

    @Test
    @WithMockUser
    public void redirectWhenProPayNotFoundException() throws Exception {
        User user = new User("User", "password", "ROLE_USER",
                "Nachname", "Vorname", "email@web.de", LocalDate.now(),
                new Address("Strasse", "Stadt", 41460));
        Mockito.when(userService.get("user")).thenReturn(user);
        Mockito.when(proPayService.getAccount(user)).thenThrow(new NotFoundException("message"));

        mvc.perform(MockMvcRequestBuilders.get("/propayAccount"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("errMessage", "message"));
    }
}