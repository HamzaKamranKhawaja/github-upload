# Gitlet Design Document

**Name**: Hamza Kamran Khawaja

## Classes and Data Structures
### Main.java
Parses the input and throws errors if incorrect input format. 
* initilize : initializes a .gitlet directory and sets up persistence
###Branch.java
* branch - *create a branch*
###Commit.java
* Commit - represents a commit object
 
**Instance Variables**
1. message
2. dateTime
3. parent
* Commit (default) - for the initial commit
* init
#UnitTest.java
#Utils.java
#Commands.java
* add : adds a file with the given name to the staging directory
* remove : adds a file with the given name to the removal directory
* log : prints out commits sequentially till initial commit recursively
* global-log : prints out all commits ever made
* find : finds all the commits with the given message
* status : outputs the status of the .gitlet directory
* checkoutFile : checks out file with the given name (overwrites files to the file tracked by the given commit)
* checkoutFileDefault : default checkoutFile for the current commit
* checkoutCommit : checkout a file in the given commit
* checkouBbranch : checks out all files in the given branch
* rmbranch : remove a branch
* reset : reset a branch (essentially like checkoutCommit but also moves BRANCH pointers)



## Algorithms



## Persistence
* GITLET_DIR - *contains the main gitlet directory*
* COMMIT_DIR - *contains all commits ever created with names 
as of the SHA-1 ID of the serialized commits*
* STAGING_DIR - *stage for addition*
* STAGING_DIR_REMOVAL - *stage for removal*
* CONTENT_DIR - *contains the serialized blob SHA-1 names as the filenames*
and the contents as the string contents of the original file *
* BRANCHES_DIR - *all branches ever created that contains ALL_BRANCHES directory
and HEAD_BRANCH file*
* ALL_BRANCHES - *All branches ever created with the name of the branch as the 
file name and the contents as the serialized SHA-1 of the commit the head of the 
branch is at*
* HEAD_BRANCH - *the name of the current branch*

