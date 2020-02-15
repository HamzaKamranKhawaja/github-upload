package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class ArraysTest {
    /**
     */
    @Test
    public void catenateTest() {

        int[] A = new int[] {1,2,3,4,5};
        int[] B = new int[] {6,7,8,9,10};

        int[] expected = new int[] {1,2,3,4,5,6,7,8,9,10};

        assertArrayEquals(expected, Arrays.catenate(A,B));
    }

    @Test
    public void removeTest() {

        int[] testArray = new int[] {1,2,3,4,5,6,7,8,9,10};

        int[] expected = new int[] {1,2,3,4,5};

        assertArrayEquals(expected, Arrays.remove(testArray, 5, 5));
    }

    @Test
    public void naturalRunsTest() {

        int[] testArray = new int[] {1, 3, 7, 5, 4, 6, 9, 10};

        int[][] expected = new int[][] {{1, 3, 7}, {5}, {4, 6, 9, 10}};

        assertArrayEquals(expected, Arrays.naturalRuns(testArray));
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
