import java.io.IOException;
import java.io.StringReader;

/** String translation.
 *  @author Hamza Kamran Khawaja
 */
public class Translate {

    static String translate(String words, String from, String to)
            throws IOException {
        /* NOTE: The try {...} catch is a technicality to keep Java happy. */
        char[] reader = new char[words.length()];

        try {
            StringReader Source = new StringReader(words);

            TrReader trans = new TrReader(Source, from, to);
            trans.read(reader);
            words = new String(reader);

            return words;

        } catch (IOException e) {
            return null;
        }
    }
}
