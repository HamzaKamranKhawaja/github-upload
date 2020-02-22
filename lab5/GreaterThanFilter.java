/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        value = ref;
        col1 = input.colNameToIndex(colName);
    }

    @Override
    protected boolean keep() {
        return _next.getValue(col1).compareTo(value) > 0;
    }

    private int col1;
    private String value;
}
