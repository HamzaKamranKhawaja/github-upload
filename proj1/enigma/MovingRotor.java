package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Hamza Kamran
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        for (int i = 0; i < notches.length(); i++) {
            if (!alphabet().contains(notches.charAt(i))) {
                throw new EnigmaException("Notch does not exist in alphabet");
            }
        }
        _notches = notches;
    }
    @Override
    void advance() {

        set(_setting + 1);
    }

    @Override
    boolean atNotch() {
        boolean atnotch = false;
        for (int i = 0; i < _notches.length(); i++) {
            if (_notches.charAt(i) == alphabet().toChar(setting())) {
                atnotch = true;
            }
        }
        return atnotch;
    }

    @Override
    boolean rotates() {
        return true;
    }

    /** contains notches. */
    private String _notches;
}
