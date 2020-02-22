public class TotalWeirdListClient implements IntUnaryFunction {

    private int Total;

    public TotalWeirdListClient(int val) {
        this.Total = val;
    }
    @Override
    public int apply(int x) {
        Total += x;
        return x;
    }
    public int getSum() {
        return Total;
    }

}
