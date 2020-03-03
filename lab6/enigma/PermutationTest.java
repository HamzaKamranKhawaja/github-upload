package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author
 */
public abstract class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    abstract Permutation getNewPermutation(String cycles, Alphabet alphabet);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet(String chars);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet();

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    @Test
    public void testInvertChar() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals("Expected B",'B', p.invert('A'));
        assertEquals("Expected D",'D', p.invert('B'));
        assertEquals("Expected A",'A', p.invert('C'));
    }

    @Test
    public void testSize() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals("Should be size 4",4, p.size());

        Permutation b = getNewPermutation("(S)", getNewAlphabet("S"));
        assertEquals("Should be size 1",1, b.size());

    }

    @Test
    public void testpermute() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCDE"));
        assertEquals("Permute should point 2",2, p.permute(0));
        assertEquals("Permute should point 0",0, p.permute(1));
        assertEquals("Permute should point 3",3, p.permute(2));
        assertEquals("Permute should point 1",1, p.permute(3));
        assertEquals("Permute should point 4",4, p.permute(4));

        assertEquals("Permute should point 2",'C', p.permute('A'));
        assertEquals("Permute should point 0",'A', p.permute('B'));
        assertEquals("Permute should point 3",'D', p.permute('C'));
        assertEquals("Permute should point 1",'B', p.permute('D'));
        assertEquals("Permute should point 4",'E', p.permute('E'));

        assertEquals("expected size 5",5, p.size());
        assertFalse(p.derangement());

        Permutation self = getNewPermutation("(S)", getNewAlphabet("S"));
        assertEquals("Permute self should point correctly 0->0",0, self.permute(0));

        Permutation multi = getNewPermutation("   (S)     (T) (E)   ", getNewAlphabet("STE"));
        assertEquals("Permute should point S",'S', multi.permute('S'));
        assertEquals("Permute should point T",'T', multi.permute('T'));
        assertEquals("Permute should point E",'E', multi.permute('E'));
    }

    @Test
    public void testinvert() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCDE"));
        assertEquals("Invert incorrect 1",1, p.invert(0));
        assertEquals("Invert incorrect 2",2, p.invert(3));
        assertEquals("Invert incorrect 1",0, p.invert(2));
        assertEquals("Invert incorrect 0",3, p.invert(1));
        assertEquals("Invert incorrect 4",4, p.invert(4));

        assertEquals("Invert incorrect 1",'B', p.invert('A'));
        assertEquals("Invert incorrect 2",'C', p.invert('D'));
        assertEquals("Invert incorrect 1",'A', p.invert('C'));
        assertEquals("Invert incorrect 0",'D', p.invert('B'));
        assertEquals("Invert incorrect 4",'E', p.invert('E'));

        assertEquals("expected size 5",5, p.size());
        assertFalse(p.derangement());

        Permutation self = getNewPermutation("(S)", getNewAlphabet("S"));
        assertEquals("Single invert should point to itself 0<-0", 0, self.invert(0));

        Permutation multi = getNewPermutation("   (S)     (T) (E)   ", getNewAlphabet("STE"));
        assertEquals("invert should point S",'S', multi.invert('S'));
        assertEquals("invert should point T",'T', multi.invert('T'));
        assertEquals("invert should point E",'E', multi.invert('E'));

    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        p.invert('F');
    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabetTwo() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        p.permute('F');
    }


    @Test
    public void testAlphabet() {
        Alphabet test = getNewAlphabet("ABCD");
        Permutation p = getNewPermutation("(BACD)", test);

        assertEquals(test, p.alphabet());

        Alphabet testTwo = getNewAlphabet("ABCDE");
        Permutation b = getNewPermutation("(BACDE)", testTwo);

        assertNotEquals(test, b.alphabet());
    }

    @Test
    public void derangementTest() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertTrue("Should be true since cycles is (BACD)", p.derangement());

        Permutation b = getNewPermutation("(B)", getNewAlphabet("B"));
        assertFalse("Should be false since cycles is (B)",b.derangement());

        Permutation bTwo = getNewPermutation("(D)      (ABC)", getNewAlphabet("ABCD"));
        assertFalse("Should be false since cycles is (D) (ABC)",bTwo.derangement());

        Permutation c = getNewPermutation("", getNewAlphabet("BAC"));
        assertFalse("All should be false",c.derangement());

        Permutation d = getNewPermutation("(A) (B)    (C)", getNewAlphabet("BAC"));
        assertFalse(d.derangement());
    }


}
