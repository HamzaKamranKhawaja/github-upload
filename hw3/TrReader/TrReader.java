import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Hamza Kamran Khawaja
 */

/**Will read from a SOURCE file from OFFSET and output to TO */

public class TrReader extends Reader {

    private Reader Source;
    private String Origin;
    private String To;

    public TrReader(Reader str, String from, String to) {
        Source = str;
        Origin = from;
        To = to;
    }

    /** Reads LENGTH characters into BUFFER starting
     * at index OFFSET. will return LENGTH */
    public int read(char[] Buffer, int Offset, int Length) throws IOException {

        int charsToRead = Source.read(Buffer, Offset, Length);

        for (int x = Offset; x < Offset + charsToRead; x += 1) {
            for (int y = 0; y < Origin.length(); y += 1) {
                if (Buffer[x] == Origin.charAt(y)) {
                    Buffer[x] = To.charAt(y);
                    break;
                }
            }
        }
        return charsToRead;
    }

    @Override
    public void close() {

    }

}
