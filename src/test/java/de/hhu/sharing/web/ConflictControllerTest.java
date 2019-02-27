package de.hhu.sharing.ControllerTests;

import de.hhu.sharing.model.*;
import de.hhu.sharing.services.*;
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

@RunWith(SpringRunner.class)
@WebMvcTest(ConflictController.class)
public class ConflictControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @MockBean
    ConflictService conflictService;

    @MockBean
    BorrowingProcessService borrowingProcessService;

    private User createUser(String username, String role) {
        return new User(username, "password", role,
                "Nachname", "Vorname", "email@web.de", LocalDate.now(),
                new Address("Strasse", "Stadt", 41460));
    }

    private LendableItem createLendableItem(User lender) {
        return new LendableItem("Item", "Beschreibung", 10, 100, lender);
    }

    private BorrowingProcess createProcess(User borrower, User lender) {
        BorrowingProcess process = new BorrowingProcess(createLendableItem(lender),
                new Period(LocalDate.now(), LocalDate.now().plusDays(1)));
        borrower.addToBorrowed(process);
        lender.addToLend(process);
        return process;
    }

    private Conflict createConflict(User lender, User borrower, BorrowingProcess process){
        Conflict conflict = new Conflict(lender, borrower, process, new Message(lender.getUsername(), "Problem"));
        conflict.setId(1L);
        return conflict;
    }

    @Test
    @WithMockUser
    public void retrieveStatusConflict() throws Exception {
        User lender = createUser("Lender", "ROLE_USER");
        User borrower = createUser("Borrower", "ROLE_USER");
        BorrowingProcess process = createProcess(borrower, lender);
        Mockito.when(userService.get("user")).thenReturn(borrower);
        Mockito.when(borrowingProcessService.get(1L)).thenReturn(process);
        Mockito.when(userService.userIsInvolvedToProcess(borrower, process)).thenReturn(true);
        Mockito.when(conflictService.getFromBorrowingProcess(process)).thenReturn(null);
        Mockito.when(userService.getBorrowerFromBorrowingProcessId(1L)).thenReturn(borrower);

        mvc.perform(MockMvcRequestBuilders.get("/conflict").param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void redirectConflictWhenNotAuthorized() throws Exception {
        User lender = createUser("Lender", "ROLE_USER");
        User borrower = createUser("Borrower", "ROLE_USER");
        BorrowingProcess process = createProcess(borrower, lender);
        Mockito.when(userService.get("user")).thenReturn(borrower);
        Mockito.when(borrowingProcessService.get(1L)).thenReturn(process);
        Mockito.when(userService.userIsInvolvedToProcess(borrower, process)).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.get("/conflict").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("notAuthorized", true));
    }

    @Test
    @WithMockUser
    public void redirectConflictWhenConflictExists() throws Exception {
        User lender = createUser("Lender", "ROLE_USER");
        User borrower = createUser("Borrower", "ROLE_USER");
        BorrowingProcess process = createProcess(borrower, lender);
        Conflict conflict = createConflict(lender, borrower, process);
        Mockito.when(userService.get("user")).thenReturn(borrower);
        Mockito.when(borrowingProcessService.get(1L)).thenReturn(process);
        Mockito.when(userService.userIsInvolvedToProcess(borrower, process)).thenReturn(true);
        Mockito.when(conflictService.getFromBorrowingProcess(process)).thenReturn(conflict);

        mvc.perform(MockMvcRequestBuilders.get("/conflict").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/conflictDetails?id=1"))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    @WithMockUser
    public void redirectSaveConflict() throws Exception {
        User lender = createUser("Lender", "ROLE_USER");
        User borrower = createUser("Borrower", "ROLE_USER");
        BorrowingProcess process = createProcess(borrower, lender);
        Mockito.when(userService.get("user")).thenReturn(borrower);
        Mockito.when(borrowingProcessService.get(1L)).thenReturn(process);
        Mockito.when(userService.userIsInvolvedToProcess(borrower, process)).thenReturn(true);
        Mockito.when(conflictService.getFromBorrowingProcess(process)).thenReturn(null);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id", "1");
        map.add("problem", "This test.");

        mvc.perform(MockMvcRequestBuilders.post("/saveConflict").params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    @WithMockUser
    public void redirectSaveConflictWhenNotAuthorized() throws Exception {
        User lender = createUser("Lender", "ROLE_USER");
        User borrower = createUser("Borrower", "ROLE_USER");
        BorrowingProcess process = createProcess(borrower, lender);
        Mockito.when(userService.get("user")).thenReturn(borrower);
        Mockito.when(borrowingProcessService.get(1L)).thenReturn(process);
        Mockito.when(userService.userIsInvolvedToProcess(borrower, process)).thenReturn(false);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id", "1");
        map.add("problem", "This test.");

        mvc.perform(MockMvcRequestBuilders.post("/saveConflict").params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("notAuthorized", true));
    }

    @Test
    @WithMockUser
    public void redirectSaveConflictWhenConflictExists() throws Exception {
        User lender = createUser("Lender", "ROLE_USER");
        User borrower = createUser("Borrower", "ROLE_USER");
        BorrowingProcess process = createProcess(borrower, lender);
        Conflict conflict = createConflict(lender, borrower, process);
        Mockito.when(userService.get("user")).thenReturn(borrower);
        Mockito.when(borrowingProcessService.get(1L)).thenReturn(process);
        Mockito.when(userService.userIsInvolvedToProcess(borrower, process)).thenReturn(true);
        Mockito.when(conflictService.getFromBorrowingProcess(process)).thenReturn(conflict);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id", "1");
        map.add("problem", "This test.");

        mvc.perform(MockMvcRequestBuilders.post("/saveConflict").params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/conflictDetails?id=1"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("conflictExistsAlready", true));
    }

    @Test
    @WithMockUser
    public void redirectAddMessageToConflict() throws Exception {
        User lender = createUser("Lender", "ROLE_USER");
        User borrower = createUser("Borrower", "ROLE_USER");
        BorrowingProcess process = createProcess(borrower, lender);
        Conflict conflict = createConflict(lender, borrower, process);
        Mockito.when(userService.get("user")).thenReturn(borrower);
        Mockito.when(conflictService.get(1L)).thenReturn(conflict);
        Mockito.when(userService.userIsInvolvedToProcess(borrower, process)).thenReturn(true);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id", "1");
        map.add("message", "New message.");

        mvc.perform(MockMvcRequestBuilders.post("/conflictNewMessage").params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/conflictDetails?id=1"))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    @WithMockUser
    public void redirectAddMessageToConflictWhenNotAuthorized() throws Exception {
        User lender = createUser("Lender", "ROLE_USER");
        User borrower = createUser("Borrower", "ROLE_USER");
        BorrowingProcess process = createProcess(borrower, lender);
        Conflict conflict = createConflict(lender, borrower, process);
        Mockito.when(userService.get("user")).thenReturn(borrower);
        Mockito.when(conflictService.get(1L)).thenReturn(conflict);
        Mockito.when(userService.userIsInvolvedToProcess(borrower, process)).thenReturn(false);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id", "1");
        map.add("message", "New message.");

        mvc.perform(MockMvcRequestBuilders.post("/conflictNewMessage").params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("notAuthorized", true));
    }

    @Test
    @WithMockUser
    public void retrieveStatusConflictDetails() throws Exception {
        User lender = createUser("Lender", "ROLE_USER");
        User borrower = createUser("Borrower", "ROLE_USER");
        BorrowingProcess process = createProcess(borrower, lender);
        Conflict conflict = createConflict(lender, borrower, process);
        Mockito.when(userService.get("user")).thenReturn(borrower);
        Mockito.when(conflictService.get(1L)).thenReturn(conflict);
        Mockito.when(userService.userIsInvolvedToProcess(borrower, process)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.get("/conflictDetails").param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void retrieveStatusConflictDetailsWhenAdmin() throws Exception {
        User lender = createUser("Lender", "ROLE_USER");
        User borrower = createUser("Borrower", "ROLE_USER");
        BorrowingProcess process = createProcess(borrower, lender);
        Conflict conflict = createConflict(lender, borrower, process);
        User admin = createUser("Admin", "ROLE_ADMIN");
        Mockito.when(userService.get("user")).thenReturn(admin);
        Mockito.when(conflictService.get(1L)).thenReturn(conflict);

        mvc.perform(MockMvcRequestBuilders.get("/conflictDetails").param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void redirectConflictDetailsWhenNotAuthorized() throws Exception {
        User lender = createUser("Lender", "ROLE_USER");
        User borrower = createUser("Borrower", "ROLE_USER");
        BorrowingProcess process = createProcess(borrower, lender);
        Conflict conflict = createConflict(lender, borrower, process);
        Mockito.when(userService.get("user")).thenReturn(borrower);
        Mockito.when(conflictService.get(1L)).thenReturn(conflict);
        Mockito.when(userService.userIsInvolvedToProcess(borrower, process)).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.get("/conflictDetails").param("id", "1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("notAuthorized", true));
    }

    @Test
    @WithMockUser
    public void forbiddenForNotAdminConflictView() throws Exception {
        Mockito.when(conflictService.getAll()).thenReturn(new ArrayList<>());
        mvc.perform(MockMvcRequestBuilders.get("/admin/conflicts"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void retrieveStatusForAdminConflictView() throws Exception {
        Mockito.when(conflictService.getAll()).thenReturn(new ArrayList<>());
        mvc.perform(MockMvcRequestBuilders.get("/admin/conflicts"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void forbiddenForNotAdminLender() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/admin/conflicts/lender").param("id","1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void redirectForAdminLender() throws Exception {
        User lender = createUser("Lender", "ROLE_USER");
        User borrower = createUser("Borrower", "ROLE_USER");
        BorrowingProcess process = createProcess(borrower, lender);
        Conflict conflict = createConflict(lender, borrower, process);
        Mockito.when(userService.get("user")).thenReturn(borrower);
        Mockito.when(conflictService.get(1L)).thenReturn(conflict);
        mvc.perform(MockMvcRequestBuilders.get("/admin/conflicts/lender").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/conflicts"))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    @WithMockUser
    public void forbiddenForNotAdminBorrower() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/admin/conflicts/borrower").param("id","1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void redirectForAdminBorrower() throws Exception {
        User lender = createUser("Lender", "ROLE_USER");
        User borrower = createUser("Borrower", "ROLE_USER");
        BorrowingProcess process = createProcess(borrower, lender);
        Conflict conflict = createConflict(lender, borrower, process);
        Mockito.when(userService.get("user")).thenReturn(borrower);
        Mockito.when(conflictService.get(1L)).thenReturn(conflict);
        mvc.perform(MockMvcRequestBuilders.get("/admin/conflicts/borrower").param("id","1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/conflicts"))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }
}
