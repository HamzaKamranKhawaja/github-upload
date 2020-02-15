package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        /* *Replace this body with the solution. */
        int Index_of_return_array = 0;
            int[] returnArray = new int[A.length + B.length];


            for (int i = 0; i < A.length; i++, Index_of_return_array ++) {
                returnArray[Index_of_return_array] = A[i];
            }
            for (int i = 0; i < B.length; i++, Index_of_return_array ++) {
                returnArray[Index_of_return_array] = B[i];
            }
            return returnArray;

        }



    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int length) {
        /* *Replace this body with the solution. */
        int[] return_array = new int[A.length - length];
        int required_Index = 0;
        Boolean started = false;

        for (int Beginning_Index = 0; Beginning_Index < return_array.length; Beginning_Index++) {
            if (Beginning_Index == start) {
                started = true;
                length --;
            } else if (started && length != 0) {
                length --;
            } else {
                return_array[required_Index] = A[Beginning_Index];
                required_Index ++;
            }
        }
        return return_array;
    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        /* *Replace this body with the solution. */
        int total_Size = 0;

        for (int i = 0; i < A.length; i+= 1) {
            if (i != 0) {
                if (A[i] < A[i - 1]) {
                    total_Size ++;
                }
            }
        }

        int[][] result = new int[total_Size+ 1][];
        int Number_at_Index = 0;

        for (int i = 0; i < A.length;) {
            int size = 0;
            for (int k = i; k < A.length; k++) {
                if (k+1 == A.length) {
                    break;
                } else if (A[k] < A[k + 1]) {
                    size ++;
                } else {
                    break;
                }
            }

            if (size == 0) {
                result[Number_at_Index] = new int[1];
                result[Number_at_Index][0] = A[i];
                Number_at_Index += 1;
                i += 1;
            } else {
                result[Number_at_Index] = new int[size ++];

                for (int k = 0; k <= size; k+= 1, i++) {
                    result[Number_at_Index][k] = A[i];

                }
                Number_at_Index ++;
            }
        }

        return result;
    }

}

