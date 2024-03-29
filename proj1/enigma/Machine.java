package enigma;

import java.util.ArrayList;
import java.util.Collection;

/** Class that represents a complete enigma machine.
 *  @author Hamza KAMRAN
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _pawls = pawls;
        _numRotors = numRotors;
        _allRotors = allRotors;
        _plugboard = new Permutation("()", new Alphabet(""));
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        usedrotors = new ArrayList<>();
        if (rotors.length > _allRotors.size()) {
            throw new EnigmaException("Rotors must be selected "
                    + "from available rotors.");
        }
        boolean contains = false;
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor R: _allRotors) {
                if (R.name().equals(rotors[0])) {
                    if (!R.reflecting()) {
                        throw new EnigmaException("First Rotor"
                            + " has to be reflecting. ");
                    }
                }
                if (R.name().contentEquals(rotors[i])) {
                    contains = true;
                    usedrotors.add(R);
                }
            }
            if (!contains) {
                throw new EnigmaException("You cannot "
                        + "add a rotor not in allRotors");
            }
        }
        for (Rotor R: usedrotors) {
            R.set(0);
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("String setting "
                    + "should be of length numRotors() -1");
        }
        for (int i = 0; i < setting.length(); i++) {
            usedrotors.get(i + 1).set(setting.charAt(i));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        String plugboardString = plugboard.alphabet().toString();
        if (plugboardString.length() > _alphabet.size()) {
            throw new EnigmaException("Plugboard cannot "
                   + "contain characters not in alphabet");
        }
        for (int i = 0; i < plugboard.alphabet().size(); i++) {
            if (!_alphabet.contains(plugboard.alphabet().toChar(i))) {
                throw new EnigmaException("Plugboard has "
                        + "characters not in alphabet of machine");
            }
        }
        _plugboard = plugboard;

    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        int transformed = _plugboard.permute(c);
        if (usedrotors.size() == 0) {
            throw new EnigmaException("Cannot convert if no rotors");
        }
        for (int i = numRotors() - numPawls(); i <= numRotors() - 1; i++) {
            if (!usedrotors.get(i).reflecting() && usedrotors.get(i).rotates()
                   && i != usedrotors.size() - 1
                   && usedrotors.get(i + 1).atNotch()) {
                usedrotors.get(i).advance();
            } else if (!usedrotors.get(i).reflecting()
                   && usedrotors.get(i).rotates()
                   && i != usedrotors.size() - 1
                   && usedrotors.get(i).atNotch()
                    && usedrotors.get(i - 1).rotates()) {
                usedrotors.get(i).advance();
            }
        }
        usedrotors.get(usedrotors.size() - 1).advance();

        for (int j = usedrotors.size() - 1; j >= 0; j--) {
            transformed = usedrotors.get(j).convertForward(transformed);
        }
        for (int k = 1; k < usedrotors.size(); k++) {
            transformed = usedrotors.get(k).convertBackward(transformed);
        }
        transformed = _plugboard.permute(transformed);
        return transformed;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String message = msg.replaceAll("\\s", "");
        String converted = "";
        for (int i = 0; i < message.length(); i++) {
            if (!_alphabet.contains(message.charAt(i))) {
                throw new EnigmaException("Message has "
                        + "characters not in alphabet: "
                        + message.charAt(i));
            }
            int changed = convert(_alphabet.toInt(message.charAt(i)));
            converted = converted + _alphabet.toChar(changed);
        }
        return converted;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of pawls I have. */
    private int _pawls;

    /** Total Number of Rotors I have. */
    private int _numRotors;

    /** Collection of all rotors. */
    protected Collection<Rotor> _allRotors;

    /** Collection of rotors I have. */
    protected ArrayList<Rotor> usedrotors;

    /** Plugboard arrangement. */
    protected Permutation _plugboard;
}
