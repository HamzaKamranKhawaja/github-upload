package enigma;

import java.util.Arrays;

/**@author Hamza Kamran Khawaja

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet. */

class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        if (!unique(chars)) {
            throw new EnigmaException("Alphabet "
                    + "must have all unique characters");
        }
        _alphabet = chars;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _alphabet.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return this._alphabet.indexOf(ch) >= 0;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return this._alphabet.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return this._alphabet.indexOf(ch);
    }
    /**@param str jdlkjf.
    /** Returns true if string is unique characters, else returns false */
    boolean unique(String str) {
        char[] chArray = str.toCharArray();
        Arrays.sort(chArray);
        for (int i = 0; i < chArray.length - 1; i++) {
            if (chArray[i] != chArray[i + 1]) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return _alphabet;
    }

    /** aLPJKHKHDF. */
    private String _alphabet;
}
