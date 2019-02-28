package de.hhu.sharing.model;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.LocalDate;

public class LendableitemTest {
    private User generateUser(String username) {
        LocalDate birthdate = LocalDate.of(2000, 1, 1);
        Address address = new Address("unistrase", "duesseldorf", 40233);
        User user = new User(username, "password", "role", "lastname", "forename", "email", birthdate, address);
        return user;
    }

    private LendableItem generateItem(User user) {
        return new LendableItem("apfel", "lecker", 1, 1, user);
    }

    private Period generatePeriod(int startday, int endday) {
        LocalDate startdate = LocalDate.of(2019, 4, startday);
        LocalDate enddate = LocalDate.of(2019, 4, endday);
        return new Period(startdate, enddate);
    }

    @Test
    public void testAddToRequests(){
        User user1 = generateUser("user1");
        User user2 = generateUser("user2");
        LendableItem item = generateItem(user1);
        Request req = new Request(new Period(LocalDate.now(),LocalDate.now().plusDays(1)),user2);
        item.addToRequests(req);
        Assertions.assertThat(item.getRequests().size()).isEqualTo(1);
    }

    @Test
    public void testRemoveFromRequests(){
        User user1 = generateUser("user1");
        User user2 = generateUser("user2");
        LendableItem item = generateItem(user1);
        Request req = new Request(new Period(LocalDate.now(),LocalDate.now().plusDays(1)),user2);
        item.addToRequests(req);
        item.removeFromRequests(req);
        Assertions.assertThat(item.getRequests().size()).isEqualTo(0);
    }

    @Test
    public void testAddToPeriods(){
        User user1 = generateUser("user1");
        LendableItem item = generateItem(user1);
        item.addToPeriods(new Period(LocalDate.now(),LocalDate.now().plusDays(1)));
        Assertions.assertThat(item.getPeriods().size()).isEqualTo(1);
    }

    @Test
    public void testRemoveFromPeriods(){
        User user1 = generateUser("user1");
        LendableItem item = generateItem(user1);
        Period period = new Period(LocalDate.now(),LocalDate.now().plusDays(1));
        item.addToPeriods(period);
        item.removeFromPeriods(period);
        Assertions.assertThat(item.getPeriods().size()).isEqualTo(0);
    }

    @Test
    public void testNoPeriodsAndRequests(){
        User user1 = generateUser("user1");
        LendableItem item = generateItem(user1);
        Assertions.assertThat(item.noPeriodsAndRequests()).isTrue();
    }

    @Test
    public void testIsAvailableFalse(){
        User user1 = generateUser("user1");
        LendableItem item = generateItem(user1);
        Period period = new Period(LocalDate.now(),LocalDate.now().plusDays(1));
        Period period2 = new Period(LocalDate.now(),LocalDate.now().plusDays(3));
        item.addToPeriods(period);
        Assertions.assertThat(item.isAvailableAt(period2)).isFalse();
    }

    @Test
    public void testIsAvailableTrue(){
        User user1 = generateUser("user1");
        LendableItem item = generateItem(user1);
        Period period = new Period(LocalDate.now(),LocalDate.now().plusDays(1));
        Period period2 = new Period(LocalDate.now().plusDays(2),LocalDate.now().plusDays(3));
        item.addToPeriods(period);
        Assertions.assertThat(item.isAvailableAt(period2)).isTrue();
    }
}
