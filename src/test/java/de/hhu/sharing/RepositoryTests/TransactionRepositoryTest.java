package de.hhu.sharing.RepositoryTests;

import de.hhu.sharing.data.TransactionRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.propay.Transaction;
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
public class TransactionRepositoryTest {

    @Autowired
    TransactionRepository transRepo;

    @Autowired
    UserRepository userRepo;

    @MockBean
    StorageService storageService;


    @Test
    public void testFindAll() {
        Transaction trans1 = new Transaction();
        Transaction trans2 = new Transaction();
      /*  trans1.setReservationId(1);
        trans2.setReservationId(2);
        transRepo.save(trans1);
        transRepo.save(trans2);

        Assertions.assertThat(transRepo.findAll().size()).isEqualTo(2);*/
    }

    @Test
    public void testFindTargetTransaction() {
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

        Transaction trans3 = new Transaction();
        Transaction trans4 = new Transaction();
        Transaction trans5 = new Transaction();

       /* trans3.setReservationId(3);
        trans4.setReservationId(4);
        trans5.setReservationId(5);

        trans3.setSource(source);
        trans3.setTarget(target);
        trans4.setSource(source);
        trans4.setTarget(target);
        trans5.setSource(target);
        trans5.setTarget(source);

        transRepo.save(trans3);
        transRepo.save(trans4);
        transRepo.save(trans5);

        Assertions.assertThat(transRepo.findByTarget(target).size()).isEqualTo(2);
        Assertions.assertThat(transRepo.findByTarget(target).contains(trans3)).isTrue();
        Assertions.assertThat(transRepo.findByTarget(target).contains(trans4)).isTrue();*/
    }

    @Test
    public void testFindSourceTransaction() {
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

        Transaction trans3 = new Transaction();
        Transaction trans4 = new Transaction();
        Transaction trans5 = new Transaction();

       /* trans3.setReservationId(3);
        trans4.setReservationId(4);
        trans5.setReservationId(5);

        trans3.setSource(source);
        trans3.setTarget(target);
        trans4.setSource(source);
        trans4.setTarget(target);
        trans5.setSource(target);
        trans5.setTarget(source);

        transRepo.save(trans3);
        transRepo.save(trans4);
        transRepo.save(trans5);

        Assertions.assertThat(transRepo.findBySource(source).size()).isEqualTo(2);
        Assertions.assertThat(transRepo.findBySource(source).contains(trans3)).isTrue();
        Assertions.assertThat(transRepo.findBySource(source).contains(trans4)).isTrue();*/
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

        Transaction trans3 = new Transaction();
        Transaction trans4 = new Transaction();
        Transaction trans5 = new Transaction();

     /*   trans3.setReservationId(3);
        trans4.setReservationId(4);
        trans5.setReservationId(5);

        trans3.setSource(source);
        trans3.setTarget(target);
        trans4.setSource(source);
        trans4.setTarget(target);
        trans5.setSource(target);
        trans5.setTarget(source);

        trans3.setProcessId(3L);
        trans4.setProcessId(4L);
        trans5.setProcessId(5L);

        transRepo.save(trans3);
        transRepo.save(trans4);
        transRepo.save(trans5);

        Assertions.assertThat(transRepo.findByProcessId(3L)).isNotEqualTo(null);
        Assertions.assertThat(transRepo.findByProcessId(4L)).isNotEqualTo(null);
        Assertions.assertThat(transRepo.findByProcessId(5L)).isNotEqualTo(null);*/
    }
}
