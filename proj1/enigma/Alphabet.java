package enigma;

/**@author Hamza Kamran Khawaja

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet. */

class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
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
        //Removed default lines
        //return 'A' <= ch && ch <= 'Z';
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return this._alphabet.charAt(index);

        //Removed default lines:
        //return (char) ('A' + index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return this._alphabet.indexOf(ch);

        //Removed the default lines:
        //return ch - 'A';
    }
    private String _alphabet;
}
