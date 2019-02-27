package de.hhu.sharing.data;

import de.hhu.sharing.data.TransactionRentalRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.propay.TransactionRental;
import de.hhu.sharing.model.User;
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
public class TransactionRentalRepositoryTest {

    @Autowired
    TransactionRentalRepository transRepo;

    @Autowired
    UserRepository userRepo;

    @MockBean
    StorageService storageService;


    @Test
    public void testFindAll() {
        TransactionRental trans1 = new TransactionRental();
        TransactionRental trans2 = new TransactionRental();
        trans1.setId(1L);
        trans2.setId(2L);
        transRepo.save(trans1);
        transRepo.save(trans2);

        Assertions.assertThat(transRepo.findAll().size()).isEqualTo(2);
    }

    @Test
    public void testFindTargetTransactionRental() {
        Address sharedAdress = new Address("Gemeinsames", "Wohnhaus", 1234);
        User source = new User();
        User target = new User();
        source.setUsername("source");
        source.setAddress(sharedAdress);
        target.setUsername("target");
        target.setAddress(sharedAdress);

        userRepo.save(source);
        userRepo.save(target);

        source = userRepo.findByUsername("source").get();
        target = userRepo.findByUsername("target").get();

        TransactionRental trans3 = new TransactionRental();
        TransactionRental trans4 = new TransactionRental();
        TransactionRental trans5 = new TransactionRental();

        trans3.setId(3L);
        trans4.setId(4L);
        trans5.setId(5L);

        trans3.setSender(source);
        trans3.setReceiver(target);
        trans4.setSender(source);
        trans4.setReceiver(target);
        trans5.setSender(target);
        trans5.setReceiver(source);

        transRepo.save(trans3);
        transRepo.save(trans4);
        transRepo.save(trans5);

        Assertions.assertThat(transRepo.findAllByReceiver(target).size()).isEqualTo(2);
        Assertions.assertThat(transRepo.findAllByReceiver(target).contains(trans3)).isTrue();
        Assertions.assertThat(transRepo.findAllByReceiver(target).contains(trans4)).isTrue();
    }

    @Test
    public void testFindSourceTransactionRental() {
        Address sharedAdress = new Address("Gemeinsames", "Wohnhaus", 1234);
        User source = new User();
        User target = new User();
        source.setUsername("source");
        source.setAddress(sharedAdress);
        target.setUsername("target");
        target.setAddress(sharedAdress);

        userRepo.save(source);
        userRepo.save(target);

        source = userRepo.findByUsername("source").get();
        target = userRepo.findByUsername("target").get();

        TransactionRental trans3 = new TransactionRental();
        TransactionRental trans4 = new TransactionRental();
        TransactionRental trans5 = new TransactionRental();

        trans3.setId(3L);
        trans4.setId(4L);
        trans5.setId(5L);

        trans3.setSender(source);
        trans3.setReceiver(target);
        trans4.setSender(source);
        trans4.setReceiver(target);
        trans5.setSender(target);
        trans5.setReceiver(source);

        transRepo.save(trans3);
        transRepo.save(trans4);
        transRepo.save(trans5);

        Assertions.assertThat(transRepo.findAllBySender(source).size()).isEqualTo(2);
        Assertions.assertThat(transRepo.findAllBySender(source).contains(trans3)).isTrue();
        Assertions.assertThat(transRepo.findAllBySender(source).contains(trans4)).isTrue();
    }

    @Test
    public void testFindByProcessId() {
        Address sharedAdress = new Address("Gemeinsames", "Wohnhaus", 1234);
        User source = new User();
        User target = new User();
        source.setUsername("source");
        source.setAddress(sharedAdress);
        target.setUsername("target");
        target.setAddress(sharedAdress);

        userRepo.save(source);
        userRepo.save(target);

        source = userRepo.findByUsername("source").get();
        target = userRepo.findByUsername("target").get();

        TransactionRental trans3 = new TransactionRental();
        TransactionRental trans4 = new TransactionRental();
        TransactionRental trans5 = new TransactionRental();

        trans3.setId(3L);
        trans4.setId(4L);
        trans5.setId(5L);

        trans3.setSender(source);
        trans3.setReceiver(target);
        trans4.setSender(source);
        trans4.setReceiver(target);
        trans5.setSender(target);
        trans5.setReceiver(source);

        trans3.setProcessId(3L);
        trans4.setProcessId(4L);
        trans5.setProcessId(5L);

        transRepo.save(trans3);
        transRepo.save(trans4);
        transRepo.save(trans5);

        Assertions.assertThat(transRepo.findByProcessId(3L)).isNotEqualTo(null);
        Assertions.assertThat(transRepo.findByProcessId(4L)).isNotEqualTo(null);
        Assertions.assertThat(transRepo.findByProcessId(5L)).isNotEqualTo(null);
    }
}
