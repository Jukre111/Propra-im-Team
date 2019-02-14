package de.hhu.sharing.model;

public class Account {
    String account;
    int ammount;
    Reservation[] reservations;

    public String getReservationStrings() {
        String string = "";

        for(int i = 0; i < reservations.length; i++) {
            string += reservations[i].id + " ";
        }

        return string;
    }
}
