package gitlet;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static gitlet.Main.*;

/**  Adds a copy of the file as it currently exists to the staging area
 *   see the description of the commit command). For this reason, adding
 *   a file is also called staging the file for addition. Staging an
 *   already-staged file overwrites the previous entry in the staging
 *   area with the.
 *   @author hamza */
public class Commands {


    /**  Adds a copy of the file as it currently exists to the staging area
     *   see the description of the commit command). For this reason, adding
     *   a file is also called staging the file for addition. Staging an
     *   already-staged file overwrites the previous entry in the staging
     *   area with the new contents. The staging area should be somewhere in
     *   .gitlet. If the current working version of the file is identical to
     *   the version in the current commit, do not stage it to be added, and
     *   remove it from the staging area if it is already there (as can happen
     *   when a file is changed, added, and then changed back). The file will
     *   no longer be staged for removal (see gitlet rm), if it was at the time
     *   of the command.
     * @param filename kjdshfkjhdfs
     *   */

    public static void add(String filename) throws IOException {
        File addedFile = Utils.join(CWD, filename);
        if (!addedFile.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        File staged = Utils.join(STAGING_DIR, filename);
        File removeFile = Utils.join(STAGING_DIR_REMOVAL, filename);
        String commitSHA = Utils.readContentsAsString(HEAD);
        Commit lastCommit = Utils.readObject(Utils.join(COMMIT_DIR,
                commitSHA), Commit.class);
        if (removeFile.exists()) {
            removeFile.delete();
        }
        if (lastCommit.mapping != null
                && lastCommit.mapping.containsKey(filename)) {
            String fileSHA = lastCommit.mapping.get(filename);
            String contents = Utils.readContentsAsString(Utils.join(
                    CONTENT_DIR, fileSHA));
            String cwdcontents = Utils.readContentsAsString(addedFile);
            if (cwdcontents.equals(contents)) {
                if (staged.exists()) {
                    staged.delete();
                }
                if (!removeFile.exists()) {
                    removeFile.delete();
                }
            } else {
                Utils.writeContents(staged, cwdcontents);
            }
        } else {
            String contentsAsString = Utils.readContentsAsString(addedFile);
            Utils.writeContents(staged, contentsAsString);
        }
    }

    /**
     * Unstage the file if it is currently staged for addition.
     * If the file is tracked in the current commit, stage it for
     * removal and remove the file from the working directory if
     * the user has not already done so (do not remove it unless
     * it is tracked in the current commit).If the file is neither
     * staged nor tracked by the head commit, print the error message
     * "No reason to remove the file.".
     * @param filename dfkjhkdfjsh */

    public static void remove(String filename) throws IOException {
        File file = Utils.join(STAGING_DIR, filename);
        String commitID = Utils.readContentsAsString(HEAD);
        Commit lastCommit = Utils.readObject(Utils.join(
                COMMIT_DIR, commitID), Commit.class);
        if (!file.exists() && lastCommit.mapping != null
                && !lastCommit.mapping.containsKey(filename)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }

        if (file.exists()) {
            file.delete();
        }
        if (lastCommit.mapping != null
                && lastCommit.mapping.containsKey(filename)) {
            Utils.join(STAGING_DIR_REMOVAL, filename).createNewFile();
            Utils.restrictedDelete(Utils.join(CWD, filename));
        }
    }

    /** Does log.
     * @param lastCommit hjdfk */

    public static void log(String lastCommit) {

        File commitFile = Utils.join(COMMIT_DIR, lastCommit);
        Commit thisCommit = Utils.readObject(commitFile, Commit.class);
        String parentsha = thisCommit.parent;
        if (parentsha == null) {
            System.out.println("===");
            System.out.println("commit " + lastCommit);
            String dateStr = thisCommit.dateTime.format(
                    DateTimeFormatter.ofPattern("EE MMM dd HH:mm:ss yyyy"));
            System.out.println("Date: " + dateStr + " -0800");
            System.out.println(thisCommit.message);
        } else {
            System.out.println("===");
            System.out.println("commit " + lastCommit);
            String dateStr = thisCommit.dateTime.format(
                    DateTimeFormatter.ofPattern("EE MMM dd HH:mm:ss yyyy"));
            System.out.println("Date: " + dateStr + " -0800");
            System.out.println(thisCommit.message);
            System.out.println();
            log(parentsha);
        }
    }

    /** used for glonbakjdshfkjdfs.
     */
    public static void globalLog() {

        if (COMMIT_DIR.exists() && COMMIT_DIR.listFiles() != null) {
            for (String commitFile : Objects.requireNonNull(
                    Utils.plainFilenamesIn(COMMIT_DIR),
                    "Commit dir cannot be null")) {
                if (!commitFile.equals("HEAD")) {
                    File filewithcommit = Utils.join(COMMIT_DIR, commitFile);
                    Commit thisCommit = Utils.readObject(filewithcommit,
                            Commit.class);
                    String parentsha = thisCommit.parent;
                    if (parentsha == null) {
                        System.out.println("===");
                        System.out.println("commit " + commitFile);
                        String dateStr = thisCommit.dateTime.format(
                                DateTimeFormatter.ofPattern(
                                        "EE MMM dd HH:mm:ss yyyy"));
                        System.out.println("Date: " + dateStr + " -0800");
                        System.out.println(thisCommit.message);
                        System.out.println();
                    } else {
                        System.out.println("===");
                        System.out.println("commit " + commitFile);
                        String dateStr = thisCommit.dateTime.format(
                                DateTimeFormatter.ofPattern(
                                        "EE MMM dd HH:mm:ss yyyy"));
                        System.out.println("Date: " + dateStr + " -0800");
                        System.out.println(thisCommit.message);
                        System.out.println();
                    }
                }
            }
        }
    }

    /** Prints out the ids of all commits that have the given commit message,
     *  one per line.
     *  If there are multiple such commits, it prints the ids out on separate
     *  lines.
     *  The commit message is a single operand. For multilword messages,
     *  quotation used.
     *  @param message kjbjhb */

    public static void find(String message) {
        if (COMMIT_DIR.exists() && COMMIT_DIR.listFiles() != null) {
            boolean commitExists = false;
            for (String commitFile : Objects.requireNonNull(
                    Utils.plainFilenamesIn(COMMIT_DIR),
                    "Commit dir cannot be null")) {
                if (!commitFile.equals("HEAD")) {
                    File filewithcommit = Utils.join(COMMIT_DIR, commitFile);
                    Commit thisCommit = Utils.readObject(filewithcommit,
                            Commit.class);
                    if (thisCommit.message.equals(message)) {
                        System.out.println(commitFile);
                        commitExists = true;
                    }
                }
            }
            if (!commitExists) {
                System.out.println("Found no commit with that message.");
            }
        }
    }

    /** Displays what branches currently exist, and marks the
     *  current branch with a *.
     *  Also displays what files have been staged for addition or removal.
     *  An example of the exact format it should follow is as follows.

     === Branches ===
     *master
     other-branch

     === Staged Files ===
     wugTest.txt
     wug2.txt

     === Removed Files ===
     goodbye.txt

     === Modifications Not Staged For Commit ===
     junk.txt (deleted)
     wug3.txt (modified)

     === Untracked Files ===
     random.stuff
     .*/
    public static void status() {
        String headbranch = Utils.readContentsAsString(HEAD_BRANCH);
        System.out.println("=== Branches ===");
        if (ALL_BRANCHES.exists() && ALL_BRANCHES.list() != null
            && Objects.requireNonNull(ALL_BRANCHES.list()).length != 0) {
            for (String branchname: Objects.requireNonNull(
                    ALL_BRANCHES.list())) {
                if (headbranch.equals(branchname)) {
                    System.out.println("*" + branchname);
                } else {
                    System.out.println(branchname);
                }
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        if (STAGING_DIR.exists() && STAGING_DIR.list() != null
            && Objects.requireNonNull(STAGING_DIR.list()).length != 0) {
            for (String filename: Objects.requireNonNull(STAGING_DIR.list())) {
                System.out.println(filename);
            }
        }
        System.out.println();
        System.out.println("=== Removed Files ===");

        if (STAGING_DIR_REMOVAL.exists() && STAGING_DIR_REMOVAL.list() != null
                && Objects.requireNonNull(
                        STAGING_DIR_REMOVAL.list()).length != 0) {
            for (String filename: Objects.requireNonNull(
                    STAGING_DIR_REMOVAL.list())) {
                System.out.println(filename);
            }
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    /** java gitlet.Main checkout -- [file name]
     *  java gitlet.Main checkout [commit id] -- [file name]
     *  java gitlet.Main checkout [branch name]
     *  1- Takes the version of the file as it exists in the head commit,
     *  the front of the current branch,
     *  and puts it in the working directory, overwriting the version of
     *  the file that's already there if
     *  there is one. The new version of the file is not staged.
     *  @param filename  djhdkjfsh
     *  @param commitid kjhdfskjh */

    public static void checkoutFile(String filename, String commitid)
            throws IOException {
        File commitfile = Utils.join(COMMIT_DIR, commitid);
        if (!commitfile.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit headCommit = Utils.readObject(commitfile, Commit.class);
        if (headCommit.mapping == null
                || !headCommit.mapping.containsKey(filename)) {
            System.out.println("File does not exist in the commit.");
            System.exit(0);
        } else {
            File cwdfile = Utils.join(CWD, filename);
            String filesha = headCommit.mapping.get(filename);
            String filecontents = Utils.readContentsAsString(Utils.join(
                    CONTENT_DIR, filesha));
            if (cwdfile.exists()) {
                Utils.writeContents(cwdfile, filecontents);
            } else if (!cwdfile.exists()) {
                File newcwdfile =  Utils.join(CWD, filename);
                newcwdfile.createNewFile();
                Utils.writeContents(newcwdfile, filecontents);
            }
        }
    }
    /** For Default, we simply pass in HeadCommit in CHECKOUTFILE.
     * @param filename dhjkdsfhkjds */
    public static void checkoutFileDefault(String filename)
            throws IOException {
        checkoutFile(filename, getHead());
    }
     /** 2- Takes the version of the file as it exists in
      *  the commit with the given id, and puts it in the
     * working directory, overwriting the version of the
      * file that's already there if there is one. The
     * new version of the file is not staged.
      * @param commitid dfjkjdf
      * @param filename jdkjfdk*/

    public static void checkoutCommit(String filename,
                                      String commitid) throws IOException {
        int counter = 0;
        String id = "";
        for (String commitName: Objects.requireNonNull(
                Utils.plainFilenamesIn(COMMIT_DIR),
                "COMMIT DIRECTORY IS NULL.")) {
            if (commitName.startsWith(commitid)) {
                counter = counter + 1;
                id = commitName;
            }
        }
        if (counter == 0) {
            System.out.println(" No commit with that id exists.");
            System.exit(0);
        } else if (counter > 1) {
            System.out.println("More than 1 commits with the given name.");
            System.exit(0);
        } else {
            checkoutFile(filename, id);
        }
    }

    /** 3- Takes all files in the commit at the head of the given
     *  branch, and puts them in the working directory,
    * overwriting the versions of the files that are already there
     * if they exist. Also, at the end of this command,
    * the given branch will now be considered the current branch
     * (HEAD). Any files that are tracked in the current
    * but are not present in the checked-out branch are deleted.
     * The staging area is cleared, unless the checked-out
    * branch is the current branch (see Failure cases).
     * @param checkoutbranch dhkjhdf */
    public static void checkoutBranch(String checkoutbranch)
            throws IOException {

        String currentcommitID = Utils.readContentsAsString(HEAD);
        checkdir(checkoutbranch);
        String checkoutcommitID = Utils.readContentsAsString(
                Utils.join(ALL_BRANCHES, checkoutbranch));
        Commit checkoutcommit = Utils.readObject(Utils.join(
                COMMIT_DIR, checkoutcommitID), Commit.class);
        Commit currentcommit = Utils.readObject(Utils.join(
                COMMIT_DIR, currentcommitID), Commit.class);
        if (checkoutcommit.mapping == null) {
            checker(checkoutcommit, currentcommit);

        } else {
            for (String checkoutfile : checkoutcommit.
                    mapping.keySet()) {
                for (String fileinCWD : Objects.requireNonNull(
                        Utils.plainFilenamesIn(CWD))) {
                    if (fileinCWD.equals(checkoutfile)) {
                        if (currentcommit.mapping != null
                                && !currentcommit.mapping.containsKey(
                                        checkoutfile)) {
                            System.out.println(" There is an untracked file in"
                                    + " the way; delete it, or add and"
                                    + " commit it first.");
                            System.exit(0);
                        }
                    }
                }
                String fileID = checkoutcommit.mapping.get(checkoutfile);
                String contents = Utils.readContentsAsString(
                        Utils.join(CONTENT_DIR, fileID));
                Utils.writeContents(Utils.join(CWD, checkoutfile), contents);
            }
            if (currentcommit.mapping != null) {
                for (String currentfile : currentcommit.mapping.keySet()) {
                    if (!checkoutcommit.mapping.containsKey(currentfile)) {
                        File todelete = Utils.join(CWD, currentfile);
                        if (todelete.exists()) {
                            Utils.restrictedDelete(todelete);
                        }
                    }
                }
            }
            for (String checkoutfile : checkoutcommit.mapping.keySet()) {
                checkoutCommit(checkoutfile, checkoutcommitID);
            }
            for (String currentfile : currentcommit.mapping.keySet()) {
                if (!checkoutcommit.mapping.containsKey(currentfile)) {
                    File todelete = Utils.join(CWD, currentfile);
                    if (todelete.exists()) {
                        Utils.restrictedDelete(todelete);
                    }
                }
            }
        }
        deletedir(checkoutbranch);
    }

    /** Checks the commits against one another.
     * @param checkoutcommit  djkdfjs
     * @param currentcommit  dkjfdskjfds
     */
    public static void checker(Commit checkoutcommit, Commit currentcommit) {
        if (currentcommit.mapping != null) {
            for (String currentfile : currentcommit.mapping.
                    keySet()) {
                File todelete = Utils.join(CWD, currentfile);
                if (todelete.exists()) {
                    Utils.restrictedDelete(todelete);
                }
            }
        }
    }
    /** Prevents wrong values in the directory.
     * @param checkoutbranch dkjkdfsj
     */
    public static void checkdir(String checkoutbranch) {
        String currentBranch = Utils.readContentsAsString(HEAD_BRANCH);
        if (checkoutbranch.equals(currentBranch)) {
            System.out.println("No need to checkout the current branch");
            System.exit(0);
        }
        if (!Objects.requireNonNull(Utils.plainFilenamesIn(
                ALL_BRANCHES)).contains(checkoutbranch)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
    }

    /** Deletes directories.
     * @param checkoutbranch jdkjdfk */
    public static void deletedir(String checkoutbranch) {
        List<String> files = Utils.plainFilenamesIn(STAGING_DIR);
        if (files != null && files.size() != 0) {
            for (String filename : files) {
                Utils.join(STAGING_DIR, filename).delete();
            }
        }
        List<String> filesforremoval = Utils.plainFilenamesIn(
                STAGING_DIR_REMOVAL);
        if (filesforremoval != null && filesforremoval.size() != 0) {
            for (String filename : filesforremoval) {
                Utils.join(STAGING_DIR_REMOVAL, filename).delete();
            }
        }

        Utils.writeContents(HEAD_BRANCH, checkoutbranch);
    }

    /** Deletes the branch with the given name. This only
     *  means to delete the pointer
     *  associated with the branch; it does not mean to
     *  delete all commits that were
     *  created under the branch, or anything like that.
     *  @param branchname dkj d*/
    public static void rmbranch(String branchname) {
        if (branchname.equals(Utils.readContentsAsString(HEAD_BRANCH))) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        File branchFile = Utils.join(ALL_BRANCHES, branchname);
        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        } else {
            branchFile.delete();
        }
    }

    /**  Checks out all the files tracked by the given commit. Removes tra
     * cked files that are
     *   not present in that commit. Also moves the current branch's head
     *   to that commit node.
     *   See the intro for an example of what happens to the head pointer
     *   after using reset.
     *   The [commit id] may be abbreviated as for checkout. The staging a
     *   rea is cleared. The
     *   command is essentially checkout of an arbitrary commit that also
     *   changes the current
     *   branch head.
     *   @param commitSHA igjg*/
    public static void reset(String commitSHA) throws IOException {
        int counter = 0;
        String id = "";
        for (String commitName: Objects.requireNonNull(
                Utils.plainFilenamesIn(COMMIT_DIR),
                "COMMIT DIRECTORY IS NULL.")) {
            if (commitName.startsWith(commitSHA)) {
                counter = counter + 1;
                id = commitName;
            }
        }
        if (counter == 0) {
            System.out.println(" No commit with that id exists.");
            System.exit(0);
        } else if (counter > 1) {
            System.out.println("More than 1 commits with the given name.");
            System.exit(0);
        } else {
            Commit checkoutcommit = Utils.readObject(
                    Utils.join(COMMIT_DIR, id), Commit.class);
            for (String filename : checkoutcommit.mapping.keySet()) {
                checkoutCommit(filename, id);
            }
        }
        List<String> files = Utils.plainFilenamesIn(STAGING_DIR);
        if (files != null && files.size() != 0) {
            for (String filename: files) {
                Utils.join(STAGING_DIR, filename).delete();
            }
        }
        List<String> filesforremoval = Utils.plainFilenamesIn(
                STAGING_DIR_REMOVAL);
        if (filesforremoval != null && filesforremoval.size() != 0) {
            for (String filename: filesforremoval) {
                Utils.join(STAGING_DIR_REMOVAL, filename).delete();
            }
        }
        String branch = Utils.readContentsAsString(HEAD_BRANCH);
        Utils.writeContents(Utils.join(BRANCHES_DIR, branch), id);
        Utils.writeContents(HEAD, id);
    }
}
