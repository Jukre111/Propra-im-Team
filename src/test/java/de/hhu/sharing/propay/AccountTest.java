package de.hhu.sharing.propay;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;

public class AccountTest {

    @Test
    public void testGetLastReservationId() {
        ArrayList<Reservation> reservationList = new ArrayList();
        Account acc = new Account("acc",100, reservationList);
        Reservation res1 = new Reservation(1L,10);
        Reservation res2 = new Reservation(2L,20);
        reservationList.add(res1);
        reservationList.add(res2);
        Assertions.assertThat(acc.getLastReservationId()).isEqualTo(2L);
    }
    @Test
    public void testGetLastReservationIdNull() {
        ArrayList<Reservation> reservationList = new ArrayList();
        Account acc = new Account("acc",100, reservationList);
        Assertions.assertThat(acc.getLastReservationId()).isEqualTo(null);
    }
}
