
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


    /**
     * Tests region search with one matching point.
     */
    public void testRegionSearchFound()
    {
        tree.insert(new Point("a", 100, 100));
        tree.insert(new Point("b", 500, 500));

        tree.regionSearch(50, 50, 100, 100);

        assertTrue(
            systemOut().getHistory().contains("Point found: (a, 100, 100)"));

        assertFalse(
            systemOut().getHistory().contains("Point found: (b, 500, 500)"));
    }


    /**
     * Tests region search with no matching points.
     */
    public void testRegionSearchEmpty()
    {
        tree.insert(new Point("a", 100, 100));

        tree.regionSearch(500, 500, 50, 50);

        assertFalse(systemOut().getHistory().contains("Point found:"));
    }


    /**
     * Tests rejecting a rectangle with invalid width.
     */
    public void testInvalidRegionWidth()
    {
        tree.regionSearch(5, 5, -2, 10);

        assertTrue(
            systemOut().getHistory()
                .contains("Rectangle rejected: (5, 5, -2, 10)"));
    }


    /**
     * Tests rejecting a rectangle with invalid height.
     */
    public void testInvalidRegionHeight()
    {
        tree.regionSearch(5, 5, 10, 0);

        assertTrue(
            systemOut().getHistory()
                .contains("Rectangle rejected: (5, 5, 10, 0)"));
    }


    /**
     * Tests searching an empty Quadtree.
     */
    public void testRegionSearchEmptyTree()
    {
        tree.regionSearch(0, 0, 100, 100);

        assertTrue(
            systemOut().getHistory().contains("1 quadtree nodes visited"));
    }


    /**
     * Tests finding multiple points in a region.
     */
    public void testRegionSearchMultiple()
    {
        tree.insert(new Point("a", 10, 10));
        tree.insert(new Point("b", 20, 20));
        tree.insert(new Point("c", 500, 500));

        tree.regionSearch(0, 0, 100, 100);

        assertTrue(
            systemOut().getHistory().contains("Point found: (a, 10, 10)"));

        assertTrue(
            systemOut().getHistory().contains("Point found: (b, 20, 20)"));

        assertFalse(
            systemOut().getHistory().contains("Point found: (c, 500, 500)"));
    }


    /**
     * Tests identifying duplicate coordinates.
     */
    public void testDuplicates()
    {
        tree.insert(new Point("a", 10, 20));
        tree.insert(new Point("b", 10, 20));
        tree.insert(new Point("c", 30, 40));

        tree.duplicates();

        assertTrue(systemOut().getHistory().contains("Duplicate points:"));

        assertTrue(systemOut().getHistory().contains("(10, 20)"));
    }


    /**
     * Tests removing a point by coordinates.
     */
    public void testRemoveCoordinates()
    {
        tree.insert(new Point("a", 100, 100));

        Point removed = tree.remove(100, 100);

        assertNotNull(removed);
        assertEquals("a", removed.getName());
        assertTrue(tree.getRoot() instanceof EmptyNode);
    }


    /**
     * Tests removing a missing point.
     */
    public void testRemoveMissing()
    {
        tree.insert(new Point("a", 100, 100));

        Point removed = tree.remove(200, 200);

        assertNull(removed);
        assertTrue(tree.getRoot() instanceof LeafNode);
    }


    /**
     * Tests removing a specific point.
     */
    public void testRemovePoint()
    {
        Point first = new Point("a", 100, 100);
        Point second = new Point("b", 100, 100);

        tree.insert(first);
        tree.insert(second);

        assertTrue(tree.remove(first));

        LeafNode leaf = (LeafNode)tree.getRoot();

        assertEquals(1, leaf.getPoints().size());
        assertSame(second, leaf.getPoints().get(0));
    }


    /**
     * Tests collapsing after removal.
     */
    public void testCollapseAfterRemoval()
    {
        tree.insert(new Point("a", 100, 100));
        tree.insert(new Point("b", 600, 100));
        tree.insert(new Point("c", 100, 600));
        tree.insert(new Point("d", 600, 600));

        assertTrue(tree.getRoot() instanceof InternalNode);

        tree.remove(600, 600);

        assertTrue(tree.getRoot() instanceof LeafNode);

        LeafNode leaf = (LeafNode)tree.getRoot();

        assertEquals(3, leaf.getPoints().size());
    }


    /**
     * Tests removing all points.
     */
    public void testRemoveAll()
    {
        tree.insert(new Point("a", 100, 100));
        tree.insert(new Point("b", 600, 600));

        tree.remove(100, 100);
        tree.remove(600, 600);

        assertSame(EmptyNode.getInstance(), tree.getRoot());
    }


    /**
     * Tests removing a point from a split tree.
     */
    public void testRemoveFromInternal()
    {
        tree.insert(new Point("a", 100, 100));
        tree.insert(new Point("b", 200, 200));
        tree.insert(new Point("c", 300, 300));
        tree.insert(new Point("d", 600, 600));
        tree.insert(new Point("e", 700, 700));

        assertTrue(tree.getRoot() instanceof InternalNode);

        Point removed = tree.remove(700, 700);

        assertNotNull(removed);
        assertEquals("e", removed.getName());

        assertTrue(tree.getRoot() instanceof InternalNode);
    }

}
