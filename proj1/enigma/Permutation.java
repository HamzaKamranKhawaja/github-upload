package enigma;
import java.util.Arrays;
import java.lang.Math.*;

import net.sf.saxon.functions.IndexOf;

import java.lang.reflect.Array;

import static enigma.EnigmaException.*;

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
    Permutation(String cycles, Alphabet alphabet) throws IllegalArgumentException{
        _alphabet = alphabet;
        _Cycles = cycles;
        Forward = new int[_alphabet.size()];
        Backward = new int[_alphabet.size()];

        Arrays.fill(Forward, -99);
        Arrays.fill(Backward, -99);

        String CleanCycle = cycles.replaceAll("\\s+","" );

        int openCounter = 0; int closeCounter = 0;
        int lastOpen = 0; int lastClose = 0;

        for (int i = 0; i < CleanCycle.length(); i++){
            char current = CleanCycle.charAt(i);
            if(current == '('){
                openCounter += 1;
                lastOpen = i;
            }
            else if(current == ')'){
                closeCounter += 1;
                lastClose = i;

                if (Math.abs(openCounter - closeCounter) > 1){
                    throw new IllegalArgumentException("Incorrect Sequence of mapping");
                }
            }
            //TODO: Find if you need these lines
          else if (!alphabet.contains(current)) {
                throw new IllegalArgumentException("Incorrect type in sequence");
            }
            else {
                int index = alphabet.toInt(current);
                char nextChar = CleanCycle.charAt(i + 1);

                char prevChar = CleanCycle.charAt(i - 1);

                if (nextChar != ')') {
                    Forward[index] = alphabet.toInt(nextChar);
                }
                else{
                    char wrappedChar = CleanCycle.charAt(lastOpen + 1);
                    int wrappedCharIndex = alphabet.toInt(wrappedChar);
                    Forward[index] = wrappedCharIndex;
                    //alphabet.toChar(CleanCycle.charAt(lastOpen + 1));
                    Backward[wrappedCharIndex] = index;


                    //if(prevChar != '('){
                      //  Backward[index] = alphabet.toInt(prevChar);
                    //}

                }
            }
        }
        for(int i = 0; i < Forward.length; i++) {
            if (Forward[i] == -99) {
                Forward[i] = i;
            }
        }
          for(int i = 0; i < Forward.length; i++){
               Backward[Forward[i]] = i;
        }
        }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _Cycles = _Cycles + cycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        System.out.println(Forward.length);
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return Forward.length;
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int actualIndex = wrap(p);
        return Forward[actualIndex];
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int actualIndex = wrap(c);
        return Backward[actualIndex];
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int Index = _alphabet.toInt(p);
        int PermutedIndex = Forward[Index];
        char returnedChar = _alphabet.toChar(PermutedIndex);
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
        for(int i = 0; i < Forward.length; i++){
            if (Forward[i] == i){
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
   private  int[] Forward;

   /** Backward mapping array. */
   private int[] Backward;
}
