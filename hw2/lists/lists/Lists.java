package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

/** List problem.
 *  @author
 */
class Lists {

    /* B. */

    /**
     * Return the list of lists formed by breaking up L into "natural runs":
     * that is, maximal strictly ascending sublists, in the same order as
     * the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     * then result is the four-item list
     * ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     * Destructive: creates no new IntList items, and may modify the
     * original list pointed to by L.
     */




        /* Replace this body with the solution.
        //10 intlists
        //IntList L is the first one
        //disconnect the end of each run
        //save the beginning of each run in a list

        //edge case checks */
    static IntListList naturalRuns(IntList L) {
        IntList common = L;
        if (L == null) {
            return null;
        } else {
            IntList b = common;
            IntList a = L;
            IntListList return_List;

            while (L != null) {
                if (a.tail == null) {
                    a = a.tail;
                    b.tail = null;
                    break;
                } else if (a.head < a.tail.head) {
                    b = b.tail;
                    a = a.tail;

                } else {
                    b.tail = null;
                    a = a.tail;

                    break;
                }
            }

            return_List = new IntListList(L, naturalRuns(a));
            return return_List;
        }
    }
}
/*

        }




        IntList[] saves = new IntList(10);
        int saved = 1;
        IntList start_run = L;
        for (IntList a = start_run; a != null && a.tail != null; a = a.tail) {
            if (a.head < a.tail.head) {
                continue;
            }

            else if (a.head >= a.tail.head) {
                saves[saved] = start_run;
                IntList old = a;
                start_run =
                a = a.tail;
                old.tail = null;
                saved++;
            }
            else if (a.tail.tail == null) {

            }


        }


        if (L == null){ //Error
            return null;
        }

        else if(L.tail == null){  //
            return L;
        }

        else if (L.head >= L.tail.head){
        intList a = L.tail;
        L.tail = a;
        return naturalRuns(L.tail);
        }

        else {
            return naturalRuns(L.tail);
        }
        return listolist;




        IntListList saves = new IntListList(L, null);
        IntListList last = saves;
        if (L == null) {
            return saves;
        }
        for (IntList a = L; a != null && a.tail != null; a = a.tail) {
            //edge case checks
            if (a.head >= a.tail.head) {
                last.tail = new IntListList(a.tail, null);
                last = last.tail;
                a.tail = null;
            }
            if (a.tail.tail == null) {
                if (a.tail.head >= a.tail.tail.head) {
                    last.tail = new IntListList(a.tail.tail, null);
                    a.tail.tail = null;
                }
            }
        }
        return saves;
    }*/

