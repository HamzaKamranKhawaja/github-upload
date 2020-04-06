/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Square.*;

/** Represents the state of a game of Lines of Action.
 *  @author
 */
class Board {

    /** Default number of moves for each side that results in a draw. */
    static final int DEFAULT_MOVE_LIMIT = 60;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row][col]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BP);
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        this();
        copyFrom(board);
        this._subsetsInitialized = false;
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        // FIXME
        _turn = side;
        _moveLimit = DEFAULT_MOVE_LIMIT;
        //to initialize the board. Copy contents from 2-D CONTENTS to 1-D _BOARD
        for (int i = 0; i < contents.length; i++) {
            System.arraycopy(contents[i], 0, _board,
                    i * BOARD_SIZE, BOARD_SIZE );
        }
        _subsetsInitialized = false;

    }

    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board !=  this) {
            _moveLimit = board._moveLimit;
            _turn = board._turn;
            System.arraycopy(board._board, 0, this._board,
                    0, board._board.length);
        }
        //FIXME? OR DONE
    }

    /** Return the contents of the square at SQ. */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /** Set the square at SQ to V and set the side that is to move next
     *  to NEXT, if NEXT is not null. */
    void set(Square sq, Piece v, Piece next) {
        // FIXME? Or done?
        int posn = sq.index();
        this._board[posn] = v;

        if (next != null) {
            _turn = next;
        }
        _subsetsInitialized = false;
    }

    /** Set the square at SQ to V, without modifying the side that
     *  moves next. */
    void set(Square sq, Piece v) {
        set(sq, v, null);
    }

    /** Set limit on number of moves by each side that results in a tie to
     *  LIMIT, where 2 * LIMIT > movesMade(). */
    void setMoveLimit(int limit) {
        if (2 * limit <= movesMade()) {
            throw new IllegalArgumentException("move limit too small");
        }
        _moveLimit = 2 * limit;
    }

    /** Assuming isLegal(MOVE), make MOVE. This function assumes that
     *  MOVE.isCapture() will return false.  If it saves the move for
     *  later retraction, makeMove itself uses MOVE.captureMove() to produce
     *  the capturing move. */
    void makeMove(Move move) {
        assert isLegal(move);
        // FIXME? MAKEMOVE itself uses MOVE.captureMove() ?
        Square firstS = move.getFrom();
        Piece firstP = get(firstS);
        Square endS = move.getTo();
        Piece endP = get(endS);

        if (firstP != endP && endP != EMP) {
            move = move.captureMove();
        }
        _moves.add(move);
        set (endS, firstP);
        set (firstS, EMP);
        _turn = _turn.opposite();
        _subsetsInitialized = false;

    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        // FIXME

        Move lastMove = _moves.get(_moves.size() - 1);
        Square firstS = lastMove.getFrom();
        Piece firstP = get(firstS);
        Square endS = lastMove.getTo();
        Piece endP = get(endS);

        if (lastMove.isCapture()) {
            set(firstS, endP);
            set(endS, endP.opposite());
        }
        else {
            set(firstS, endP);
            set(endS, EMP);
        }
        _moves.remove(_moves.size() - 1);
        _turn = _turn.opposite();
        _subsetsInitialized = false;
        _winnerKnown = false;

    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff FROM - TO is a legal move for the player currently on
     *  move. */
    boolean isLegal(Square from, Square to) {
        if (from.index() < 0 || from.index() >= ALL_SQUARES.length) {
            return false;
        }
        if (to.index() < 0 || to.index() >= ALL_SQUARES.length) {
            return false;
        }
        if (!from.isValidMove(to)) {
            return false;
        }
        if (get(from) == EMP) {
            return false;
        }
        if (blocked(from, to)) {
            return false;
        }
        if (get(from) != _turn) {
            return false;
        }
        if (get(from) == get(to)) {
            return false;
        }
        int dir = from.direction(to);
        int distance = from.distance(to);
        return getPiecesInLine(dir, from) == distance;
    }

    /** Return true iff MOVE is legal for the player currently on move.
     *  The isCapture() property is ignored. */
    boolean isLegal(Move move) {
        return isLegal(move.getFrom(), move.getTo());
    }

    /** Return a sequence of all legal moves from this position. */
    List<Move> legalMoves() {
        List<Move> moves = new ArrayList<Move>();

        for (int i = 0; i < ALL_SQUARES.length; i++) {
            for (int j = 0; j < ALL_SQUARES.length; j++) {
                if (i != j) {
                    if (isLegal(ALL_SQUARES[i], ALL_SQUARES[j])) {
                        moves.add(Move.mv(ALL_SQUARES[i], ALL_SQUARES[j]));
                    }
                }
            }
            }


        return moves;  // FIXME
    }

    /** Return true iff the game is over (either player has all his
     *  pieces continguous or there is a tie). */
    boolean gameOver() {
        return winner() != null;
    }

    /** Return true iff SIDE's pieces are continguous. */
    boolean piecesContiguous(Piece side) {
        int size = getRegionSizes(side).size();
        return getRegionSizes(side).size() == 1;
    }

    /** Return the winning side, if any.  If the game is not over, result is
     *  null.  If the game has ended in a tie, returns EMP. */
    Piece winner() {
        if (!_winnerKnown) {
            // FIXME
            computeRegions();
            if (piecesContiguous(_turn) &&
                    piecesContiguous(_turn.opposite())) {
                _winner = _turn.opposite();
                _winnerKnown = true;
            }
            else if (piecesContiguous(_turn.opposite())) {
                _winner = _turn.opposite();
                _winnerKnown = true;
            }
            else if (piecesContiguous(_turn)) {
                _winner = _turn;
                _winnerKnown = true;
            }
            else if (movesMade() >= _moveLimit) {
                _winner = EMP;
                _winnerKnown = true;
            }
            else {
                _winner = null;
            }
       }
        return _winner;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return Arrays.deepEquals(_board, b._board) && _turn == b._turn;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_board) * 2 + _turn.hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            out.format("    ");
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                out.format("%s ", get(sq(c, r)).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Return true if a move from FROM to TO is blocked by an opposing
     *  piece or by a friendly piece on the target square. */
    private boolean blocked(Square from, Square to) {
        if (get(to) == get(from)) {
            return true;
        } else { ;
            int direction = from.direction(to);
            int distance = from.distance(to);
            for (int i = distance - 1; i > 0; i--) {
                Square sq = from.moveDest(direction, i);
                if (sq != null) {
                    if (get(sq) == get(from).opposite()) {
                        return true;
                    }
                }
            }
            return false; // FIXME
        }
    }

    /** Return the size of the as-yet unvisited cluster of squares
     *  containing P at and adjacent to SQ.  VISITED indicates squares that
     *  have already been processed or are in different clusters.  Update
     *  VISITED to reflect squares counted. */
    public int numContig(Square sq, boolean[][] visited, Piece p) {
        if (get(sq) == EMP) {
            return 0;
        }
        else if (get(sq) != p) {
            return 0;
        }
        else if (visited[sq.row()][sq.col()]) {
            return 0;
        }
        int counter = 1;
        visited[sq.row()][sq.col()] = true;
        Square[] allsquares = sq.adjacent();
        for (Square adjsq: allsquares) {
            counter = counter + numContig(adjsq, visited, p);
        }
        return counter;  // FIXME
    }

    /** Set the values of _whiteRegionSizes and _blackRegionSizes. */
    public void computeRegions() {
        if (_subsetsInitialized) {
            return;
        }
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();
        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        for (Square sq: ALL_SQUARES) {
            if (get(sq) == WP) {
                int whiteSize = numContig(sq, visited, WP);
                if (whiteSize != 0) {
                    _whiteRegionSizes.add(whiteSize);
                }
            }
            if (get(sq) == BP) {
                int blackSize = numContig(sq, visited, BP);
                if (blackSize != 0) {
                    _blackRegionSizes.add(blackSize);
               }
            }
        }

        Collections.sort(_whiteRegionSizes, Collections.reverseOrder());
        Collections.sort(_blackRegionSizes, Collections.reverseOrder());
        _subsetsInitialized = true;
    }

    /** Return the sizes of all the regions in the current union-find
     *  structure for side S. */
    List<Integer> getRegionSizes(Piece s) {
        computeRegions();
        if (s == WP) {
            return _whiteRegionSizes;
        } else {
            return _blackRegionSizes;
        }
    }

    /** A heuristic function that obtains the sum of the number of
     * maximizing contiguous regions
     * and subtract the number of minimizing contiguous regions
      */
    public int boardState() {
        if (this.winner() == Maximizer) {
            return WINNING_VALUE;
        }
        else if (this.winner() == Maximizer.opposite()) {
            return -WINNING_VALUE;
        }

        return getRegionSizes(Maximizer).size()
                - getRegionSizes(Maximizer.opposite()).size();
    }

    // FIXME: Other methods, variables?
    /** Calculates sum of distances between all pieces of BLACK side and
     * subtract WHITE side distances ie BLACK is the minimizer */

    public int getSumdistances() {
        int distance = 0;
        for (Square sq : ALL_SQUARES) {
            for (Square sq2 : ALL_SQUARES) {
                if (get(sq) == Maximizer.opposite() && !sq.equals(sq2)
                        && Maximizer.opposite() == get(sq2)) {
                    distance += sq.distance(sq2);
                }
                else if (get(sq) == Maximizer && !sq.equals(sq2)
                        && Maximizer == get(sq2)) {
                    distance -= sq.distance(sq2);
                }
            }
        }
        return distance;
    }

    /** Returns the number of non-EMP pieces in both a direction and its opposite,
     * defined as (direction + 4) % 8.
     */
    protected  int getPiecesInLine (int direction, Square from) {
        int pieces = 1;
        int oppositedir = (direction + 4) % 8;
        for (int i = 1; i < BOARD_SIZE ; i++) {
            Square indirection = from.moveDest(direction, i);
            Square awaydirection = from.moveDest(oppositedir, i);
            if (indirection != null && get(indirection) != EMP) {
                pieces = pieces + 1;
            }
            if (awaydirection != null && get(awaydirection) != EMP) {
                pieces = pieces + 1;
            }
        }
        return pieces;
    }
    /** The standard initial configuration for Lines of Action (bottom row
     *  first). */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** Current contents of the board.  Square S is at _board[S.index()]. */
    protected Piece[] _board = new Piece[BOARD_SIZE * BOARD_SIZE];


    /** Maximizer for the heuristic functions */
    Piece Maximizer = WP;
    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;
    /** Limit on number of moves before tie is declared.  */
    private int _moveLimit;
    /** True iff the value of _winner is known to be valid. */
    private boolean _winnerKnown;
    /** Cached value of the winner (BP, WP, EMP (for tie), or null (game still
     *  in progress).  Use only if _winnerKnown. */
    private Piece _winner;

    /** True iff subsets computation is up-to-date. */
    private boolean _subsetsInitialized;

    /** List of the sizes of continguous clusters of pieces, by color. */
    private final ArrayList<Integer>
        _whiteRegionSizes = new ArrayList<>(),
        _blackRegionSizes = new ArrayList<>();

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
}
