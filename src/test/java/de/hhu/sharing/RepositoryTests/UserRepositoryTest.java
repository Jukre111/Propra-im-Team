package de.hhu.sharing.RepositoryTests;

import de.hhu.sharing.data.BorrowingProcessRepository;
import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.*;
import de.hhu.sharing.storage.StorageService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepo;

    @Autowired
    BorrowingProcessRepository bPRepo;

    @Autowired
    ItemRepository itemRepo;

    @MockBean
    StorageService storageService;

    public User createUser(String username){
        LocalDate birthdate = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User(username,"password", "role", "lastnmae", "forname", "email",birthdate,address);
        userRepo.save(user);
        return userRepo.findByUsername(username).get();
    }

    public BorrowingProcess createBorrowedProcess(){

        User user = createUser("testman");

        LocalDate startdate = LocalDate.of(2010,1,1);
        LocalDate enddate = LocalDate.of(2010,2,2);
        Period period = new Period(startdate,enddate);

        lendableItem lendableItem = new lendableItem("apfel", "lecker",1,1 ,user);
        itemRepo.save(lendableItem);
        itemRepo.findById(lendableItem.getId()).get();

        BorrowingProcess bP = new BorrowingProcess(lendableItem,period);
        bPRepo.save(bP);
        return bPRepo.findById(bP.getId()).get();
    }


    @Test
    public void testFindAll() {
        createUser("testman1");
        createUser("testman2");
        Assertions.assertThat(userRepo.findAll().size()).isEqualTo(2);
    }

    @Test
    public void testFindByUsername(){

        createUser("testman");
        Optional<User> optionalUser = userRepo.findByUsername("testman");
        Assertions.assertThat(optionalUser.get().getUsername()).isEqualTo("testman");
    }

    @Test
    public void testFindByBorrowed_id(){

        User user = createUser("testman");
        BorrowingProcess bP = createBorrowedProcess();

        user.addToBorrowed(bP);

        Optional<User> optionalUser = userRepo.findByBorrowed_id(bP.getId());

        Assertions.assertThat(optionalUser.get().getBorrowed().get(0).getId()).isEqualTo(bP.getId());

    }

}
