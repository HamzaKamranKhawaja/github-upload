package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class PermutationTest {

    //TODO: CHECK IMPLEMENTATION OF ALPHABET. DOES NOT SEEM
    //TODO: TO BE GENERAL. ABCDEF... INSTEAD OF ABD OR OTHER RANDOM ALPHA SET.
    /**
     * Testing time limit.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(100000);

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

//    @Test
//    public void testInvertChar(){
//        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
//        assertEquals('B', p.invert('A'));
//        assertEquals('A', p.invert('C'));
//        assertEquals('C', p.invert('D'));
//        assertEquals('D', p.invert('B'));
//
//    }

    @Test
    public void testSize() {
        Permutation p = new Permutation("(ABD)(C)(EFG)", new Alphabet("ABCDEFG"));
        assertEquals(7, p.size());

        Permutation h = new Permutation("(ABCDEFGHIJK)(LM)(N)", new Alphabet("ABCDEFGHIJKLMN"));
        assertEquals(14, h.size());
    }

    @Test
    public void testPermuteInt() {
        Permutation p = new Permutation("(ABD)(C)(EFG)", new Alphabet("ABCDEFG"));
        assertEquals(1, p.permute(0));
        assertEquals(3, p.permute(1));
        assertEquals(2, p.permute(2));
        assertEquals(5, p.permute(4));
        assertEquals(0, p.permute(3));
        assertEquals(4, p.permute(6));
    }

    @Test
    public void testInvertInt() {
        Permutation p = new Permutation("(ABD)(C)(EFG)", new Alphabet("ABCDEFG"));
        assertEquals(3, p.invert(0));
        assertEquals(0, p.invert(1));
        assertEquals(2, p.invert(2));
        assertEquals(1, p.invert(3));
        assertEquals(6, p.invert(4));

    }

    @Test
    public void testPermuteChar() {
        Permutation p = new Permutation("(ABD)(C)(EFG)", new Alphabet("ABCDEFGHI"));
        assertEquals('B', p.permute('A'));
        assertEquals('D', p.permute('B'));
        assertEquals('A', p.permute('D'));
        assertEquals('C', p.permute('C'));
        assertEquals('F', p.permute('E'));
        assertEquals('E', p.permute('G'));
        assertEquals('H', p.permute('H'));
        assertEquals('I', p.permute('I'));

    }

    @Test
    public void testInvertChar() {
        Permutation p = new Permutation("(ABD)(C)(EFG)", new Alphabet("ABCDEFGH"));
        assertEquals(3, p.invert(0));
        assertEquals(0, p.invert(1));
        assertEquals(2, p.invert(2));
        assertEquals(1, p.invert(3));
        assertEquals(6, p.invert(4));
        assertEquals(4, p.invert(5));
        assertEquals(5, p.invert(6));
        assertEquals(7, p.invert(7));



    }

    @Test
    public void testDerangement() {
        Permutation p = new Permutation("(ABDC)(EFG)", new Alphabet("ABCDEFG"));
        Permutation g = new Permutation("(ABD)(EFG)", new Alphabet("ABCDEFG"));

        assertTrue(p.derangement());
        assertFalse(g.derangement());
    }

    @Test
    public void testAlphabet() {
        Permutation p = new Permutation("(ABC)", new Alphabet("ABC"));
        //TODO: Fill code here

    }
}
