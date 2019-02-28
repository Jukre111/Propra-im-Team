package de.hhu.sharing.services;

import de.hhu.sharing.data.TransactionRentalRepository;
import de.hhu.sharing.model.*;
import de.hhu.sharing.propay.Account;
import de.hhu.sharing.propay.Reservation;
import de.hhu.sharing.propay.TransactionPurchase;
import de.hhu.sharing.propay.TransactionRental;
import org.apache.tomcat.jni.Local;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import de.hhu.sharing.services.ProPayService;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.internal.verification.VerificationModeFactory.times;

public class ProPayServiceTest {


    @Mock
    TransactionRentalService transRenService;
    @Mock
    RestTemplate rt;
    @Spy
    @InjectMocks
    ProPayService pps = new ProPayService();

    @Before
    public void initialize(){
        MockitoAnnotations.initMocks(this);
    }

    private void prepareRestTemplate(String URL, String json) {
        Mockito.doNothing().when(pps).callURL(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
        Mockito.when(rt.getForObject(URL, String.class)).thenReturn(json);
    }

    private User createUser(String username) {
        User user = new User(username,"pswd", "ROLE_USER", "last", "first", "mail",
                LocalDate.now(),new Address("street", "city", 4000));
        return user;
    }

    @Test
    public void testGetAccount() {
        User userSrc = new User("Source", "pwd", "USER_ROLE", "Brand", "Feuer",
                            "FeuerBrand@112.de", LocalDate.now(), new Address("strasse", "stadt", 4000));

        String URL = "http://localhost:8888/" + "account/" + userSrc.getUsername() + "/";
        String json = "{\"account\":\"Source\"," +
                        "\"amount\":50.0," +
                        "\"reservations\":[{" +
                            "\"id\":1," +
                            "\"amount\":30.0" +
                        "},{" +
                            "\"id\":2," +
                            "\"amount\":15.0}" +
                        "]}";

        prepareRestTemplate(URL, json);
        Account account = pps.getAccount(userSrc);

        Assertions.assertThat(account).isNotEqualTo(null);
        Assertions.assertThat(account.getAmount()).isEqualTo(50);
        Assertions.assertThat(account.getAccount()).isEqualTo("Source");
    }

    @Test
    public void testInitiateTransactionRental() {
        String userSrc = "Source";
        String URL = "http://localhost:8888/account/" + userSrc + "/";
        User source = this.createUser("Source");
        User target = this.createUser("Target");
        TransactionRental transRen = new TransactionRental(50,15,0L,new LendableItem("itemName", "desc", 50, 15, target),source,target);

        String json = "{\"account\":\"Source\"," +
                "\"amount\":50.0," +
                "\"reservations\":[{" +
                "\"id\":1," +
                "\"amount\":30.0" +
                "},{" +
                "\"id\":4," +
                "\"amount\":15.0}" +
                "]}";

        prepareRestTemplate(URL,json);
        pps.initiateTransactionRental(transRen);

        Assert.assertTrue(transRen.getId() == 4);
    }

    @Test
    public void testRechargeCredit() {
        User user = this.createUser("user");
        Mockito.doNothing().when(pps).callURL(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
        pps.rechargeCredit(user,50);
        Mockito.verify(pps, times(1)).rechargeCredit(user,50);

    }

    @Test
    public void testInitiateTransactionPurchase() {
        User source = this.createUser("Source");
        User target = this.createUser("Target");
        SellableItem item = new SellableItem("itemName", "desc",50,target);
        TransactionPurchase transPur = new TransactionPurchase(item,source,target);
        Mockito.doNothing().when(pps).callURL(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
        pps.initiateTransactionPurchase(transPur);
        Mockito.verify(pps, times(1)).initiateTransactionPurchase(transPur);
    }

    @Test
    public void testReleaseDeposit() {
        User source = this.createUser("Source");
        User target = this.createUser("Target");
        TransactionRental transRen = new TransactionRental(50,15,0L,new LendableItem("itemName", "desc", 50, 15, target),source,target);

        Mockito.doNothing().when(pps).callURL(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
        pps.releaseDeposit(source,transRen);
        Mockito.verify(transRenService, times(1)).setDepositRevoked(transRen,"Nein");
    }

    @Test
    public void testPunishDeposit() {
        User source = this.createUser("Source");
        User target = this.createUser("Target");
        TransactionRental transRen = new TransactionRental(50,15,0L,new LendableItem("itemName", "desc", 50, 15, target),source,target);

        Mockito.doNothing().when(pps).callURL(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
        pps.punishDeposit(source,transRen);
        Mockito.verify(transRenService, times(1)).setDepositRevoked(transRen,"Ja");
    }

    @Test
    public void testEnoughCreditRental() {
        LocalDate starttime = LocalDate.now();
        LocalDate endtime = LocalDate.now().plusDays(9L); //this now is at least few milliseconds later than the one above => 10 Days
        User source1 = this.createUser("Source1");
        User source2 = this.createUser("Source2");
        LendableItem item = new LendableItem("itemName", "desc", 50, 15, this.createUser("Seller"));
        Account account1 = new Account(source1.getUsername(), 515, new ArrayList<>());
        Account account2 = new Account(source2.getUsername(), 514, new ArrayList<>());

        Mockito.doReturn(account1).when(pps).getAccount(source1);
        Mockito.doReturn(account2).when(pps).getAccount(source2);

        //cannot borrow same item at the same time
        boolean enoughCredit1 = pps.enoughCredit(source1, item, starttime, endtime);
        boolean enoughCredit2 = pps.enoughCredit(source2, item, starttime.plusDays(11L), endtime.plusDays(11L));

        Assertions.assertThat(enoughCredit1).isTrue();
        Assertions.assertThat(enoughCredit2).isFalse();
    }

    @Test
    public void testEnoughCreditPurchase() {
        User source1 = this.createUser("Source1");
        User source2 = this.createUser("Source2");
        SellableItem item1 = new SellableItem("itemName", "desc", 515, source2);
        SellableItem item2 = new SellableItem("itemName", "desc", 515, source1);
        Account account1 = new Account(source1.getUsername(), 515, new ArrayList<>());
        Account account2 = new Account(source2.getUsername(), 514, new ArrayList<>());

        Mockito.doReturn(account1).when(pps).getAccount(source1);
        Mockito.doReturn(account2).when(pps).getAccount(source2);

        //cannot borrow same item at the same time
        boolean enoughCredit1 = pps.enoughCredit(source1, item1);
        boolean enoughCredit2 = pps.enoughCredit(source2, item2);

        Assertions.assertThat(enoughCredit1).isTrue();
        Assertions.assertThat(enoughCredit2).isFalse();
    }

    @Test
    public void testGetDepositSum() {
        User source1 = this.createUser("Source1");
        ArrayList<Reservation> reservations = new ArrayList<>();
        Reservation reservation1 = new Reservation(1L,100);
        Reservation reservation2 = new Reservation(2L,200);
        Reservation reservation3 = new Reservation(3L,30);
        reservations.add(reservation1);
        reservations.add(reservation2);
        reservations.add(reservation3);
        Account account1 = new Account(source1.getUsername(), 515, reservations);
        Assertions.assertThat(pps.getDepositSum(account1)).isEqualTo(330);
    }

    @Test(expected = NotFoundException.class)
    public void testCallURL() throws Exception {
        String URL = "http://localhost:8888/account/" + "user" + "/";
        pps.callURL(URL,"POST", 3);
    }
}
