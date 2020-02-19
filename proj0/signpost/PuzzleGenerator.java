package signpost;

import java.util.Collections;
import java.util.Random;

import signpost.Model.Sq;

import static signpost.Place.*;
import static signpost.Utils.*;

/** A creator of random Signpost puzzles.
 *  @author
 */
class PuzzleGenerator implements PuzzleSource {

    /** A new PuzzleGenerator whose random-number source is seeded
     *  with SEED. */
    PuzzleGenerator(long seed) {
        _random = new Random(seed);
    }

    @Override
    public Model getPuzzle(int width, int height, boolean allowFreeEnds) {
        Model model =
                new Model(makePuzzleSolution(width, height, allowFreeEnds));
        makeSolutionUnique(model);
        model.autoconnect();
        return model;
    }

    /** Return an array representing a WIDTH x HEIGHT Signpost puzzle.
     *  The first array index indicates x-coordinates (column numbers) on
     *  the board, and the second index represents y-coordinates (row numbers).
     *  Its values will be the sequence numbers (1 to WIDTH x HEIGHT)
     *  appearing in a sequence queen moves on the resulting board.
     *  Unless ALLOWFREEENDS, the first and last sequence numbers will
     *  appear in the upper-left and lower-right corners, respectively. */
    private int[][] makePuzzleSolution(int width, int height,
                                       boolean allowFreeEnds) {
        _vals = new int[width][height];
        _successorCells = Place.successorCells(width, height);
        int last = width * height;
        int x0, y0, x1, y1;
        if (allowFreeEnds) {
            int r0 = _random.nextInt(last),
                    r1 = (r0 + 1 + _random.nextInt(last - 1)) % last;
            x0 = r0 / height; y0 = r0 % height;
            x1 = r1 / height; y1 = r1 % height;
        } else {
            x0 = 0; y0 = height - 1;
            x1 = width - 1; y1 = 0;
        }
        _vals[x0][y0] = 1;
        _vals[x1][y1] = last;
        boolean ok = findSolutionPathFrom(x0, y0);
        assert ok;
        return _vals;
    }

    /** Try to find a random path of queen moves through VALS from (X0, Y0)
     *  to the cell with number LAST.  Assumes that
     *    + The dimensions of VALS conforms to those of MODEL;
     *    + There are cells (separated by queen moves) numbered from 1 up to
     *      and including the number in (X0, Y0);
     *    + There is a cell numbered LAST;
     *    + All other cells in VALS contain 0.
     *  Does not change the contents of any non-zero cell in VALS.
     *  Returns true and leaves the path that is found in VALS.  Otherwise
     *  returns false and leaves VALS unchanged. Does not change MODEL. */
    private boolean findSolutionPathFrom(int x0, int y0) {
        int w = _vals.length, h = _vals[0].length;
        int v;
        int start = _vals[x0][y0] + 1;
        PlaceList moves = _successorCells[x0][y0][0];
        Collections.shuffle(moves, _random);
        for (Place p : moves) {
            v = _vals[p.x][p.y];
            if (v == 0) {
                _vals[p.x][p.y] = start;
                if (findSolutionPathFrom(p.x, p.y)) {
                    return true;
                }
                _vals[p.x][p.y] = 0;
            } else if (v == start && start == w * h) {
                return true;
            }
        }
        return false;
    }

    /** Extend unambiguous paths in MODEL (add all connections where there is
     *  a single possible successor or predecessor). Return true iff any change
     *  was made. */
    static boolean extendSimple(Model model) {
        boolean found;
        found = false;
        while (makeForwardConnections(model)
                || makeBackwardConnections(model)) {
            found = true;
        }
        return found;
    }

    /** Make all unique forward connections in MODEL (those in which there is
     *  a single possible successor).  Return true iff changes were made. */
    static boolean makeForwardConnections(Model model) {
        int w = model.width(), h = model.height();
        boolean result;
        result = false;
        for (Sq sq : model) {
            if (sq.successor() == null && sq.direction() != 0) {
                Sq found = findUniqueSuccessor(model, sq);
                if (found != null) {
                    sq.connect(found);
                    result = true;
                }
            }
        }
        return result;
    }

    /** Return the unique square in MODEL to which unconnected square START
     *  can connect, or null if there isn't such a unique square. The unique
     *  square is either (1) the only connectable square in the proper
     *  direction from START, or (2) if START is numbered, a connectable
     *  numbered square in the proper direction from START (with the next
     *  number in sequence). */

