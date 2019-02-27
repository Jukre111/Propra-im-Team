package de.hhu.sharing.model;

import org.junit.Assert;
import org.junit.Test;

public class UserTest {


    public User createUser(){
        return new User();
    }

    public BorrowingProcess createProcess(){
        return new BorrowingProcess();
    }

    @Test
    public void testAddToBorrowed(){

        User user  = createUser();
        BorrowingProcess process = createProcess();
        user.addToBorrowed(process);
        Assert.assertTrue(user.getBorrowed().size() == 1);
        Assert.assertTrue(user.getBorrowed().get(0).equals (process));

    }

    @Test
    public void testRemoveFromBorrowed(){
        User user  = createUser();
        BorrowingProcess process1 = createProcess();
        BorrowingProcess process2 = createProcess();

        user.addToBorrowed(process1);
        user.addToBorrowed(process2);

        user.removeFromBorrowed(process1);

        Assert.assertTrue(user.getBorrowed().size() == 1);
        Assert.assertTrue(user.getBorrowed().get(0).equals (process2));

    }

    @Test
    public void testAddToLend(){
        User user  = createUser();
        BorrowingProcess process = createProcess();
        user.addToLend(process);
        Assert.assertTrue(user.getLend().size() == 1);
        Assert.assertTrue(user.getLend().get(0).equals (process));

    }

    @Test
    public void testRemoveFromLend(){
        User user  = createUser();
        BorrowingProcess process1 = createProcess();
        BorrowingProcess process2 = createProcess();

        user.addToLend(process1);
        user.addToLend(process2);

        user.removeFromLend(process1);

        Assert.assertTrue(user.getLend().size() == 1);
        Assert.assertTrue(user.getLend().get(0).equals (process2));

    }
}
