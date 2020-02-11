
/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Vivant Sakore (1/29/2020)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {

            DNode copy = this._front;
            int size = 0;

            while(copy != null) {
                size+= 1;
                copy = copy._next;
            }

            return size;
        }


        /**
         * @param i index of element to return,
         *          where i = 0 returns the first element,
         *          i = 1 returns the second element,
         *          i = -1 returns the last element,
         *          i = -2 returns the second to last element, and so on.
         *          You can assume i will always be a valid index, i.e 0 <= i < size for positive indices
         *          and -size <= i <= -1 for negative indices.
         * @return The integer value at index i
         */
    public int get(int i) {
        // FIXME: Implement this method and return correct value

        if (i < 0) {
            DNode copy = this._back;

            while (i != -1){
                copy = copy._prev;
                i += 1;
            }
            return copy._val;
        } else {
            DNode copy = this._front;

            while (i != 0){
                copy = copy._next;
                i -= 1;
            }
            return copy._val;
        }
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {

        if (this._front == null) {
            this._front = new DNode(null, d, null);
            this._back = this._front;
        } else {
            DNode newElement = new DNode(null, d, this._front);
            this._front._prev = newElement;
            this._front = newElement;
        }
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {

        if (this._back == null) {
            this._back = new DNode(null, d, null);
            this._front = this._back;
        } else {
            DNode newElement = new DNode(this._back, d,null );
            this._back._next = newElement;
            this._back = newElement;
        }
    }


    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position,
     *              index = -1 inserts at the back,
     *              index = -2 inserts at the second to last position, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size for positive indices (including insertions at front and back)
     *              and -(size+1) <= index <= -1 for negative indices (including insertions at front and back).
     */
    public void insertAtIndex(int d, int index) {
        // FIXME: Implement this method
        DNode newElement = new DNode(null, d, null);
        DNode copyFront = this._front;
        DNode copyBack = this._back;

        if (this._front == null || this._back == null || index == 0 || (index < 0 & size() < Math.abs(index))) {
            insertFront(d);
        } else if (index == size() || index == -1) {
            insertBack(d);
        } else {
            if (index > 0) {

                for (int i = 0; i < index; i+=1) {
                    copyFront = copyFront._next;
                }
                newElement._next = copyFront;
                newElement._prev = copyFront._prev;
                copyFront._prev._next = newElement;
                copyFront._prev = newElement;

            } else {

                for (int i = index; i < -2; i+= 1) {
                    copyBack = copyBack._prev;
                }

                newElement._next = copyBack;
                newElement._prev = copyBack._prev;
                copyBack._prev._next = newElement;
                copyBack._prev = newElement;

            }
        }
    }




    /**
     * Removes the first item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {

        int value = this._front._val;

        if (this._front._next == null) {
        this._front = null;
        this._back = null;
        } else {

        this._front = this._front._next;
        this._front._prev = null;
        }

        return value;
        }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        int value = this._back._val;

        if (this._back._prev == null) {

        this._back = null;
        this._front = null;

        } else {

        this._back = this._back._prev;
        this._back._next  = null;
        }

        return value;
        }


/**
 * @param index index of element to be deleted,
 *          where index = 0 returns the first element,
 *          index = 1 will delete the second element,
 *          index = -1 will delete the last element,
 *          index = -2 will delete the second to last element, and so on.
 *          You can assume index will always be a valid index,
 *              i.e 0 <= index < size for positive indices (including deletions at front and back)
 *              and -size <= index <= -1 for negative indices (including deletions at front and back).
 * @return the item that was deleted
 */
    public int deleteAtIndex(int index) {
        // FIXME: Implement this method and return correct value
        return 0;
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        // FIXME: Implement this method to return correct value
            if (size() == 0) {
                return "[]";
            }
            String str = "[";
            DNode curr = _front;
            for (; curr._next != null; curr = curr._next) {
                str += curr._val + ", ";
            }
            str += curr._val +"]";
            return str;
        }


    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