    static Sq findUniqueSuccessor(Model model, Sq start) {

        PlaceList Success = start.successors();

        if (start.sequenceNum() == 0) {
            PlaceList pres = new PlaceList();

            for (Place can: Success) {
                if (Place.dirOf(start.x, start.y, can.x, can.y) != 0) {
                    if (model.get(can).predecessor() == null) {
                        pres.add(can);
                    }
                }
            }

            if (pres.size() == 1) {
                return model.get(pres.get(0));
            } else {
                return null;
            }

        } else {
            PlaceList pres = new PlaceList();

            for (Place can: Success) {
                if (Place.dirOf(start.x, start.y, can.x, can.y) != 0) {
                    if (model.get(can).predecessor() == null) {
                        pres.add(can);
                    }
                }
            }

            if (pres.size() == 1) {
                return model.get(pres.get(0));
            } else {
                return null;
            }

        }
    }


    /** Make all unique backward connections in MODEL (those in which there is
     *  a single possible predecessor).  Return true iff changes made. */
    static boolean makeBackwardConnections(Model model) {
        int w = model.width(), h = model.height();
        boolean result;
        result = false;
        for (Sq sq : model) {
            if (sq.predecessor() == null && sq.sequenceNum() != 1) {
                Sq found = findUniquePredecessor(model, sq);
                if (found != null) {
                    found.connect(sq);
                    result = true;
                }
            }
        }
        return result;
    }


    /** Return the unique square in MODEL that can connect to unconnected
     *  square END, or null if there isn't such a unique square.
     *  This function does not handle the case in which END and one of its
     *  predecessors is numbered, except when the numbered predecessor is
     *  the only unconnected predecessor.  This is because findUniqueSuccessor
     *  already finds the other cases of numbered, unconnected cells. */
    static Sq findUniquePredecessor(Model model, Sq end) {
        if (end.predecessors() != null) {
            if (end.predecessors().size() == 1) {
                if (model.get(end.predecessors().get(0)).connectable(end)) {
                    return model.get(end.predecessors().get(0));
                }
            } else {
                PlaceList candidate = new PlaceList();
                for (Place Finding : end.predecessors()) {
                    if (model.get(Finding).predecessor() == null) {
                        if (model.get(Finding).connectable(end)) {
                            candidate.add(Finding);
                        }
                    }
                }

                if (candidate.size() == 1) {
                    return model.get(candidate.get(0));
                }
            }
        }
        return null;
    }


    /** Remove all links in MODEL and unfix numbers (other than the first and
     *  last) that do not affect solvability.  Not all such numbers are
     *  necessarily removed. */
    private void trimFixed(Model model) {
        int w = model.width(), h = model.height();
        boolean changed;
        do {
            changed = false;
            for (Sq sq : model) {
                if (sq.hasFixedNum() && sq.sequenceNum() != 1
                        && sq.direction() != 0) {
                    model.restart();
                    int n = sq.sequenceNum();
                    sq.unfixNum();
                    extendSimple(model);
                    if (model.solved()) {
                        changed = true;
                    } else {
                        sq.setFixedNum(n);
                    }
                }
            }
        } while (changed);
    }

    /** Fix additional numbers in MODEL to make the solution from which
     *  it was formed unique.  Need not result in a minimal set of
     *  fixed numbers. */
    private void makeSolutionUnique(Model model) {
        model.restart();
        AddNum:
        while (true) {
            extendSimple(model);
            if (model.solved()) {
                trimFixed(model);
                model.restart();
                return;
            }
            PlaceList unnumbered = new PlaceList();
            for (Sq sq : model) {
                if (sq.sequenceNum() == 0) {
                    unnumbered.add(sq.pl);
                }
            }
            Collections.shuffle(unnumbered, _random);
            for (Place p : unnumbered) {
                Model model1 = new Model(model);
                model1.get(p).setFixedNum(model.solution()[p.x][p.y]);
                if (extendSimple(model1)) {
                    model.get(p).setFixedNum(model1.get(p).sequenceNum());
                    continue AddNum;
                }
            }
            throw badArgs("no solution found");
        }
    }

    @Override
    public void setSeed(long seed) {
        _random.setSeed(seed);
    }

    /** Solution board currently being filled in by findSolutionPathFrom. */
    private int[][] _vals;
    /** Mapping of positions and directions to lists of queen moves on _vals. */
    private PlaceList[][][] _successorCells;

    /** My PNRG. */
    private Random _random;

}


