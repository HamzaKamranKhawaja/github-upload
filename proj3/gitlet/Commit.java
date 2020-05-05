package gitlet;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static gitlet.Main.*;


/** Saves a snapshot of certain files in the current commit
 * and staging area
 *so they can be restored at a later time, creating a new co
 * mmit. The commit
 *  is said to be tracking the saved files. By default, each
 *  commit's snapshot
 *  of files will be exactly the same as its parent commit's
 *  snapshot of files;
 *  it will keep versions of files exactly as they are, and
 *  not update them. A
 *  commit will only update the contents of files it is trac
 *  king that have been
 *  staged for addition at the time of commit, in which case
 *  the commit will now
 *  include the version of the file that was staged instead
 *  of the version it got
 *  from its parent. A commit will save and start tracking a
 *  ny files that were staged
 *  for addition but weren't tracked by its parent. Finally,
 *  files tracked in the current
 *  commit may be untracked in the new commit as a result be
 *  ing staged for removal by the
 *  rm command (below).
 *
 *  The bottom line: By default a commit is the same as its
 *  parent. Files staged for addition
 *  and removal are the updates to the commit.
 *   ===
 *    commit a0da1ea5a15ab613bf9961fd86f010cf74c7ee48
 *    Date: Thu Nov 9 20:00:05 2017 -0800
 *    A commit message.
 *
 *    ===
 * @author hamza
 */
public class Commit implements Serializable {

    /** default constructor.
     * @param messagetosave kdsjfkldsjfk */
    public Commit(String messagetosave) {
        LocalDateTime epochObject = Instant.ofEpochMilli(0).
                atZone(ZoneId.of("America/Los_Angeles")).toLocalDateTime();

        this.message = messagetosave;
        this.parent = null;
        this.mapping = null;
        this.dateTime = epochObject;
    }

    /** constructor that takes in a message, a Localdatetime object,
     * and commit parents SHA-1 String.
     * @param datetime kdsfjkfd
     * @param  messagetosave jkdjkfj
     * @param commitParent jdksjkdfj */
    public Commit(String commitParent, String messagetosave,
                  LocalDateTime datetime) throws IOException {
        this.message = messagetosave;
        this.parent = commitParent;
        this.dateTime = datetime;
        Commit parentCommit = Utils.readObject(
                Utils.join(COMMIT_DIR, commitParent), Commit.class);
        if (parentCommit.mapping == null) {
            this.mapping = new HashMap<String, String>();
        } else {
            this.mapping = Utils.deepClone(parentCommit.mapping);
        }
        List<String> stagedFilenames = Utils.plainFilenamesIn(STAGING_DIR);

        if (stagedFilenames != null && stagedFilenames.size() != 0) {
            for (String filename : stagedFilenames) {
                File file = Utils.join(STAGING_DIR, filename);
                String filecontents = Utils.readContentsAsString(file);
                String updatedFileSHA = Utils.sha1(filecontents);
                this.mapping.put(filename, updatedFileSHA);
                File addChangedtoContents = Utils.join(
                        CONTENT_DIR, updatedFileSHA);
                try {
                    addChangedtoContents.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Cannot Create file with name: "
                            + filename);
                }
                Utils.writeContents(addChangedtoContents, filecontents);
                boolean deleted = file.delete();
            }
        }
        if (STAGING_DIR_REMOVAL.list() != null
                && Objects.requireNonNull(
                        STAGING_DIR_REMOVAL.list()).length != 0) {
            for (String forRemoval : Objects.requireNonNull(
                    Utils.plainFilenamesIn(STAGING_DIR_REMOVAL))) {
                this.mapping.remove(forRemoval);
                File removed = Utils.join(STAGING_DIR_REMOVAL, forRemoval);
                removed.delete();
            }
        }
        byte[] serializedCommit = Utils.serialize(this);
        String id = Utils.sha1(serializedCommit);
        File commitFile = Utils.join(COMMIT_DIR, id);
        commitFile.createNewFile();
        Utils.writeObject(commitFile, this);
        Utils.writeContents(HEAD, id);
        String currentBranch = Utils.readContentsAsString(HEAD_BRANCH);
        File thisBranch = Utils.join(ALL_BRANCHES, currentBranch);
        if (!thisBranch.exists()) {
            System.out.println("WTF current Branch: "
                    + currentBranch + " doesnt exist!!!!");
            System.exit(0);
        }
        Utils.writeContents(thisBranch, id);
    }

    /** The init method that uses the default constructor. */
    public static void init() {
        byte[] serializedBlank = Utils.serialize(BLANK);
        String id = Utils.sha1(serializedBlank);
        File commitFile = Utils.join(COMMIT_DIR, id);
        try {
            commitFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot create commit with id: " + id);
        }
        Utils.writeContents(commitFile, serializedBlank);
        Utils.writeContents(HEAD, id);
        File headBranch = Utils.join(ALL_BRANCHES, "master");
        try {
            headBranch.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot Create master branch");
        }
        Utils.writeContents(headBranch, id);
        Utils.writeContents(HEAD_BRANCH, "master");
    }


    /** Hashmap MAPPING to store SHA-1 String: COMMIT mappings. */
    protected HashMap<String, String> mapping = new HashMap<String, String>();

    /** Return the MESSAGE associated with this Commit. */
    public String getMessage() {
        return this.message;
    }

    /** Return the DateTime of this Commit. */
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    /** Return the Parent Commit's SHA-1 String of this Commit. */
    public String getParent() {
        return this.parent;
    }

    /** The first commit ever. */
    static final Commit BLANK = new Commit("initial commit");

    /** The MESSAGE associated with this commit. */
    protected String message;

    /** The Time and Date of this commit as a LocalTime instance. Immutable. */
    protected LocalDateTime dateTime;

    /** The parent of the current commit. */
    protected String parent;

}
