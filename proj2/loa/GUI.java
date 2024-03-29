/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import ucb.gui2.TopLevel;
import ucb.gui2.LayoutSpec;

import java.awt.Dimension;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.io.InputStream;
import java.io.IOException;
import java.io.StringWriter;

import java.util.concurrent.ArrayBlockingQueue;

import static loa.Piece.*;

/** The GUI controller for a LOA board and buttons.
 *  @author Hamza Kamran
 */
class GUI extends TopLevel implements View, Reporter {

    /** Minimum size of board in pixels. */
    private static final int MIN_SIZE = 500;

    /** Size of pane used to contain help text. */
    static final Dimension TEXT_BOX_SIZE = new Dimension(500, 700);

    /** Resource name of "About" message. */
    static final String ABOUT_TEXT = "loa/About.html";

    /** Resource name of Loa help text. */
    static final String HELP_TEXT = "loa/Help.html";

    /** A new window with given TITLE providing a view of a Loa board. */
    GUI(String title) {
        super(title, true);
        addMenuButton("Game->New", this::newGame);
        addMenuButton("Game->Quit", this::quit);
        addMenuButton("help->How to play?", this::help);
        addMenuButton("undo->UNDO PREVIOUS MOVE", this::undo);
        addMenuButton("switch-> white to auto", this::autowhite);
        addMenuButton("switch-> black to auto", this::autoblack);
        addMenuButton("switch-> white to manual", this::manualwhite);
        addMenuButton("switch-> black to manual", this::manualblack);

        _widget = new BoardWidget(_pendingCommands);
        add(_widget,
            new LayoutSpec("y", 1,
                           "height", 1,
                           "width", 3));
        addLabel("To move: White", "CurrentTurn",
                 new LayoutSpec("x", 0, "y", 0,
                                "height", 1,
                                "width", 3));
    }

    /** Response to "Quit" button click. */
    private void quit(String dummy) {
        _pendingCommands.offer("quit");
    }

    /** Response to "New Game" button click. */
    private void newGame(String dummy) {
        _pendingCommands.offer("new");
    }

    /** Response to "help" button click. */
    private void help(String dummy) {
        displayText("About", HELP_TEXT);
    }

    /** Response to "help" button click. */

    private void undo(String dummy) {
        _pendingCommands.add("undo");
    }
    /** Response to "autowhite" button click. */

    private void autowhite(String dummy) {
        _pendingCommands.add("autowhite");
    }

    /** Response to "autoblack" button click. */
    private void autoblack(String dummy) {
        _pendingCommands.add("autoblack");
    }

    /** Response to "manwhite" button click. */
    private void manualwhite(String dummy) {
        _pendingCommands.add("manualwhite");
    }

    /** Response to "manblack" button click. */
    private void manualblack(String dummy) {
        _pendingCommands.add("manualblack");
    }

    /** Return the next command from our widget, waiting for it as necessary.
     *  The BoardWidget uses _pendingCommands to queue up moves that it
     *  receives.  This class uses _pendingCommands to queue up commands that
     *  are generated by clicking on menu items. */
    String readCommand() {
        try {
            _widget.setMoveCollection(true);
            String cmnd = _pendingCommands.take();
            _widget.setMoveCollection(false);
            return cmnd;
        } catch (InterruptedException excp) {
            throw new Error("unexpected interrupt");
        }
    }

    @Override
    public void update(Game controller) {
        Board board = controller.getBoard();

        _widget.update(board);
        if (board.winner() != null) {
            setLabel("CurrentTurn",
                     String.format("Winner: %s",
                                   board.winner().fullName()));
        } else {
            setLabel("CurrentTurn",
                     String.format("To move: %s", board.turn().fullName()));
        }

        boolean manualWhite = controller.manualWhite(),
            manualBlack = controller.manualBlack();
    }

    /** Display text in resource named TEXTRESOURCE in a new window titled
     *  TITLE. */
    private void displayText(String title, String textResource) {
        /* Implementation note: It would have been more convenient to avoid
         * having to read the resource and simply use dispPane.setPage on the
         * resource's URL.  However, we wanted to use this application with
         * a nonstandard ClassLoader, and arranging for straight Java to
         * understand non-standard URLS that access such a ClassLoader turns
         * out to be a bit more trouble than it's worth. */
        JFrame frame = new JFrame(title);
        JEditorPane dispPane = new JEditorPane();
        dispPane.setEditable(false);
        dispPane.setContentType("text/html");
        InputStream resource =
            GUI.class.getClassLoader().getResourceAsStream(textResource);
        StringWriter text = new StringWriter();
        try {
            while (true) {
                int c = resource.read();
                if (c < 0) {
                    dispPane.setText(text.toString());
                    break;
                }
                text.write(c);
            }
        } catch (IOException e) {
            return;
        }
        JScrollPane scroller = new JScrollPane(dispPane);
        scroller.setVerticalScrollBarPolicy(scroller.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setPreferredSize(TEXT_BOX_SIZE);
        frame.add(scroller);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void reportError(String fmt, Object... args) {
        showMessage(String.format(fmt, args), "Loa Error", "error");
    }

    @Override
    public void reportNote(String fmt, Object... args) {
        showMessage(String.format(fmt, args), "Loa Message", "information");
    }

    @Override
    public void reportMove(Move unused) {
    }

    /** The board widget. */
    private BoardWidget _widget;

    /** Queue of pending commands resulting from menu clicks and moves on the
     *  board.  We use a blocking queue because the responses to clicks
     *  on the board and on menus happen in parallel to the methods that
     *  call readCommand, which therefore needs to wait for clicks to happen. */
    private ArrayBlockingQueue<String> _pendingCommands =
        new ArrayBlockingQueue<>(5);

}