/*package signpost;

import java.util.Collections;
import java.util.Random;

import signpost.Model.Sq;
import static signpost.Place.PlaceList;
import static signpost.Utils.*;

/** A creator of random Signpost puzzles.
 *  @author

class PuzzleGenerator implements PuzzleSource {

    /**
     * A new PuzzleGenerator whose random-number source is seeded
     * with SEED.
     //add comment line
    PuzzleGenerator(long seed) {
        _random = new Random(seed);
    }

    @Override
    public Model getPuzzle(int width, int height, boolean allowFreeEnds) {
        Model model =
                new Model(makePuzzleSolution(width, height, allowFreeEnds));
        // FIXME: Remove the "//" on the following two lines.
        // makeSolutionUnique(model);
        // model.autoconnect();
        return model;
    }

    /**
     * Return an array representing a WIDTH x HEIGHT Signpost puzzle.
     * The first array index indicates x-coordinates (column numbers) on
     * the board, and the second index represents y-coordinates (row numbers).
     * Its values will be the sequence numbers (1 to WIDTH x HEIGHT)
     * appearing in a sequence queen moves on the resulting board.
     * Unless ALLOWFREEENDS, the first and last sequence numbers will
     * appear in the upper-left and lower-right corners, respectively.

    private int[][] makePuzzleSolution(int width, int height,
                                       boolean allowFreeEnds) {
        _vals = new int[width][height];
        _successorCells = Place.successorCells(width, height);
        int last = width * height;
        int x0, y0, x1, y1;
        if (allowFreeEnds) {
            int r0 = _random.nextInt(last),
                    r1 = (r0 + 1 + _random.nextInt(last - 1)) % last;
            x0 = r0 / height;
            y0 = r0 % height;
            x1 = r1 / height;
            y1 = r1 % height;
        } else {
            x0 = 0;
            y0 = height - 1;
            x1 = width - 1;
            y1 = 0;
        }
        _vals[x0][y0] = 1;
        _vals[x1][y1] = last;
        // FIXME: Remove the following return statement and uncomment the
        //        next three lines.
       /* return new int[][] {
            { 14, 9, 8, 1 },
            { 15, 10, 7, 2 },
            { 13, 11, 6, 3 },
            { 16, 12, 5, 4 }
        };


        boolean ok = findSolutionPathFrom(x0, y0);
        assert ok;
        return _vals;
    }

    /**
     * Try to find a random path of queen moves through VALS from (X0, Y0)
     * to the cell with number LAST.  Assumes that
     * + The dimensions of VALS conforms to those of MODEL;
     * + There are cells (separated by queen moves) numbered from 1 up to
     * and including the number in (X0, Y0);
     * + There is a cell numbered LAST;
     * + All other cells in VALS contain 0.
     * Does not change the contents of any non-zero cell in VALS.
     * Returns true and leaves the path that is found in VALS.  Otherwise
     * returns false and leaves VALS unchanged. Does not change MODEL.

    private boolean findSolutionPathFrom(int x0, int y0) {
        int w = _vals.length, h = _vals[0].length;
        int v;
        int start = _vals[x0][y0] + 1;
        PlaceList moves = _successorCells[x0][y0][0];
        Collections.shuffle(moves, _random);
        for (Place p : moves) {
            v = _vals[p.x][p.y];
            if (v == 0) {
                _vals[p.x][p.y] = start;
                if (findSolutionPathFrom(p.x, p.y)) {
                    return true;
                }
                _vals[p.x][p.y] = 0;
            } else if (v == start && start == w * h) {
                return true;
            }
        }
        return false;
    }

    /**
     * Extend unambiguous paths in MODEL (add all connections where there is
     * a single possible successor or predecessor). Return true iff any change
     * was made.

    static boolean extendSimple(Model model) {
        boolean found;
        found = false;
        while (makeForwardConnections(model)
                || makeBackwardConnections(model)) {
            found = true;
        }
        return found;
    }

    /**
     * Make all unique forward connections in MODEL (those in which there is
     * a single possible successor).  Return true iff changes were made.

    static boolean makeForwardConnections(Model model) {
        int w = model.width(), h = model.height();
        boolean result;
        result = false;
        for (Sq sq : model) {
            if (sq.successor() == null && sq.direction() != 0) {
                Sq found = findUniqueSuccessor(model, sq);
                if (found != null) {
                    sq.connect(found);
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * Return the unique square in MODEL to which unconnected square START
     * can connect, or null if there isn't such a unique square. The unique
     * square is either (1) the only connectable square in the proper
     * direction from START, or (2) if START is numbered, a connectable
     * numbered square in the proper direction from START (with the next
     * number in sequence).

    static Sq findUniqueSuccessor(Model model, Sq start) {
        // FIXME: Fill in to satisfy the comment.
        PlaceList candidate = start.successors();

        if (start.sequenceNum() == 0) {
            PlaceList pres = new PlaceList();

            for (Place can : candidate) {
                if (Place.dirOf(start.x, start.y, can.x, can.y) != 0) {
                    if (model.get(can).predecessor() == null) {
                        pres.add(can);
                    }
                }
            }

            if (pres.size() == 1) {
                return model.get(pres.get(0));
            } else {
                return null;
            }

        } else {
            PlaceList pres = new PlaceList();

            for (Place can : candidate) {
                if (Place.dirOf(start.x, start.y, can.x, can.y) != 0) {
                    if (model.get(can).predecessor() == null) {
                        pres.add(can);
                    }
                }
            }

            if (pres.size() == 1) {
                return model.get(pres.get(0));
            } else {
                return null;
            }

        }
    }

    static Sq findUniqueSuccessorCOPY(Model model, Sq start) {

        PlaceList[][][] places = Place.successorCells(model.width(), model.height());

        PlaceList candidates = new PlaceList();
        PlaceList select = new PlaceList();

        for (int i = 0; i < places[start.x][start.y][start.direction()].size(); i += 1) {
            Place temp = places[start.x][start.y][start.direction()].get(i);

            if (start.connectable(model.get(temp.x, temp.y))) {
                candidates.add(temp);
            }
        }

        if (candidates.size() == 1) {
            return model.get(candidates.get(0).x, candidates.get(0).y);
        } else {

            for (Place candidate : candidates) {
                if (model.get(candidate.x, candidate.y).sequenceNum() - 1 == start.sequenceNum()) {
                    select.add(candidate);
                }
            }

            if (select.size() != 1) {
                return null;
            } else {
                return model.get(select.get(0).x, select.get(0).y);
            }
        }
    }

    /**
     * Make all unique backward connections in MODEL (those in which there is
     * a single possible predecessor).  Return true iff changes made.

    static boolean makeBackwardConnections(Model model) {
        int w = model.width(), h = model.height();
        boolean result;
        result = false;
        for (Sq sq : model) {
            if (sq.predecessor() == null && sq.sequenceNum() != 1) {
                Sq found = findUniquePredecessor(model, sq);
                if (found != null) {
                    found.connect(sq);
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * Return the unique square in MODEL that can connect to unconnected
     * square END, or null if there isn't such a unique square.
     * This function does not handle the case in which END and one of its
     * predecessors is numbered, except when the numbered predecessor is
     * the only unconnected predecessor.  This is because findUniqueSuccessor
     * already finds the other cases of numbered, unconnected cells.

    static Sq findUniquePredecessor(Model model, Sq end) {
        // FIXME: Replace the following to satisfy the comment.
        if (end.predecessors() != null) {
            if (end.predecessors().size() == 1) {
                if (model.get(end.predecessors().get(0)).connectable(end)) {
                    return model.get(end.predecessors().get(0));
                }
            } else {
                PlaceList candidate = new PlaceList();
                for (Place search : end.predecessors()) {
                    if (model.get(search).predecessor() == null) {
                        if (model.get(search).connectable(end)) {
                            candidate.add(search);
                        }
                    }
                }

                if (candidate.size() == 1) {
                    return model.get(candidate.get(0));
                }
            }
        }
        return null;
    }




    /** Remove all links in MODEL and unfix numbers (other than the first and
     *  last) that do not affect solvability.  Not all such numbers are
     *  necessarily removed.
    private void trimFixed(Model model) {
        int w = model.width(), h = model.height();
        boolean changed;
        do {
            changed = false;
            for (Sq sq : model) {
                if (sq.hasFixedNum() && sq.sequenceNum() != 1
                    && sq.direction() != 0) {
                    model.restart();
                    int n = sq.sequenceNum();
                    sq.unfixNum();
                    extendSimple(model);
                    if (model.solved()) {
                        changed = true;
                    } else {
                        sq.setFixedNum(n);
                    }
                }
            }
        } while (changed);
    }

    /** Fix additional numbers in MODEL to make the solution from which
     *  it was formed unique.  Need not result in a minimal set of
     *  fixed numbers.
    private void makeSolutionUnique(Model model) {
        model.restart();
        AddNum:
        while (true) {
            extendSimple(model);
            if (model.solved()) {
                trimFixed(model);
                model.restart();
                return;
            }
            PlaceList unnumbered = new PlaceList();
            for (Sq sq : model) {
                if (sq.sequenceNum() == 0) {
                    unnumbered.add(sq.pl);
                }
            }
            Collections.shuffle(unnumbered, _random);
            for (Place p : unnumbered) {
                Model model1 = new Model(model);
                model1.get(p).setFixedNum(model.solution()[p.x][p.y]);
                if (extendSimple(model1)) {
                    model.get(p).setFixedNum(model1.get(p).sequenceNum());
                    continue AddNum;
                }
            }
            throw badArgs("no solution found");
        }
    }

    @Override
    public void setSeed(long seed) {
        _random.setSeed(seed);
    }

    /** Solution board currently being filled in by findSolutionPathFrom.
    private int[][] _vals;
    /** Mapping of positions and directions to lists of queen moves on _vals.
    private PlaceList[][][] _successorCells;

    /** My PNRG.
    private Random _random;

}
*/