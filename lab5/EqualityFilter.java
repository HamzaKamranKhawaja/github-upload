import com.sun.tools.doclets.internal.toolkit.PropertyWriter;

/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        // FIXME: Add your code here.
        col1 = input.colNameToIndex(colName);
        value = match;

    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        return _next.getValue(col1).equals(value);
        }

    // FIXME: Add instance variables?
    private int col1;
    private String value;
}
