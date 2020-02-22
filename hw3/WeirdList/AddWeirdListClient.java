public class AddWeirdListClient implements IntUnaryFunction {

    private int add;

    AddWeirdListClient(int toAdd) {
        this.add = toAdd;
    }

    @Override
    public int apply(int x) {
        return add + x;
    }
}
