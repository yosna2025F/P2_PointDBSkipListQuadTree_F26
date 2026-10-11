
// -------------------------------------------------------------------------
import student.TestCase;

/**
 * Tests the LeafNode class.
 * 
 * @author Saanvi
 * @version Oct 3, 2026
 */
public class LeafNodeTest
    extends TestCase
{
    private LeafNode leaf;

    /**
     * Sets up a LeafNode before each test.
     */
    public void setUp()
    {
        leaf = new LeafNode();
    }


    /**
     * Tests that a LeafNode can be created.
     */
    public void testLeafNode()
    {
        assertNotNull(leaf);
        assertEquals(0, leaf.getPoints().size());
    }


    /**
     * Tests inserting a single point into a leaf.
     */
    public void testInsertOnePoint()
    {
        Point point = new Point("a", 100, 100);

        QuadNode result = leaf.insert(point, 0, 0, 1024);

        assertSame(leaf, result);
        assertEquals(1, leaf.getPoints().size());
        assertSame(point, leaf.getPoints().get(0));
    }


    /**
     * Tests inserting three points without splitting.
     */
    public void testInsertThreePoints()
    {
        leaf.insert(new Point("a", 100, 100), 0, 0, 1024);
        leaf.insert(new Point("b", 200, 200), 0, 0, 1024);

        QuadNode result = leaf.insert(new Point("c", 300, 300), 0, 0, 1024);

        assertSame(leaf, result);
        assertEquals(3, leaf.getPoints().size());
    }


    /**
     * Tests splitting a leaf with four different points.
     */
    public void testLeafSplit()
    {
        leaf.insert(new Point("a", 100, 100), 0, 0, 1024);
        leaf.insert(new Point("b", 600, 100), 0, 0, 1024);
        leaf.insert(new Point("c", 100, 600), 0, 0, 1024);

        QuadNode result = leaf.insert(new Point("d", 600, 600), 0, 0, 1024);

        assertTrue(result instanceof InternalNode);
    }


    /**
     * Tests that duplicate coordinates do not cause splitting.
     */
    public void testSameCoordinates()
    {
        for (int i = 0; i < 5; i++)
        {
            leaf.insert(new Point("p" + i, 100, 100), 0, 0, 1024);
        }

        assertEquals(5, leaf.getPoints().size());
        assertTrue(leaf.allSameCoordinates());
    }


    /**
     * Tests identifying different coordinates.
     */
    public void testDifferentCoordinates()
    {
        leaf.insert(new Point("a", 100, 100), 0, 0, 1024);
        leaf.insert(new Point("b", 200, 200), 0, 0, 1024);

        assertFalse(leaf.allSameCoordinates());
    }


    /**
     * Tests that points with the same location but different names remain in
     * one leaf.
     */
    public void testDuplicateNamesDifferentCoordinates()
    {
        leaf.insert(new Point("a", 100, 100), 0, 0, 1024);
        leaf.insert(new Point("a", 200, 200), 0, 0, 1024);

        assertEquals(2, leaf.getPoints().size());
        assertFalse(leaf.allSameCoordinates());
    }
}
