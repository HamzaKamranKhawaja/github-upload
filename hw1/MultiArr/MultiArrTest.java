import static org.junit.Assert.*;
import java.util.Arrays;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int[][] a = {{1, 2, 3}, {4, 5, 6}};
        int[][] b = {{4}, {9}};
        assertEquals( 6 , MultiArr.maxValue(a) );
        assertEquals( 9 , MultiArr.maxValue(b) );

    }

    @Test
    public void testAllRowSums() {
        int[][] a = {{1, 2, 3}, {4, 5, 6}};
        int[][] b = {{4}, {9}};
        int[] aa = new int[] {6, 15};
        int[] bb = new int[] {4, 9};

        assertEquals(true, Arrays.equals( aa, MultiArr.allRowSums(a) ));
        assertEquals(true, Arrays.equals( bb, MultiArr.allRowSums(b) ));


    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
