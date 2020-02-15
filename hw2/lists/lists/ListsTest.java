package lists;
import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *
 *  @author Hamza Kamran Khawaja
 */

public class ListsTest {
    /* FIXME
     */
     @Test
        public void Test1() {

              IntList toTest = IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);
              IntListList solution = IntListList.list(IntList.list(1, 3, 7), IntList.list(5), IntList.list(4, 6, 9, 10),
                      IntList.list(10, 11));
              IntListList test = Lists.naturalRuns(toTest);
             assertEquals(solution, test);
     }


    // It might initially seem daunting to try to set up
    // IntListList expected.
    //
    // There is an easy way to get the IntListList that you want in just
    // few lines of code! Make note of the IntListList.list method that
    // takes as input a 2D array.

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
