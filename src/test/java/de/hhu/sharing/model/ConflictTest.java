package de.hhu.sharing.model;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.LocalDate;

public class ConflictTest {

    private User generateUser(String username) {
        LocalDate birthdate = LocalDate.of(2000, 1, 1);
        Address address = new Address("unistrase", "duesseldorf", 40233);
        User user = new User(username, "password", "role", "lastname", "forename", "email", birthdate, address);
        return user;
    }

    private LendableItem generateItem(User user) {
        return new LendableItem("apfel", "lecker", 1, 1, user);
    }

    @Test
    public void testAddToMessages(){
        User user1 = generateUser("user1");
        User user2 = generateUser("user2");
        LendableItem item = generateItem(user1);
        BorrowingProcess process = new BorrowingProcess(item, new Period(LocalDate.now(),LocalDate.now().plusDays(1)));
        Conflict conf = new Conflict(user1,user2,process,new Message("user1","content"));
        conf.addToMessages(new Message("user2","content2"));
        Assertions.assertThat(conf.getMessages().size()).isEqualTo(2);
    }
}
