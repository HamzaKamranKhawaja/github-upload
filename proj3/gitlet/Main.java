package gitlet;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Hamza Kamran
 */
public class Main {

    /** The Current Working Directory */
    static final File CWD = new File(".");

    /** .gitlet directory. */
    static final File GITLET_DIR = Utils.join(CWD, ".gitlet");
    static final File COMMIT_DIR = Utils.join(GITLET_DIR, "committed");
    static final File STAGING_DIR = Utils.join(GITLET_DIR, "staged");
    static final File STAGING_DIR_REMOVAL = Utils.join(GITLET_DIR, "remove");
    static final File CONTENT_DIR = Utils.join(GITLET_DIR, "content");
    static final File BRANCHES_DIR = Utils.join(GITLET_DIR, "branches");
    static final File ALL_BRANCHES = Utils.join(BRANCHES_DIR, "AllBranches");
    static final File HEAD_BRANCH = Utils.join(BRANCHES_DIR, "HEAD_BRANCH");
    static final File HEAD = Utils.join(COMMIT_DIR, "HEAD");



    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) throws IOException {
        //FIXME: For each command, find incorrect operands checking:
        // If a user inputs a command that doesn't exist, print the message "No command with that name exists." and exit.
        if (args == null) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        if (!args[0].equals("init") && !GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        switch(args[0]) {
            case "init":
                // INIT IS WORKING
                //System.out.println(Utils.readObject(Utils.join(COMMIT_DIR, Utils.readContentsAsString(HEAD)), Commit.class).dateTime.toString());
                validateNumArgs("init", args, 1);
                initialize();
                Commit.init();
                break;
            case "add":
                //add is working
                validateNumArgs("add", args, 2);
                Commands.add(args[1]);
                break;
            case "commit":
                if (args.length == 1 || args[1].equals("")  ) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                validateNumArgs("commit", args, 2);
                if ((!STAGING_DIR.exists() || STAGING_DIR.list() == null ||
                        Objects.requireNonNull(Utils.plainFilenamesIn(STAGING_DIR)).size() == 0)
                                && (!STAGING_DIR_REMOVAL.exists() || STAGING_DIR_REMOVAL.list() == null ||
                        Objects.requireNonNull(Utils.plainFilenamesIn(STAGING_DIR_REMOVAL)).size() == 0)) {
                    System.out.println("No changes added to the commit.");
                    System.exit(0);
                }
                LocalDateTime now = LocalDateTime.now();
                new Commit(getHead(), args[1], now);

                break;
            case "rm":
                validateNumArgs("rm", args, 2);
                Commands.remove(args[1]);
                break;
            case "log":
                validateNumArgs("init", args, 1);
                //Head contains SHA-1 of the head commit
                Commands.log(Utils.readContentsAsString(HEAD));
                break;
            case "global-log":
                validateNumArgs("global-log", args, 1);
                Commands.global_log();;
                break;
            case "find":
                validateNumArgs("find", args, 2);
                //FIXME: Ensure args[1] gives you the correct string e.g "This is the args[1]" is still args[1]?
                Commands.find(args[1]);
                break;
            case "status":
                validateNumArgs("status", args, 1);
                Commands.status();
                break;
            case "checkout":
                if (args.length < 2 || args.length > 4 ) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                } else if (args.length == 3 && args[1].equals("--")) {
                    Commands.checkoutFileDefault(args[2]);
                } else if (args.length == 4 && args[2].equals("--")) {
                    Commands.checkoutCommit(args[3], args[1]);
                }else if (args.length == 2) {
                    Commands.checkoutBranch(args[1]);
                } else {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                break;
            case "branch":
                validateNumArgs("branch", args, 2);
                Branch.branch(args[1]);
                break;
            case "rm-branch":
                validateNumArgs("rm-branch", args, 2);
                Commands.rmbranch(args[1]);
                break;
            case "reset":
                validateNumArgs("reset", args, 2);
                Commands.reset(args[1]);
                break;
            case "merge":
                //call merge()
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }

    public static void initialize() throws GitletException {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        } else if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
        }
        if (!COMMIT_DIR.exists()) {
            COMMIT_DIR.mkdir();
        }
        if (!CONTENT_DIR.exists()) {
            CONTENT_DIR.mkdir();
        }
        if (!STAGING_DIR.exists()) {
            STAGING_DIR.mkdir();
        }
        if (!BRANCHES_DIR.exists()) {
            BRANCHES_DIR.mkdir();
        }
        if (!STAGING_DIR_REMOVAL.exists()) {
            STAGING_DIR_REMOVAL.mkdir();
        }
        if (!ALL_BRANCHES.exists()) {
           ALL_BRANCHES.mkdir();
        }
        if (!HEAD_BRANCH.exists()) {
            try {
            HEAD_BRANCH.createNewFile();
        } catch (IOException e) {
            System.out.println("Could not create HEAD_BRANCH file");
            }
        }
        if (!HEAD.exists()) {
            try {
                HEAD.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Could not create HEAD file");
            }
        }
    }

    /** Returns the SHA-1 contained in the HEAD file */
    public static String getHead() {
       if (!HEAD.exists()) {
           System.out.println("Cannot get HEAD file. Doesnt exist");
           return null;
       }
       else {
           String headSHA = Utils.readContentsAsString(HEAD);
           return headSHA;
       }
    }

    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     *
     * @param cmd Name of command you are validating
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new GitletException( //FIXME: "Invalid operands." ?
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }
}


