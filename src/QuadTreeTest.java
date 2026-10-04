
// -------------------------------------------------------------------------
import student.TestCase;

/**
 * Write a one-sentence summary of your class here. Follow it with additional
 * details about its purpose, what abstraction it represents, and how to use it.
 * 
 * @author Saanvi
 * @version Oct 3, 2026
 */
public class QuadTreeTest extends TestCase
{
    private QuadTree tree;

    /**
     * Creates an empty tree before each test.
     */
    public void setUp()
    {
        tree = new QuadTree();
    }


    /**
     * Tests creation of an empty Quadtree.
     */
    public void testQuadTree()
    {
        assertNotNull(tree);
        assertNotNull(tree.getRoot());
        assertTrue(tree.getRoot() instanceof EmptyNode);
    }
}
