package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Hamza Kamran Khawaja
 */
public final class Main {

    /**
     * Process a sequence of encryptions and decryptions, as
     * specified by ARGS, where 1 <= ARGS.length <= 3.
     * ARGS[0] is the name of a configuration file.
     * ARGS[1] is optional; when present, it names an input file
     * containing messages.  Otherwise, input comes from the standard
     * input.  ARGS[2] is optional; when present, it names an output
     * file for processed messages.  Otherwise, output goes to the
     * standard output. Exits normally if there are no errors in the input;
     * otherwise with code 1.
     */
    public static void main(String... args) {
        try {
            new Main(args).process();
            System.exit(0);
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /**
     * Check ARGS and open the necessary files (see comment on main).
     */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }
        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }
        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /**
     * Return a Scanner reading from the file named NAME.
     */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Return a PrintStream writing to the file named NAME.
     */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Configure an Enigma machine from the contents of configuration
     * file _config and apply it to the messages in _input, sending the
     * results to _output.
     */
    private void process() {
        Machine machine = readConfig();
        if (!_input.hasNext("\\*")) {
            throw new EnigmaException("Settings file does not begin with *");
        }
        _input.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
        String settings = _input.nextLine().trim();
        setUp(machine, settings);
        while (_input.hasNext()) {
            if (_input.hasNext("\\*")) {
                _input.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
                settings = _input.nextLine();
                setUp(machine, settings);
            }
            _input.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
            String converted = machine.convert(_input.nextLine().trim());
            printMessageLine(converted);
        }
        _input.close();
        _config.close();
        _output.close();
    }
    /**
     * Return an Enigma machine configured from the contents of configuration
     * file _config.
     */
    private Machine readConfig() {
        try {
            String uncleanAlpha = _config.next();
            String cleanAlpha = uncleanAlpha.replaceAll("[*()]", "");
            _alphabet = new Alphabet(cleanAlpha);
            _config.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
            if (_config.hasNextInt()) {
                rotorslots = _config.nextInt();
            } else {
                throw new EnigmaException("configuration file should contain"
                        + " total number of rotors");
            }
            _config.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
            if (_config.hasNextInt()) {
                _pawls = _config.nextInt();
            } else {
                throw new EnigmaException("configuration file should"
                        + " contain total number of pawls");
            }
            _config.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
            ArrayList<Rotor> allrotors = new ArrayList<Rotor>();
            while (_config.hasNext()) {
                allrotors.add(readRotor());
            }
            return new Machine(_alphabet, rotorslots, _pawls, allrotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }
    /**
     * Return a rotor, reading its description from _config.
     */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String type = _config.next();
            String sequence = "";
            while (_config.hasNext(("\\((.+)\\) *"))) {
                sequence = sequence + _config.next("\\((.+)\\) *");
            }
            Pattern p = Pattern.compile("(.+ .+)");
            Matcher m = p.matcher(sequence);

            if (m.find()) {
                throw new EnigmaException("Cycles "
                        + "cannot contain space within parenthesis");
            }
            String cleansequence = sequence.replaceAll("\\s", "");
            String bestsequence = cleansequence.replaceAll("\\n", "");


            if (type.charAt(0) == 'M') {
                String notches = type.subSequence(1,
                        type.length() - 1).toString();
                return new MovingRotor(name,
                        new Permutation(bestsequence, _alphabet), notches);
            } else if (type.length() > 1) {
                throw new EnigmaException("Incorrect setting of rotor;"
                        + " type should be followed with space");
            } else if (type.charAt(0) == 'N') {
                return new FixedRotor(name,
                        new Permutation(bestsequence, _alphabet));
            } else if (type.charAt(0) == 'R') {
                return new Reflector(name, new Permutation(bestsequence,
                        _alphabet));
            } else {
                throw new EnigmaException("Wrong format of file");
            }

        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        settings = settings.trim();
        if (settings.charAt(0) != '*') {
            throw new EnigmaException("File must start with *");
        }
        String plugboard = "";
        boolean set = false;
        int firstseen = 0;
        String[] arrangement = settings.split("\\s+");
        for (int i = 1; i < arrangement.length; i++) {
            if (arrangement[i].charAt(0) == '(') {
                plugboard = plugboard + arrangement[i];
                if (!set) {
                    set = true;
                    firstseen = i;
                }
            }
        }

        int numOfPlugboards = 0;

        if (firstseen != 0) {
            numOfPlugboards = arrangement.length - firstseen;
        }
        String[] rotors = new String[arrangement.length - numOfPlugboards - 2];
        int numofrotors = rotors.length;
        System.arraycopy(arrangement, 1, rotors, 0, rotors.length);
        String rotorSettings = arrangement[numofrotors + 1];
        String setrotors = "";
        convertStringArrayToString(rotors, " ");
        M.insertRotors(rotors);
        M.setRotors(rotorSettings);

        if (plugboard.equals("")) {
            M.setPlugboard(new Permutation("", _alphabet));
        } else {
            M.setPlugboard(new Permutation(plugboard, _alphabet));
        }
    }
    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i++) {
            if (i != 0 && i % 5 == 0) {
                _output.print(" ");
            }
            _output.print(msg.charAt(i));
        }
        _output.println();
    }

    /** converts array to string.
     * @param delimiter fdioughohj
     * @param strArr khigbiugygbh
     * @return lkjfdji*/
    private static String convertStringArrayToString(String[] strArr,
                                                      String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String str : strArr) {
            sb.append(str).append(delimiter);
        }
        return sb.substring(0, sb.length() - 1);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** How many rotor slots for the machine. */
    private int rotorslots;

    /** How many pawls do I have. */
    private int _pawls;
}

