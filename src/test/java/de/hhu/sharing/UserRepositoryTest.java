package de.hhu.sharing;

import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.User;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

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

        user1.setUsername("Nutzer1");
        user1.setForename("Joe");
        user1.setLastname("Karl");
        user1.setRole("ROLE_ADMIN");
        user1.setEmail("Person1@test.de");
        user1.setPassword("pswd");
        user1.setAddress(add1);

        LocalDate date1 = LocalDate.of(2019, 5, 13);
        user1.setBirthdate(date1);

        user2.setUsername("Nutzer2");
        user2.setForename("Joey");
        user2.setLastname("Karlus");
        user2.setRole("ROLE_ADMIN");
        user2.setEmail("Person2@test.de");
        user2.setPassword("pswd");
        user2.setAddress(add1);


        LocalDate date2 = LocalDate.of(2014, 5, 13);
        user2.setBirthdate(date2);

        userRepo.save(user1);
        userRepo.save(user2);

        Assertions.assertThat(userRepo.findAll().size()).isEqualTo(2);
    }

    @Test
    public void testFindByUsername(){

        User user = new User();

        Address add = new Address();

        add.setCity("Duesseldorf");
        add.setPostcode(40233);
        add.setStreet("Universitaetsstrasse 1");

        user.setUsername("Nutzer1");
        user.setForename("Joe");
        user.setLastname("Karl");
        user.setRole("ROLE_ADMIN");
        user.setEmail("Person1@test.de");
        user.setPassword("pswd");
        user.setAddress(add);

        LocalDate date1 = LocalDate.of(2019, 5, 13);
        user.setBirthdate(date1);

        userRepo.save(user);

        Optional<User> optionalUser = userRepo.findByUsername("Nutzer1");

        Assert.assertTrue(optionalUser.isPresent());
        if(optionalUser.isPresent()){
            Assertions.assertThat(optionalUser.get().getUsername()).isEqualTo("Nutzer1");
        }

    }
}
