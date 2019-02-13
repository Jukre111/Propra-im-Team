package de.hhu.sharing;

import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.User;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepo;

    @Test
    public void testFindAll() {
        User user1 = new User();
        User user2 = new User();

        // Address is needed to compile the test
        Address add1 = new Address();

        add1.setCity("Duesseldorf");
        add1.setPostcode(40233);
        add1.setStreet("Universitaetsstrasse 1");

        user1.setName("Nutzer1");
        user1.setMail("Person1@test.de");
        user1.setPassword("pswd");
        user1.setAddress(add1);

        user2.setName("Nutzer2");
        user2.setMail("Person2@test.de");
        user2.setPassword("pswd");
        user2.setAddress(add1);

        userRepo.save(user1);
        userRepo.save(user2);

        Assertions.assertThat(userRepo.findAll().size()).isEqualTo(2);
    }
}
