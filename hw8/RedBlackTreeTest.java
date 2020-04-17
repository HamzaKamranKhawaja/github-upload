import org.junit.Test;
import static org.junit.Assert.*;

public class RedBlackTreeTest {



    @Test
    public void insertTest() {
        RedBlackTree<Integer> Tree = new RedBlackTree<>();
        Tree.insert(6);
        Tree.insert(10);
        Tree.insert(2);

        boolean isBlackRoot = Tree.graderRoot().isBlack;
        assertTrue(isBlackRoot);
        assertEquals(6, Tree.graderRoot().item.intValue() );
        assertEquals(Tree.graderRoot().left.item.intValue(),2 );
        assertEquals(Tree.graderRoot().right.item.intValue(),10 );
    }














}

