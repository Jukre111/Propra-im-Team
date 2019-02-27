package de.hhu.sharing.model;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class PeriodTest {



    @Test
    public void testOverlapsWithStartdatesAreEqual(){
        Period period = new Period (LocalDate.of(2000,1,1), LocalDate.of(2000,5,5));
        Period period1 = new Period (LocalDate.of(2000,1,1), LocalDate.of(2000,6,6));

        Assert.assertTrue(period.overlapsWith(period1)==true);
    }

    @Test
    public void testOverlapsWithStartDateEqualsEnddate(){
        Period period = new Period (LocalDate.of(2000,1,1), LocalDate.of(2000,5,5));
        Period period1 = new Period (LocalDate.of(1999,1,1), LocalDate.of(2000,1,1));

        Assert.assertTrue(period.overlapsWith(period1)==true);
    }

    @Test
    public void testOverlapsWithEnddateEqualsStartDate(){
        Period period = new Period (LocalDate.of(2000,1,1), LocalDate.of(2000,5,5));
        Period period1 = new Period (LocalDate.of(2000,5,5), LocalDate.of(2000,6,6));

        Assert.assertTrue(period.overlapsWith(period1)==true);
    }

    @Test
    public void testOverlapsWithEnddatesAreEqual(){
        Period period = new Period (LocalDate.of(2000,1,1), LocalDate.of(2000,6,6));
        Period period1 = new Period (LocalDate.of(2000,5,5), LocalDate.of(2000,6,6));

        Assert.assertTrue(period.overlapsWith(period1)==true);
    }


    @Test
    public void testOverlapsWithStartdateBeforeStartdateAndEnddateAfterStartdate(){
        Period period = new Period (LocalDate.of(2000,1,1), LocalDate.of(2000,6,6));
        Period period1 = new Period (LocalDate.of(2000,5,5), LocalDate.of(2000,7,7));

        Assert.assertTrue(period.overlapsWith(period1)==true);
    }

    @Test
    public void testOverlapsWithStartdateBeforeEnddateAndEnddateAfterEnddate(){
        Period period = new Period (LocalDate.of(2000,1,1), LocalDate.of(2000,6,6));
        Period period1 = new Period (LocalDate.of(2000,5,5), LocalDate.of(2000,7,7));

        Assert.assertTrue(period.overlapsWith(period1)==true);
    }

    @Test
    public void testOverlapsWithStartdateAfterStartdateAndEnddateBeforeEndate(){
        Period period = new Period (LocalDate.of(2000,6,6), LocalDate.of(2000,6,6));
        Period period1 = new Period (LocalDate.of(2000,5,5), LocalDate.of(2000,7,7));

        Assert.assertTrue(period.overlapsWith(period1)==true);
    }

    @Test
    public void testNotOverlapsWith(){
        Period period = new Period (LocalDate.of(2000,1,1), LocalDate.of(2000,1,2));
        Period period1 = new Period (LocalDate.of(2000,1,3), LocalDate.of(2000,2,1));

        Assert.assertTrue(period.overlapsWith(period1)==false);
    }


    @Test
    public void testIsOutdated(){
        Period period = new Period (LocalDate.of(2000,1,1), LocalDate.of(2000,1,2));
        Assert.assertTrue(period.isOutdated() == true);
    }

    @Test
    public void testIsNotOutdated(){
        Period period = new Period (LocalDate.of(2020,1,1), LocalDate.of(2020,1,2));
        Assert.assertTrue(period.isOutdated() == false);
    }
}
