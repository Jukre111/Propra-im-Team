package de.hhu.sharing.ControllerTests;

import de.hhu.sharing.data.TransactionRepository;
import de.hhu.sharing.model.*;
import de.hhu.sharing.propay.Transaction;
import de.hhu.sharing.services.*;
import de.hhu.sharing.storage.StorageService;
import de.hhu.sharing.web.ConflictController;
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
import java.util.ArrayList;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@RunWith(SpringRunner.class)
@WebMvcTest(ConflictController.class)
public class ConflictControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    StorageService storeService;

    @MockBean
    ItemService itemService;

    @MockBean
    UserService userService;

    @MockBean
    BorrowingProcessService borrowingProcessService;

    @MockBean
    TransactionRepository transRepo;

    @MockBean
    ConflictService conService;

    @MockBean
    ProPayService proService;

    private User createUser(String Username) {
        User user = new User();
        user.setUsername("user");
        user.setAddress(new Address("Strasse", "Stadt", 41460));
        return user;
    }

    private BorrowingProcess createProcess(User lender) {
        BorrowingProcess process = new BorrowingProcess();
        process.setPeriod(new Period(LocalDate.now(), LocalDate.now().plusDays(1)));
        process.setItem(createItem(lender));
        process.setId(1L);
        return process;
    }

    private Item createItem(User lender) {
        Item item = new Item();
        item.setId(1L);
        item.setLender(lender);
        return item;
    }

    private Conflict createConflict() {
        Item item = createItem(createUser("Lender"));
        Conflict conflict = new Conflict();
        conflict.setId(1L);
      /*  conflict.setItem(item);
        conflict.setLender(item.getLender());
        conflict.setBorrower(createUser("Borrower"));
        conflict.setProcess(createProcess(conflict.getLender()));*/
        return conflict;
    }

    @Test
    @WithMockUser
    public void retrieveStatusConflict() throws Exception {
        User lender = createUser("Lender");
        User borrower = createUser("Borrower");
        BorrowingProcess process = createProcess(lender);

        Mockito.when(userService.getBorrowerFromBorrowingProcessId(1L)).thenReturn(borrower);
       /* Mockito.when(borrowingProcessService.getBorrowingProcess(1L)).thenReturn(process);
        mvc.perform(MockMvcRequestBuilders.get("/conflict").param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().is(200));*/
    }

    @Test
    @WithMockUser
    public void retrieveStatusPostSaveConflict() throws Exception {
        User borrower = createUser("Borrower");
        User lender = createUser("Lender");
        BorrowingProcess process = createProcess(lender);

      /*  Mockito.when(borrowingProcessService.getBorrowingProcess(Mockito.anyLong())).thenReturn(process);
        Mockito.when(borrowingProcessService.getItemFromProcess(process)).thenReturn(process.getItem());
        Mockito.when(userService.getBorrowerFromBorrowingProcessId(Mockito.anyLong())).thenReturn(borrower);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id", "1");
        map.add("problem", "This test.");

        mvc.perform(MockMvcRequestBuilders.post("/saveConflict").with(csrf()).params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"));*/
    }

    @Test
    @WithMockUser
    public void retrieveStatusConflictView() throws Exception {
        Mockito.when(conService.getAll()).thenReturn(new ArrayList<>());
        mvc.perform(MockMvcRequestBuilders.get("/conflictView"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    @WithMockUser
    public void retrieveStatusConflictDetails() throws Exception {
        Conflict conflict = createConflict();
        Mockito.when(conService.get(Mockito.anyLong())).thenReturn(conflict);
        mvc.perform(MockMvcRequestBuilders.get("/conflictDetails").param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    @WithMockUser
    public void retrieveStatusBorrower() throws Exception {
        Transaction trans = new Transaction();
        Conflict conflict = createConflict();
        Mockito.when(conService.get(Mockito.anyLong())).thenReturn(conflict);
     /*   Mockito.when(transRepo.findByProcessId(1L)).thenReturn(trans);
        Mockito.when(proService.punishDeposit("Borrower", trans)).thenReturn(200);

        mvc.perform(MockMvcRequestBuilders.get("/borrower").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/conflictView"));*/
    }

    @Test
    @WithMockUser
    public void retrieveStatusOwner() throws Exception {
        Transaction trans = new Transaction();
        Conflict conflict = createConflict();
        Mockito.when(conService.get(Mockito.anyLong())).thenReturn(conflict);
     /*   Mockito.when(transRepo.findByProcessId(1L)).thenReturn(trans);
        Mockito.when(proService.punishDeposit("Borrower", trans)).thenReturn(200);

        mvc.perform(MockMvcRequestBuilders.get("/lender").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/conflictView"));*/
    }
}
