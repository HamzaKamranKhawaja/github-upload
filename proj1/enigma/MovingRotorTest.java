package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.HashMap;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class MovingRotorTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    /** Check that rotor has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkRotor(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, rotor.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c),    ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d (%c)", ci, c),
                         ei, rotor.convertForward(ci));
            assertEquals(msg(testId, "wrong inverse of %d (%c)", ei, e),
                         ci, rotor.convertBackward(ei));
        }
    }

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setRotor(String name, HashMap<String, String> rotors,
                          String notches) {
        rotor = new MovingRotor(name, new Permutation(rotors.get(name), UPPER),
                                notches);
    }

    /* ***** TESTS ***** */

    @Test
    public void checkRotorAtA() {
        setRotor("I", NAVALA, "");
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));

        setRotor("II", NAVALA, "");
        checkRotor("Rotor II (B)", UPPER_STRING, NAVALA_MAP.get("II"));


        setRotor("III", NAVALA, "");
        checkRotor("Rotor III (A)", UPPER_STRING, NAVALA_MAP.get("III"));

        setRotor("III", NAVALZ, "");
        checkRotor("Rotor III (A)", UPPER_STRING, NAVALZ_MAP.get("III"));

    }

    @Test
    public void checkRotorAdvance() {
        setRotor("I", NAVALA, "");
        rotor.advance();
        System.out.println(rotor.name());
        System.out.println();

        checkRotor("Rotor I advanced", UPPER_STRING, NAVALB_MAP.get("I"));

        setRotor("IV", NAVALA, "");
        rotor.advance();
        rotor.advance();
        System.out.println(rotor._setting);
        assertEquals(2, rotor._setting);

        for (int i = 2; i < 26; i++) {
            System.out.println(rotor._setting);
            rotor.advance();
        }
        System.out.println(rotor._setting);
        assertEquals(0, rotor._setting);
    }

    @Test
    public void checkRotorSet() {
        setRotor("I", NAVALA, "");
        rotor.set(25);
        checkRotor("Rotor I set", UPPER_STRING, NAVALZ_MAP.get("I"));
        assertEquals(25, rotor._setting);
        rotor.advance();
        assertEquals(0, rotor._setting);
    }

    @Test (expected = EnigmaException.class)
    public void checkNotchandadvance() {
        setRotor("I", NAVALA, "BC");
        rotor.advance();
        assertTrue(rotor.atNotch());

        setRotor("I", NAVALA, "RST");
        for (int i = 0; i < 19; i++) {
            rotor.advance();
        }
        assertTrue(rotor.atNotch());


        setRotor("III", NAVALZ, "ABC");
        checkRotor("Rotor III (A)", UPPER_STRING, NAVALZ_MAP.get("III"));
        rotor.set(25);
        assertFalse(rotor.atNotch());
        rotor.advance();
        assertTrue(rotor.atNotch());
        rotor.advance();
        assertTrue(rotor.atNotch());
        assertTrue(rotor.atNotch());
        rotor.advance();
        rotor.advance();
        assertFalse(rotor.atNotch());
        setRotor("III", NAVALZ, "1");
    }

}
