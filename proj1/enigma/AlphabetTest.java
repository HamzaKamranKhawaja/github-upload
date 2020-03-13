package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Hamza Kamran
 */
public class AlphabetTest {

    /**
     * Testing time limit.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(100);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /**
     * Check that perm has an alphabet whose size is that of
     * FROMALPHA and TOALPHA and that maps each character of
     * FROMALPHA to the corresponding character of FROMALPHA, and
     * vice-versa. TESTID is used in error messages.
     */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void containsTest() {
        Permutation p = new Permutation("(A)(B)(C)", new Alphabet("ABC"));
        assertTrue(p.alphabet().contains('C'));
        assertTrue(p.alphabet().contains('A'));
        assertFalse(p.alphabet().contains('D'));

    }
    @Test
    public void toCharTest() {
        Alphabet A = new Alphabet("ABCGDEH");
        assertEquals('A', A.toChar(0));
        assertEquals('B', A.toChar(1));
        assertEquals('C', A.toChar(2));
        assertEquals('G', A.toChar(3));
        assertEquals('D', A.toChar(4));
        assertEquals('E', A.toChar(5));
        assertEquals('H', A.toChar(6));



    }
    @Test
    public void toIntTest() {
        Alphabet A = new Alphabet("ABCGDEH");
        assertEquals(0, A.toInt('A'));
        assertEquals(1, A.toInt('B'));
        assertEquals(2, A.toInt('C'));
        assertEquals(4, A.toInt('D'));
        assertEquals(5, A.toInt('E'));
        assertEquals(3, A.toInt('G'));
        assertEquals(6, A.toInt('H'));
    }

}
