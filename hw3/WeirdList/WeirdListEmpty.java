public class WeirdListEmpty extends WeirdList {


    /**
     * A new WeirdList whose head is HEAD and tail is TAIL.
     *
     * @param Tip int.
     * @param End weird List.
     */
    public WeirdListEmpty(int Tip, WeirdList End) {
        super(Tip, End);
    }

    @Override
    public int length() {
        int toReturn = Length;
        Length = 0;
        return toReturn;
    }
    @Override
    public String toString() {
        return "";
    }
    @Override
    public WeirdList map(IntUnaryFunction func) {
        return this;
    }
}
