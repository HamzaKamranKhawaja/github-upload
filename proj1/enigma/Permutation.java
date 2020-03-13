package enigma;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Hamza Kamran
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet)
            throws IllegalArgumentException {
        _alphabet = alphabet;
        _Cycles = cycles;
        forwards = new int[_alphabet.size()];
        backwards = new int[_alphabet.size()];

        for (int i = 0; i < forwards.length; i++) {
            forwards[i] = i;
        }
        String cleancycle = cycles.replaceAll("\\s+", "");

        int openCounter = 0; int closeCounter = 0;
        int lastOpen = 0; int lastClose = 0;
        for (int i = 0; i < cleancycle.length(); i++) {
            char current = cleancycle.charAt(i);
            if (current == '(') {
                openCounter = openCounter + 1;
                lastOpen = i;
            } else if (current == ')') {
                closeCounter += 1; lastClose = i;
                int counter = openCounter - closeCounter;
                if (counter != 1 && counter != 0) {
                    throw new
                            IllegalArgumentException("Incorrect Cycles");
                }
            } else if (!alphabet.contains(current)) {
                throw new
                        IllegalArgumentException("Incorrect type in sequence");
            } else {
                int index = alphabet.toInt(current);
                char nextChar = cleancycle.charAt(i + 1);
                char prevChar = cleancycle.charAt(i - 1);
                if (nextChar != ')') {
                    forwards[index] = alphabet.toInt(nextChar);
                }  else {
                    char wrappedChar =
                        cleancycle.charAt(lastOpen + 1);
                    int wrappedCharIndex =
                            alphabet.toInt(wrappedChar);
                    forwards[index] = wrappedCharIndex;
                    backwards[wrappedCharIndex] = index;
                }
            }
        }
        for (int i = 0; i < forwards.length; i++) {
            backwards[forwards[i]] = i;
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _Cycles = _Cycles + cycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int actualindex = wrap(p);
        return forwards[actualindex];
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int actualindex = wrap(c);
        return backwards[actualindex];
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int index = _alphabet.toInt(p);
        int permutedindex = forwards[index];
        char returnedChar = _alphabet.toChar(permutedindex);
        return returnedChar;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int index = _alphabet.toInt(c);
        char inverted = _alphabet.toChar(index);
        return inverted;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < forwards.length; i++) {
            if (forwards[i] == i) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles of this permutation. */
    private String _Cycles;

    /** Forward mapping array. */
    private  int[] forwards;

   /** Backward mapping array. */
    private int[] backwards;
}
