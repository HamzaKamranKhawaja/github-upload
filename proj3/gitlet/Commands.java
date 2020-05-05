package gitlet;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static gitlet.Commit.*;
import static gitlet.Main.*;

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
     *   */

    public static void add(String Filename) throws IOException {
        File addedFile = Utils.join(CWD, Filename);
        if (!addedFile.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        File staged = Utils.join(STAGING_DIR, Filename);
        File removeFile = Utils.join(STAGING_DIR_REMOVAL, Filename);
        String commitSHA = Utils.readContentsAsString(HEAD);
        Commit lastCommit = Utils.readObject(Utils.join(COMMIT_DIR, commitSHA), Commit.class);
        if (removeFile.exists()) {
            removeFile.delete();
        }
        if (lastCommit.MAPPING != null
                && lastCommit.MAPPING.containsKey(Filename)) {
            String fileSHA = lastCommit.MAPPING.get(Filename);
            String contents = Utils.readContentsAsString(Utils.join(CONTENT_DIR, fileSHA));
            String CWDcontents = Utils.readContentsAsString(addedFile);
            if (CWDcontents.equals(contents)) {
                if (staged.exists()) {
                    staged.delete();
                }
                if (!removeFile.exists()) {
                    removeFile.delete();
                }
            } else {
                Utils.writeContents(staged, CWDcontents);
            }
        } else {
            String CWDcontents = Utils.readContentsAsString(addedFile);
            Utils.writeContents(staged, CWDcontents);
            }
        }

    /**
     * Unstage the file if it is currently staged for addition.
     * If the file is tracked in the current commit, stage it for
     * removal and remove the file from the working directory if
     * the user has not already done so (do not remove it unless
     * it is tracked in the current commit).If the file is neither
     * staged nor tracked by the head commit, print the error message
     * "No reason to remove the file.". */

    public static void remove(String filename) throws IOException {
        File file = Utils.join(STAGING_DIR, filename);
        String commitID = Utils.readContentsAsString(HEAD);
        Commit lastCommit = Utils.readObject(Utils.join(COMMIT_DIR, commitID), Commit.class);
        if (!file.exists() && lastCommit.MAPPING != null &&
                !lastCommit.MAPPING.containsKey(filename)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }

        if (file.exists()) {
            file.delete();
        }
        if (lastCommit.MAPPING != null &&
                lastCommit.MAPPING.containsKey(filename)) {
           // try { so I dont know if this will work on gradescope but I've tested it locally and remove already works
            // you had something similar?
            //oh nice. Great minds think alike. bhai bhai. I'm laughing like crazy

                Utils.join(STAGING_DIR_REMOVAL, filename).createNewFile();
                Utils.restrictedDelete(Utils.join(CWD, filename)); //FIXME: Should it delete from the CWD or somewhere else?
           // } catch (IOException e) {
              //  e.printStackTrace();
                //System.out.println("File cannot be added to Removal Directory.");

        }
    }


    public static void log(String lastCommit) {
        //FIXME: FOR MERGE PARENTS? For merge
        //FIXME: commits (those that have two parent commits), add a line just below the first,
        // as in:
        // commit 3e8bf1d794ca2e9ef8a4007275acf3751c7170ff
        // Merge: 4975af1 2c1ead1
        // Date: Sat Nov 11 12:30:00 2017 -0800
        // Merged development into master.


        File commitFile = Utils.join(COMMIT_DIR, lastCommit);
        Commit thisCommit = Utils.readObject(commitFile, Commit.class);
        String ParentSHA = thisCommit.parent;
        if (ParentSHA == null) {
            System.out.println("===");
            System.out.println("commit " + lastCommit);
            //%tb %ta %ta
            String dateStr = thisCommit.dateTime.format(
                    DateTimeFormatter.ofPattern("EE MMM dd HH:mm:ss yyyy"));
            System.out.println("Date: " + dateStr + " -0800"); //FIXME: DISPLAY PST NOT UTC e.g Wed Dec 31 16:00:00 1969 -0800
            System.out.println(thisCommit.message);
        } else {
            System.out.println("===");
            System.out.println("commit " + lastCommit);
            String dateStr = thisCommit.dateTime.format(
                    DateTimeFormatter.ofPattern("EE MMM dd HH:mm:ss yyyy"));
            System.out.println("Date: " + dateStr + " -0800"); //FIXME: should be proper format e.g Thu Nov 9 17:01:33 2017 -0800
            System.out.println(thisCommit.message);
            System.out.println();
            log(ParentSHA);
        }
    }

    public static void global_log() {

        if (COMMIT_DIR.exists() && COMMIT_DIR.listFiles() != null) {
            for (String commitFile : Objects.requireNonNull(Utils.plainFilenamesIn(COMMIT_DIR),
                    "Commit dir cannot be null")) {
                if (!commitFile.equals("HEAD")) {
                    File FileWithCommit = Utils.join(COMMIT_DIR, commitFile);
                    Commit thisCommit = Utils.readObject(FileWithCommit, Commit.class);
                    String ParentSHA = thisCommit.parent;
                    if (ParentSHA == null) {
                        System.out.println("===");
                        System.out.println("commit " + commitFile);
                        String dateStr = thisCommit.dateTime.format(
                                DateTimeFormatter.ofPattern("EE MMM dd HH:mm:ss yyyy"));
                        System.out.println("Date: " + dateStr + " -0800"); //FIXME: DISPLAY PST NOT UTC e.g Wed Dec 31 16:00:00 1969 -0800
                        System.out.println(thisCommit.message);
                        System.out.println();
                    } else {
                        System.out.println("===");
                        System.out.println("commit " + commitFile);
                        String dateStr = thisCommit.dateTime.format(
                                DateTimeFormatter.ofPattern("EE MMM dd HH:mm:ss yyyy"));
                        System.out.println("Date: " + dateStr + " -0800"); //FIXME: DISPLAY PST NOT UTC e.g Wed Dec 31 16:00:00 1969 -0800
                        System.out.println(thisCommit.message);
                        System.out.println();
                    }
                }
            }
        }
    }

    /** Prints out the ids of all commits that have the given commit message, one per line.
     *  If there are multiple such commits, it prints the ids out on separate lines.
     *  The commit message is a single operand. For multilword messages, quotation used */

    public static void find(String message) {
        if (COMMIT_DIR.exists() && COMMIT_DIR.listFiles() != null) {
            boolean commitExists = false;
            for (String commitFile : Objects.requireNonNull(Utils.plainFilenamesIn(COMMIT_DIR),
                    "Commit dir cannot be null")) {
                if (!commitFile.equals("HEAD")) {
                File FileWithCommit = Utils.join(COMMIT_DIR, commitFile);
                Commit thisCommit = Utils.readObject(FileWithCommit, Commit.class);
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

    /** Displays what branches currently exist, and marks the current branch with a *.
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
        //FIXME: Replace the default list() method with the UTILS.PLAINFILENAMESIN method
        String HeadBranch = Utils.readContentsAsString(HEAD_BRANCH);
        System.out.println("=== Branches ===");
        if (ALL_BRANCHES.exists() && ALL_BRANCHES.list() != null
        && Objects.requireNonNull(ALL_BRANCHES.list()).length != 0) {
            for (String branchname: Objects.requireNonNull(ALL_BRANCHES.list())) {
                if (HeadBranch.equals(branchname)) {
                    System.out.println("*"+branchname);
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
                && Objects.requireNonNull(STAGING_DIR_REMOVAL.list()).length != 0) {
            for (String filename: Objects.requireNonNull(STAGING_DIR_REMOVAL.list())) {
                System.out.println(filename);
            }
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();

        //FIXME: DO THE SAME FOR Modifications Not Staged For Commit
        // AND UNTRACKED FILES. SEE JAVA DOC.
    }

    /** java gitlet.Main checkout -- [file name]
     *  java gitlet.Main checkout [commit id] -- [file name]
     *  java gitlet.Main checkout [branch name]
     *  1- Takes the version of the file as it exists in the head commit, the front of the current branch,
     *  and puts it in the working directory, overwriting the version of the file that's already there if
     *  there is one. The new version of the file is not staged. */

    public static void checkoutFile(String Filename, String CommitID) throws IOException {
        File CommitFile = Utils.join(COMMIT_DIR, CommitID);
        if (!CommitFile.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit headCommit = Utils.readObject(CommitFile, Commit.class);
        if (headCommit.MAPPING == null
                || !headCommit.MAPPING.containsKey(Filename)) {
            System.out.println("File does not exist in the commit.");
            System.exit(0);
        } else {
            File CWDFile = Utils.join(CWD, Filename);
            String FileSHA = headCommit.MAPPING.get(Filename);
            String FileContents = Utils.readContentsAsString(Utils.join(CONTENT_DIR, FileSHA));
            if (CWDFile.exists()) {
                Utils.writeContents(CWDFile, FileContents);
            } else if (!CWDFile.exists()) {
                //try {
                    File newCWDFile =  Utils.join(CWD, Filename);
                    newCWDFile.createNewFile();
                    Utils.writeContents(newCWDFile, FileContents);
                //} catch (IOException e) {
                  //  throw new GitletException("CWD file cannot be created with name: " + Filename);
                //}
            }
        }
    }
    /** For Default, we simply pass in HeadCommit in CHECKOUTFILE. */
    public static void checkoutFileDefault(String Filename) throws IOException {
        checkoutFile(Filename, getHead());
    }
     /** 2- Takes the version of the file as it exists in the commit with the given id, and puts it in the
     * working directory, overwriting the version of the file that's already there if there is one. The
     * new version of the file is not staged. */

    public static void checkoutCommit(String Filename, String CommitID) throws IOException { //FIXME: CHECKOUT TO THE INIT??
        int counter = 0;
        String ID = "";
        for (String commitName: Objects.requireNonNull(Utils.plainFilenamesIn(COMMIT_DIR),
                "COMMIT DIRECTORY IS NULL.")) {
            if (commitName.startsWith(CommitID)) {
                counter = counter + 1;
                ID = commitName;
            }
        }
        if (counter == 0) {
            System.out.println(" No commit with that id exists.");
            System.exit(0);
        } else if (counter > 1) {
            System.out.println("More than 1 commits with the given name.");
            System.exit(0);
        } else {
            checkoutFile(Filename, ID);
        }
    }

    /** 3- Takes all files in the commit at the head of the given branch, and puts them in the working directory,
    * overwriting the versions of the files that are already there if they exist. Also, at the end of this command,
    * the given branch will now be considered the current branch (HEAD). Any files that are tracked in the current
    * but are not present in the checked-out branch are deleted. The staging area is cleared, unless the checked-out
    * branch is the current branch (see Failure cases). */
    public static void checkoutBranch(String checkoutbranch) throws IOException {
        String currentBranch = Utils.readContentsAsString(HEAD_BRANCH);
        String currentcommitID = Utils.readContentsAsString(HEAD);
        if (checkoutbranch.equals(currentBranch)) {
            System.out.println("No need to checkout the current branch");
            System.exit(0);
        }
        if (!Objects.requireNonNull(Utils.plainFilenamesIn(ALL_BRANCHES)).contains(checkoutbranch)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        //fixme: can the above be used to check ALL_BRANCHES (or use boolean)
        String checkoutcommitID = Utils.readContentsAsString(Utils.join(ALL_BRANCHES, checkoutbranch));
        Commit checkoutcommit = Utils.readObject( Utils.join(COMMIT_DIR, checkoutcommitID), Commit.class);
        Commit currentcommit = Utils.readObject(Utils.join(COMMIT_DIR, currentcommitID), Commit.class);
        if (checkoutcommit.MAPPING == null) {
            if (currentcommit.MAPPING != null) {
                for (String currentfile : currentcommit.MAPPING.keySet()) {
                    File todelete = Utils.join(CWD, currentfile);
                    if (todelete.exists()) {
                        Utils.restrictedDelete(todelete);
                    }
                }
            }

        } else {
            System.out.println("I am in else part");
            for (String checkoutfile : checkoutcommit.MAPPING.keySet()) {
                System.out.println("I am in for loop, and file is:" + checkoutfile);
                for (String fileinCWD : Objects.requireNonNull(Utils.plainFilenamesIn(CWD))) { //FIXME: Perhaps you could use contains?
                    System.out.println("I am in for loop, and CWDfile is:" + fileinCWD);
                    if (fileinCWD.equals(checkoutfile)) {
                        System.out.println(checkoutfile +" is being tracked");
                        if (currentcommit.MAPPING != null
                                && !currentcommit.MAPPING.containsKey(checkoutfile)) {
                            System.out.println(" There is an untracked file in the way; delete it, or add and commit it first.");
                            System.exit(0);
                        }
                    }
                }
                String fileID = checkoutcommit.MAPPING.get(checkoutfile);
                String contents = Utils.readContentsAsString(Utils.join(CONTENT_DIR, fileID));
                Utils.writeContents(Utils.join(CWD, checkoutfile), contents);
            }
            if (currentcommit.MAPPING != null) {
                for (String currentfile : currentcommit.MAPPING.keySet()) {
                    if (!checkoutcommit.MAPPING.containsKey(currentfile)) {
                        File todelete = Utils.join(CWD, currentfile);
                        if (todelete.exists()) {
                            Utils.restrictedDelete(todelete);
                        }
                    }
                }
            }
            for (String checkoutfile : checkoutcommit.MAPPING.keySet()) {
                checkoutCommit(checkoutfile, checkoutcommitID);
            }
            for (String currentfile : currentcommit.MAPPING.keySet()) {
                //FIXME: WHAT IF CHECKOUT MAPPING IS NULL??
                if (!checkoutcommit.MAPPING.containsKey(currentfile)) {
                    File todelete = Utils.join(CWD, currentfile);
                    if (todelete.exists()) {
                        Utils.restrictedDelete(todelete);
                    }
                }
            }
        }
            List<String> files = Utils.plainFilenamesIn(STAGING_DIR);
            if (files != null && files.size() != 0) {
                for (String filename : files) {
                    Utils.join(STAGING_DIR, filename).delete();
                }
            }
            List<String> filesforremoval = Utils.plainFilenamesIn(STAGING_DIR_REMOVAL);
            if (filesforremoval != null && filesforremoval.size() != 0) {
                for (String filename : filesforremoval) {
                    Utils.join(STAGING_DIR_REMOVAL, filename).delete();
                }
            }

        Utils.writeContents(HEAD_BRANCH, checkoutbranch);
    }


    /** Deletes the branch with the given name. This only means to delete the pointer
     *  associated with the branch; it does not mean to delete all commits that were
     *  created under the branch, or anything like that. */
    public static void rmbranch(String branchname) {
        if (branchname.equals(Utils.readContentsAsString(HEAD_BRANCH))) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        File branchFile = Utils.join(ALL_BRANCHES, branchname);
        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        else {
            branchFile.delete();
        }
    }

    /**  Checks out all the files tracked by the given commit. Removes tracked files that are
     *   not present in that commit. Also moves the current branch's head to that commit node.
     *   See the intro for an example of what happens to the head pointer after using reset.
     *   The [commit id] may be abbreviated as for checkout. The staging area is cleared. The
     *   command is essentially checkout of an arbitrary commit that also changes the current
     *   branch head. */
    public static void reset(String commitSHA) throws IOException {
        int counter = 0;
        String ID = "";
        for (String commitName: Objects.requireNonNull(Utils.plainFilenamesIn(COMMIT_DIR),
                "COMMIT DIRECTORY IS NULL.")) {
            if (commitName.startsWith(commitSHA)) {
                counter = counter + 1;
                ID = commitName;
            }
        }
        if (counter == 0) {
            System.out.println(" No commit with that id exists.");
            System.exit(0);
        } else if (counter > 1) {
            System.out.println("More than 1 commits with the given name.");
            System.exit(0);
        } else {
            Commit checkoutcommit = Utils.readObject(Utils.join(COMMIT_DIR, ID), Commit.class);
            for (String filename: checkoutcommit.MAPPING.keySet()) //FIXME: CAN WE RESET TO THE INIT COMMIT?
            checkoutCommit(filename, ID);
        }
        List<String> files = Utils.plainFilenamesIn(STAGING_DIR);
        if (files != null && files.size() != 0) {
            for (String filename: files) {
                Utils.join(STAGING_DIR, filename).delete();
            }
        }
        List<String> filesforremoval = Utils.plainFilenamesIn(STAGING_DIR_REMOVAL);
        if (filesforremoval != null && filesforremoval.size() != 0) {
            for (String filename: filesforremoval) {
                Utils.join(STAGING_DIR_REMOVAL, filename).delete();
            }
        }
        String branch = Utils.readContentsAsString(HEAD_BRANCH);
        Utils.writeContents(Utils.join(BRANCHES_DIR, branch), ID);
        Utils.writeContents(HEAD, ID);
    }
}
