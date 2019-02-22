package de.hhu.sharing.ServiceTests;

import de.hhu.sharing.data.TransactionRepository;
import de.hhu.sharing.propay.Account;
import de.hhu.sharing.propay.Transaction;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import de.hhu.sharing.services.ProPayService;
import org.springframework.web.client.RestTemplate;

import static org.mockito.internal.verification.VerificationModeFactory.times;

public class ProPayServiceTest {

    @Mock
    TransactionRepository transRepo;
    @Mock
    RestTemplate rt;
    @InjectMocks
    ProPayService pps = new ProPayService();

    @Before
    public void initialize(){
        MockitoAnnotations.initMocks(this);
    }

    private void prepareRestTemplate(String URL, String json) {
        rt = Mockito.mock(RestTemplate.class);
        Mockito.when(rt.getForObject(URL, String.class)).thenReturn(json);
        pps.changeTemplateTo(rt);
    }

    @Test
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
        Account account = pps.showAccount(userSrc);

        Assertions.assertThat(account).isNotEqualTo(null);
        Assertions.assertThat(account.getAmount()).isEqualTo(50);
        Assertions.assertThat(account.getAccount()).isEqualTo("Source");
    }

    @Test
    public void testCreateDeposit() {
        String userSrc = "Source";
        String userTar = "Target";
        String URL = "http://localhost:8888/account/" + userSrc + "/";
        Transaction trans = new Transaction();
        trans.setDeposit(666);
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
        pps.createDeposit(userSrc, userTar, trans);
        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        Mockito.verify(transRepo, times(1)).save(captor.capture());
        Assert.assertTrue(captor.getAllValues().get(0).getReservationId() == 2);

    }

    @Test
    public void checkNullPointers() {
        Mockito.when(pps.showAccount("user4")).thenReturn(null);
        int response = pps.createAccount("user4");
        Assertions.assertThat(response).isEqualTo(-1);
        Assert.assertTrue(true);
    }

}
