/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.ArrayList;
import java.util.List;

import static loa.Piece.*;

/** An automated Player.
 *  @author
 */
class MachinePlayer extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new MachinePlayer with no piece or controller (intended to produce
     *  a template). */
    MachinePlayer() {
        this(null, null);
    }

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    String getMove() {
        Move choice;

        assert side() == getGame().getBoard().turn();
        int depth;
        choice = searchForMove();
        getGame().reportMove(choice);
        return choice.toString();
    }

    @Override
    Player create(Piece piece, Game game) {
        return new MachinePlayer(piece, game);
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private Move searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert side() == work.turn();
        _foundMove = null;
        if (side() == WP) {
            value = findMove(work, chooseDepth(), true, 1, -INFTY, INFTY);
        } else {
            value = findMove(work, chooseDepth(), true, -1, -INFTY, INFTY);
        }
        return _foundMove;
    }

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {

        if (depth == 0) {
            return board.getSumdistances();
            }

        int bestscore = 0; //? 0 or something else
        Board copied = new Board();
        copied.copyFrom(board);
        for (Move move : board.legalMoves()) {
            copied.makeMove(move);
        //the updated board and depth is used
            int score = findMove(copied, depth - 1,saveMove,
                    -sense, alpha, beta);
            if (sense == 1 && score > bestscore) {
                bestscore = score;
            }
            else if (sense == -1 && score < bestscore) {
                bestscore = score;
            }
            if (board.turn() == WP) {
                alpha = Math.max(score, alpha);
                if (copied.piecesContiguous(WP)) {
                    bestscore = WINNING_VALUE;
                    alpha = WINNING_VALUE;
                }
            }
            else {
                beta = Math.min(score, beta);
                if (copied.piecesContiguous(BP)) {
                    _foundMove = move;
                    break;
                }
            }

            if (alpha >= beta) {
                saveMove = true;
            }
            copied.retract();
            if (saveMove) {
                _foundMove = move; // FIXME
                break;
            }
        }
        //Prune, how?  Break

        return bestscore; // FIXME
    }


    /** Return a search depth for the current position. */
    private int chooseDepth() {
        return 7;  // FIXME
    }

    // FIXME: Other methods, variables here.
    /** Return the score based on the current heuristic function */
    private int findscore(Piece side, Board board) {
        int sumdistances = 0;
        /*for (int i = 0; i < ; i++) {*/

        //}
        return 1;
    }

    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;

}
