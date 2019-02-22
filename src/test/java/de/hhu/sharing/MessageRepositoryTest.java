package de.hhu.sharing;

import de.hhu.sharing.data.MessageRepository;
import de.hhu.sharing.model.Message;
import de.hhu.sharing.storage.StorageService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MessageRepositoryTest {
    @Autowired
    MessageRepository msgRepo;

    @MockBean
    StorageService storageService;

    @Test
    public void testFindAll() {
        Message msg1 = new Message();
        Message msg2 = new Message();

        msg1.setSubject("msg1");
        msg2.setSubject("msg2");

        msgRepo.save(msg1);
        msgRepo.save(msg2);

        Assertions.assertThat(msgRepo.findAll().size()).isEqualTo(2);
    }
}
