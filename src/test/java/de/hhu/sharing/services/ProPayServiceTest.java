package de.hhu.sharing.services;

import de.hhu.sharing.data.TransactionRentalRepository;
import de.hhu.sharing.propay.Account;
import de.hhu.sharing.propay.TransactionRental;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import de.hhu.sharing.services.ProPayService;
import org.springframework.web.client.RestTemplate;

import static org.mockito.internal.verification.VerificationModeFactory.times;

public class ProPayServiceTest {

    /*@Mock
    TransactionRentalRepository transRepo;
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
        rt = Mockito.mock(RestTemplate.class);
        Mockito.when(rt.getForObject(URL, String.class)).thenReturn(json);
        //pps.changeTemplateTo(rt);
    }

    /*@Test
    public void testShowAccount() {
        String userSrc = "Source";
        String URL = "http://localhost:8888/account/" + userSrc + "/";
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
    }*/

   // @Test
   // public void testCreateDeposit() {
       /* String userSrc = "Source";
        String userTar = "Target";
        String URL = "http://localhost:8888/account/" + userSrc + "/";
        TransactionRental transRen = new TransactionRental();
        transRen.setDeposit(666);
        String json = "{\"account\":\"Source\"," +
                "\"amount\":50.0," +
                "\"reservations\":[{" +
                "\"id\":1," +
                "\"amount\":30.0" +
                "},{" +
                "\"id\":2," +
                "\"amount\":15.0}" +
                "]}";

        prepareRestTemplate(URL,json);
        Mockito.doReturn(200).when(pps).callURL(Mockito.anyString(), Mockito.anyString());
        int response = pps.createDeposit(userSrc, userTar, transRen);
        ArgumentCaptor<TransactionRental> captor = ArgumentCaptor.forClass(TransactionRental.class);
        Mockito.verify(transRepo, times(1)).save(captor.capture());
        Assert.assertTrue(captor.getAllValues().get(0).getReservationId() == 2);
        Assertions.assertThat(response).isEqualTo(200);*/
   // }

 //   @Test
 //   public void checkNullPointers() {
       /* Mockito.when(pps.getAccount("user4")).thenReturn(null);
        int response = pps.createAccount("user4");
        Assertions.assertThat(response).isEqualTo(-1);
        Assert.assertTrue(true);
    }*/

}
