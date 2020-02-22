/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        col1 = input.colNameToIndex(colName);
        value = subStr;
    }

    @Override
    protected boolean keep() {
        return _next.getValue(col1).contains(value);
    }

    private int col1;
    private String value;
}
