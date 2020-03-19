/** Represents an array of integers each in the range -8..7.
 *  Such integers may be represented in 4 bits (called nybbles).
 *  @author
 */
public class Nybbles {

    /** Maximum positive value of a Nybble. */
    public static final int MAX_VALUE = 7;

    /** Return an array of size N. 
    * DON'T CHANGE THIS.*/
    public Nybbles(int N) {
        _data = new int[(N + 7) / 8];
        _n = N;
    }

    /** Return the size of THIS. */
    public int size() {
        return _n;
    }

    /** Return the Kth integer in THIS array, numbering from 0.
     *  Assumes 0 <= K < N. */
    public int get(int k) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else {
            int index = k / 8;
            int nybble = k % 8;
            int bits = this._data[index];
            int noleft = (bits << (4 * (7 - nybble))) >> (4 * (7 - nybble));
            int noright = (noleft >> (4 * nybble));

            return noright;
        }
    }

    /** Set the Kth integer in THIS array to VAL.  Assumes
     *  0 <= K < N and -8 <= VAL < 8. */
    public void set(int k, int val) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else if (val < (-MAX_VALUE - 1) || val > MAX_VALUE) {
            throw new IllegalArgumentException();
        } else {
            int index = k / 8;
            int nybble = k % 8;
            int bits = this._data[index];
            int value = this.get(k);
            int shifted = value << (nybble * 4);
            int reduced = bits - shifted;
            int replacement = val << (nybble * 4);
            int set = reduced | replacement;
            _data[index] = set;
        }
    }

    /** DON'T CHANGE OR ADD TO THESE.*/
    /** Size of current array (in nybbles). */
    public int _n;
    /** The array data, packed 8 nybbles to an int. */
    public int[] _data;
}
