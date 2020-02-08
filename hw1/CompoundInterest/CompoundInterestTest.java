import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.Math;

public class CompoundInterestTest {
    static final double DELTA = 1e-15;


    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.
        */
        assertEquals(5,CompoundInterest.numYears(2025));
        assertEquals(10,CompoundInterest.numYears(2030));
        assertEquals(0,CompoundInterest.numYears(2020));
        assertEquals(1000,CompoundInterest.numYears(3020));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(12.544,CompoundInterest.futureValue(10, 12, 2022), tolerance);
        assertEquals(9.752,CompoundInterest.futureValue(10, -0.5, 2025), tolerance);

    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(295712.29,CompoundInterest.futureValueReal(1000000, 0.0, 2060, 3), tolerance);
        assertEquals(105.88,CompoundInterest.futureValueReal(100, 5.0, 2022, 2.0), tolerance);


    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(771.56, CompoundInterest.totalSavings(100.00,
                        2025, 10.0 ), tolerance);
        assertEquals(7.0,
                CompoundInterest.totalSavings(1.00,
                        2022, 100.0 ), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(500, CompoundInterest.totalSavingsReal(100.00, 2025, 10.0, 10/1.1 ), tolerance);
        assertEquals(2.0,
                CompoundInterest.totalSavingsReal(1.00,
                        2022, 100.0, 50 ), tolerance);
    }



    /* Run the unit tests in this file. */
    public static void main(String... args) {
        //System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
