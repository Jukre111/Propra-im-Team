package de.hhu.sharing.ServiceTests;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.Period;
import de.hhu.sharing.model.Request;
import de.hhu.sharing.model.User;
import de.hhu.sharing.propay.Account;
import de.hhu.sharing.propay.Reservation;
import de.hhu.sharing.services.ProPayService;
import de.hhu.sharing.services.TransactionService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;

public class TransactionServiceTest {
    /*@Mock
    ItemRepository itemRepo;

    @Mock
    RequestRepository reqRepo;

    @Mock
    ProPayService proService;

    @InjectMocks
    TransactionService transSevice;

    @Before
    public void initialize(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckFinancesTrue() {
        User borrower = new User();
        Request request = new Request();
        Item item = new Item();

        borrower.setUsername("User");
        item.setRental(10);
        item.setDeposit(100);

        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation(1, item.getDeposit()));

        request.setPeriod(new Period(LocalDate.now(),LocalDate.now().plusDays(5)));
        request.setRequester(borrower);
        Mockito.when(proService.getAccount(borrower.getUsername())).thenReturn(new Account("User", 200, reservations));

        Assertions.assertThat(transSevice.checkFinances(request.getRequester(), item, request.getPeriod().getStartdate(), request.getPeriod().getEnddate())).isTrue();
    }

    @Test
    public void testCheckFinances() {
        User borrower = new User();
        Request request = new Request();
        Item item = new Item();

        borrower.setUsername("User");
        item.setRental(10);
        item.setDeposit(100);

        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation(1, item.getDeposit()));

        request.setPeriod(new Period(LocalDate.now(),LocalDate.now().plusDays(5)));
        request.setRequester(borrower);
        Mockito.when(proService.getAccount(borrower.getUsername())).thenReturn(new Account("User", 100, reservations));

        Assertions.assertThat(transSevice.checkFinances(request.getRequester(), item, request.getPeriod().getStartdate(), request.getPeriod().getEnddate())).isFalse();
    }*/

}
