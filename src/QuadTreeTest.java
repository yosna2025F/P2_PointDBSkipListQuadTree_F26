
// -------------------------------------------------------------------------
import student.TestCase;

/**
 * Tests the QuadTree class
 * 
 * @author Saanvi
 * @version Oct 3, 2026
 */
public class QuadTreeTest
    extends TestCase
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


    /**
     * Tests insertion into empty tree.
     */
    public void testInsertEmptyTree()
    {
        tree.insert(new Point("a", 100, 100));

        assertTrue(tree.getRoot() instanceof LeafNode);

        LeafNode leaf = (LeafNode)tree.getRoot();
        assertEquals(1, leaf.getPoints().size());
    }


    /**
     * Tests inserting three points
     */
    public void testInsertThreePoints()
    {
        tree.insert(new Point("a", 100, 100));
        tree.insert(new Point("b", 200, 200));
        tree.insert(new Point("c", 300, 300));

        assertTrue(tree.getRoot() instanceof LeafNode);

        LeafNode leaf = (LeafNode)tree.getRoot();
        assertEquals(3, leaf.getPoints().size());
    }


    /**
     * Tests splitting when inserting four points.
     */
    public void testSplitFourPoints()
    {
        tree.insert(new Point("a", 100, 100));
        tree.insert(new Point("b", 600, 100));
        tree.insert(new Point("c", 100, 600));
        tree.insert(new Point("d", 600, 600));

        assertTrue(tree.getRoot() instanceof InternalNode);

        InternalNode root = (InternalNode)tree.getRoot();

        assertTrue(root.getNorthwest() instanceof LeafNode);
        assertTrue(root.getNortheast() instanceof LeafNode);
        assertTrue(root.getSouthwest() instanceof LeafNode);
        assertTrue(root.getSoutheast() instanceof LeafNode);
    }


    /**
     * Tests inserting multiple points at the same location.
     */
    public void testDuplicateCoordinates()
    {
        for (int i = 0; i < 5; i++)
        {
            tree.insert(new Point("p" + i, 100, 100));
        }

        assertTrue(tree.getRoot() instanceof LeafNode);

        LeafNode leaf = (LeafNode)tree.getRoot();
        assertEquals(5, leaf.getPoints().size());
    }


    /**
     * Tests recursive splitting when all points initially belong to the same
     * quadrant.
     */
    public void testRecursiveSplit()
    {
        tree.insert(new Point("a", 10, 10));
        tree.insert(new Point("b", 20, 20));
        tree.insert(new Point("c", 30, 30));
        tree.insert(new Point("d", 40, 40));

        assertTrue(tree.getRoot() instanceof InternalNode);

        InternalNode root = (InternalNode)tree.getRoot();

        assertTrue(root.getNorthwest() instanceof InternalNode);
    }


    /**
     * Tests rejecting points outside the world boundaries.
     */
    public void testInvalidCoordinates()
    {
        tree.insert(new Point("a", -1, 100));
        tree.insert(new Point("b", 1024, 100));
        tree.insert(new Point("c", 100, -1));
        tree.insert(new Point("d", 100, 1024));

        assertTrue(tree.getRoot() instanceof EmptyNode);
    }


    /**
     * Tests inserting points at valid boundary coordinates.
     */
    public void testBoundaryCoordinates()
    {
        tree.insert(new Point("a", 0, 0));
        tree.insert(new Point("b", 1023, 1023));

        assertTrue(tree.getRoot() instanceof LeafNode);

        LeafNode leaf = (LeafNode)tree.getRoot();
        assertEquals(2, leaf.getPoints().size());
    }
}
