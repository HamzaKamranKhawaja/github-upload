package enigma;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Collection;
import java.util.HashMap;
import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author Hamza Kamran
 */
public class MachineTest {

    @Test
    public void test1() {
        ArrayList<Rotor> rotor = new ArrayList<Rotor>(4);
        Rotor r1 = new Reflector("I", new Permutation("(ABZDBEFGHI)", new Alphabet()));
        rotor.add(r1);
        Rotor r2 = new FixedRotor("II", new Permutation("(THROWZA)", new Alphabet()));
        rotor.add(r2);
        Rotor r3 = new MovingRotor("III", new Permutation("(SHAVE)", new Alphabet()), "");
        rotor.add(r3);
        Rotor r4 = new MovingRotor("IV", new Permutation("(ABCDE)", new Alphabet()),  "ABCD");
        rotor.add(r4);
        System.out.println("r4 setting is: " + r4._setting);
        System.out.println(r4.atNotch());
        Machine automata = new Machine(new Alphabet(), 4, 2, rotor);
        automata.insertRotors(new String[]{"I", "II", "III", "IV"});
        Alphabet newww = new Alphabet();
        System.out.println(newww.size());

        System.out.println(automata.convert("AZXCVBNM"));
        System.out.println("r4 setting is : " + r4._setting);
        System.out.println("r3 setting is : " + r3._setting);

        System.out.println(r4.atNotch());


    }
}