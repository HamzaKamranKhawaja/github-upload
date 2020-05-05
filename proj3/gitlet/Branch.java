package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Main.*;

/** A class for all branch related commands.
 * @author hamza  */

public class Branch {


    /** Creates a new branch with the given name, and points it at the
     *  current head node. A branch is nothing more than a name for a
     *  reference (a SHA-1 identifier) to a commit node. This command
     *  does NOT immediately switch to the newly created branch (just
     *  as in real Git). Before you ever call branch, your code should
     *  be running with a default branch called "master".
     *  @param name kdsjkfd*/

    public static void branch(String name) {
        File newbranch = Utils.join(ALL_BRANCHES, name);
        if (newbranch.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        } else {
            try {
                newbranch.createNewFile();
            } catch (IOException e) {
                System.out.println("Cannot create branch with name: " + name);
            }
            String headNodeSHA = Utils.readContentsAsString(HEAD);
            Utils.writeContents(newbranch, headNodeSHA);
        }

    }
}
