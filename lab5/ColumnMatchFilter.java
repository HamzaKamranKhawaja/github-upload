import java.util.Iterator;
import java.util.List;

/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);

        /* Assigns column indexes to appropriate variables */

        col1 = input.colNameToIndex(colName1);
        col2 = input.colNameToIndex(colName2);
        }


    @Override
    protected boolean keep() {
        /* checks whether elements in next row in appropriate columns are the same */

        return _next.getValue(col1).equals(_next.getValue(col2));

    }

    // FIXME: Add instance variables?
    public Iterator<Table.TableRow> _input;
    private boolean _valid;
    private List<String> _headerList;
    private int col1;
    private int col2;
    private String value1;
    private String value2;
}


