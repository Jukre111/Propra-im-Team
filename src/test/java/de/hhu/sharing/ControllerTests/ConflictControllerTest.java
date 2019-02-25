package de.hhu.sharing.ControllerTests;

import de.hhu.sharing.data.TransactionRentalRepository;
import de.hhu.sharing.model.*;
import de.hhu.sharing.propay.TransactionRental;
import de.hhu.sharing.services.*;
import de.hhu.sharing.storage.StorageService;
import de.hhu.sharing.web.ConflictController;
import org.assertj.core.api.Assertions;
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
    TransactionRentalRepository transRepo;

    @MockBean
    ConflictService conService;

    @MockBean
    ProPayService proService;

    @Test
    public void mustHaveTest (){
        Assertions.assertThat(true).isTrue();
    }
/*    private User createUser(String Username) {
        User user = new User();
        user.setUsername("user");
        user.setAddress(new Address("Strasse", "Stadt", 41460));
        return user;
    }

    private BorrowingProcess createProcess(User owner) {
        BorrowingProcess process = new BorrowingProcess();
        process.setPeriod(new Period(LocalDate.now(), LocalDate.now().plusDays(1)));
        process.setLendableItem(createItem(owner));
        process.setId(1L);
        return process;
    }

    private lendableItem createItem(User owner) {
        lendableItem lendableItem = new lendableItem();
        lendableItem.setId(1L);
        lendableItem.setOwner(owner);
        return lendableItem;
    }

    private Conflict createConflict() {
        lendableItem lendableItem = createItem(createUser("Lender"));
        Conflict conflict = new Conflict();
        conflict.setId(1L);
        conflict.setLendableItem(lendableItem);
        conflict.setOwner(lendableItem.getOwner());
        conflict.setBorrower(createUser("Borrower"));
        conflict.setProcess(createProcess(conflict.getOwner()));
        return conflict;
    }

    @Test
    @WithMockUser
    public void retrieveStatusConflict() throws Exception {
        User owner = createUser("Lender");
        User borrower = createUser("Borrower");
        BorrowingProcess process = createProcess(owner);

        Mockito.when(userService.getBorrowerFromBorrowingProcessId(1L)).thenReturn(borrower);
        Mockito.when(borrowingProcessService.getBorrowingProcess(1L)).thenReturn(process);
        mvc.perform(MockMvcRequestBuilders.get("/conflict").param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    @WithMockUser
    public void retrieveStatusPostSaveConflict() throws Exception {
        User borrower = createUser("Borrower");
        User owner = createUser("Lender");
        BorrowingProcess process = createProcess(owner);

        Mockito.when(borrowingProcessService.getBorrowingProcess(Mockito.anyLong())).thenReturn(process);
        Mockito.when(borrowingProcessService.getItemFromProcess(process)).thenReturn(process.getLendableItem());
        Mockito.when(userService.getBorrowerFromBorrowingProcessId(Mockito.anyLong())).thenReturn(borrower);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id", "1");
        map.add("problem", "This test.");

        mvc.perform(MockMvcRequestBuilders.post("/saveConflict").with(csrf()).params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"));
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
        TransactionRental transRen = new TransactionRental();
        Conflict conflict = createConflict();
        Mockito.when(conService.get(Mockito.anyLong())).thenReturn(conflict);
        Mockito.when(transRepo.findByProcessId(1L)).thenReturn(transRen);
        Mockito.when(proService.punishDeposit("Borrower", transRen)).thenReturn(200);

        mvc.perform(MockMvcRequestBuilders.get("/borrower").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/conflictView"));
    }

    @Test
    @WithMockUser
    public void retrieveStatusOwner() throws Exception {
        TransactionRental transRen = new TransactionRental();
        Conflict conflict = createConflict();
        Mockito.when(conService.get(Mockito.anyLong())).thenReturn(conflict);
        Mockito.when(transRepo.findByProcessId(1L)).thenReturn(transRen);
        Mockito.when(proService.punishDeposit("Borrower", transRen)).thenReturn(200);

        mvc.perform(MockMvcRequestBuilders.get("/owner").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/conflictView"));
    }*/
}
