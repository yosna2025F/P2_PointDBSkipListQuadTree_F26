
import student.TestCase;

/**
 * Tests the QuadTree class.
 *
 * @author Saanvi
 * @version Oct 25, 2026
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
     * Tests an empty tree and invalid insertions.
     */
    public void testEmptyAndInvalidInsert()
    {
        assertSame(EmptyNode.getInstance(), tree.getRoot());

        tree.insert(null);
        tree.insert(new Point("a", -1, 100));
        tree.insert(new Point("b", 1024, 100));
        tree.insert(new Point("c", 100, -1));
        tree.insert(new Point("d", 100, 1024));

        assertSame(EmptyNode.getInstance(), tree.getRoot());

        tree.dump();

        assertTrue(
            systemOut().getHistory().contains("Node at 0, 0, 1024: Empty"));
        assertTrue(
            systemOut().getHistory().contains("1 quadtree nodes printed"));
    }


    /**
     * Tests insertion limits and valid world boundaries.
     */
    public void testInsertLeaf()
    {
        Point a = new Point("a", 0, 0);
        Point b = new Point("b", 1023, 1023);
        Point c = new Point("c", 500, 500);

        tree.insert(a);
        tree.insert(b);
        tree.insert(c);

        assertTrue(tree.getRoot() instanceof LeafNode);

        LeafNode leaf = (LeafNode)tree.getRoot();

        assertEquals(3, leaf.getPoints().size());
        assertTrue(leaf.getPoints().contains(a));
        assertTrue(leaf.getPoints().contains(b));
        assertTrue(leaf.getPoints().contains(c));
    }


    /**
     * Tests splitting and correct quadrant placement.
     */
    public void testSplitFourQuadrants()
    {
        Point a = new Point("a", 100, 100);
        Point b = new Point("b", 600, 100);
        Point c = new Point("c", 100, 600);
        Point d = new Point("d", 600, 600);

        tree.insert(a);
        tree.insert(b);
        tree.insert(c);
        tree.insert(d);

        assertTrue(tree.getRoot() instanceof InternalNode);

        InternalNode root = (InternalNode)tree.getRoot();

        assertSame(a, ((LeafNode)root.getNorthwest()).getPoints().get(0));
        assertSame(b, ((LeafNode)root.getNortheast()).getPoints().get(0));
        assertSame(c, ((LeafNode)root.getSouthwest()).getPoints().get(0));
        assertSame(d, ((LeafNode)root.getSoutheast()).getPoints().get(0));

        tree.dump();

        String output = systemOut().getHistory();

        assertTrue(output.contains("5 quadtree nodes printed"));
        assertTrue(output.contains("Node at 0, 0, 1024: Internal"));
        assertTrue(output.contains("Node at 512, 512, 512:"));
    }


    /**
     * Tests recursive splitting within one quadrant.
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

        tree.dump();

        String output = systemOut().getHistory();

        assertTrue(output.contains("Node at 0, 0, 512: Internal"));
        assertTrue(output.contains("Node at 0, 0, 256: Internal"));
        assertTrue(output.contains("Node at 0, 0, 128: Internal"));
    }


    /**
     * Tests duplicate coordinates and duplicate output.
     */
    public void testDuplicates()
    {
        for (int i = 0; i < 5; i++)
        {
            tree.insert(new Point("p" + i, 100, 100));
        }

        assertTrue(tree.getRoot() instanceof LeafNode);
        assertEquals(5, ((LeafNode)tree.getRoot()).getPoints().size());

        tree.insert(new Point("a", 900, 900));
        tree.insert(new Point("b", 900, 900));

        tree.duplicates();

        String output = systemOut().getHistory();

        assertTrue(output.contains("(100, 100)"));
        assertTrue(output.contains("(900, 900)"));

        assertEquals(1, countOccurrences(output, "(100, 100)"));
        assertEquals(1, countOccurrences(output, "(900, 900)"));
    }


    /**
     * Tests region search boundaries and excluded points.
     */
    public void testRegionSearchBoundaries()
    {
        tree.insert(new Point("a", 100, 100));
        tree.insert(new Point("b", 199, 199));
        tree.insert(new Point("c", 200, 150));
        tree.insert(new Point("d", 150, 200));

        tree.regionSearch(100, 100, 100, 100);

        String output = systemOut().getHistory();

        assertTrue(output.contains("Point found: (a, 100, 100)"));
        assertTrue(output.contains("Point found: (b, 199, 199)"));
        assertFalse(output.contains("Point found: (c, 200, 150)"));
        assertFalse(output.contains("Point found: (d, 150, 200)"));
    }


    /**
     * Tests traversal of one quadrant and the entire tree.
     */
    public void testRegionSearchTraversal()
    {
        tree.insert(new Point("nw", 100, 100));
        tree.insert(new Point("ne", 900, 100));
        tree.insert(new Point("sw", 100, 900));
        tree.insert(new Point("se", 900, 900));

        tree.regionSearch(0, 0, 512, 512);

        String output = systemOut().getHistory();

        assertTrue(output.contains("Point found: (nw, 100, 100)"));
        assertFalse(output.contains("Point found: (ne, 900, 100)"));
        assertFalse(output.contains("Point found: (sw, 100, 900)"));
        assertFalse(output.contains("Point found: (se, 900, 900)"));
        assertTrue(output.contains("2 quadtree nodes visited"));

        systemOut().clearHistory();

        tree.regionSearch(0, 0, 1024, 1024);

        output = systemOut().getHistory();

        assertTrue(output.contains("Point found: (nw, 100, 100)"));
        assertTrue(output.contains("Point found: (ne, 900, 100)"));
        assertTrue(output.contains("Point found: (sw, 100, 900)"));
        assertTrue(output.contains("Point found: (se, 900, 900)"));
        assertTrue(output.contains("5 quadtree nodes visited"));
    }


    /**
     * Tests invalid rectangles and an empty region search.
     */
    public void testInvalidRegionSearch()
    {
        tree.regionSearch(5, 5, -2, 10);
        tree.regionSearch(5, 5, 10, 0);

        String output = systemOut().getHistory();

        assertTrue(output.contains("Rectangle rejected: (5, 5, -2, 10)"));
        assertTrue(output.contains("Rectangle rejected: (5, 5, 10, 0)"));

        systemOut().clearHistory();

        tree.regionSearch(0, 0, 100, 100);

        assertTrue(
            systemOut().getHistory().contains("1 quadtree nodes visited"));
    }


    /**
     * Tests removal by point and by coordinates.
     */
    public void testRemoveFromLeaf()
    {
        Point a = new Point("a", 100, 100);
        Point b = new Point("b", 200, 200);

        tree.insert(a);
        tree.insert(b);

        assertTrue(tree.remove(a));

        LeafNode leaf = (LeafNode)tree.getRoot();

        assertEquals(1, leaf.getPoints().size());
        assertSame(b, leaf.getPoints().get(0));

        assertSame(b, tree.remove(200, 200));
        assertSame(EmptyNode.getInstance(), tree.getRoot());
    }


    /**
     * Tests invalid and unsuccessful removals.
     */
    public void testRemoveMissing()
    {
        Point a = new Point("a", 100, 100);
        Point b = new Point("b", 200, 200);

        tree.insert(a);

        assertFalse(tree.remove((Point)null));
        assertFalse(tree.remove(b));

        assertNull(tree.remove(-1, 100));
        assertNull(tree.remove(1024, 100));
        assertNull(tree.remove(100, -1));
        assertNull(tree.remove(100, 1024));
        assertNull(tree.remove(200, 200));

        LeafNode leaf = (LeafNode)tree.getRoot();

        assertEquals(1, leaf.getPoints().size());
        assertSame(a, leaf.getPoints().get(0));
    }


    /**
     * Tests removing one point from duplicate coordinates.
     */
    public void testRemoveDuplicate()
    {
        Point a = new Point("a", 100, 100);
        Point b = new Point("b", 100, 100);
        Point c = new Point("c", 100, 100);

        tree.insert(a);
        tree.insert(b);
        tree.insert(c);

        assertTrue(tree.remove(b));

        LeafNode leaf = (LeafNode)tree.getRoot();

        assertEquals(2, leaf.getPoints().size());
        assertTrue(leaf.getPoints().contains(a));
        assertFalse(leaf.getPoints().contains(b));
        assertTrue(leaf.getPoints().contains(c));

        tree.duplicates();

        assertTrue(systemOut().getHistory().contains("(100, 100)"));
    }


    /**
     * Tests collapsing after removal and removing all points.
     */
    public void testRemoveAndCollapse()
    {
        Point a = new Point("a", 100, 100);
        Point b = new Point("b", 600, 100);
        Point c = new Point("c", 100, 600);
        Point d = new Point("d", 600, 600);
        Point e = new Point("e", 200, 200);

        tree.insert(a);
        tree.insert(b);
        tree.insert(c);
        tree.insert(d);
        tree.insert(e);

        assertTrue(tree.getRoot() instanceof InternalNode);

        assertSame(e, tree.remove(200, 200));

        // Four points remain, so the root stays internal.
        assertTrue(tree.getRoot() instanceof InternalNode);

        assertTrue(tree.remove(d));

        // Three points remain, so the root collapses.
        assertTrue(tree.getRoot() instanceof LeafNode);

        LeafNode leaf = (LeafNode)tree.getRoot();

        assertEquals(3, leaf.getPoints().size());
        assertTrue(leaf.getPoints().contains(a));
        assertTrue(leaf.getPoints().contains(b));
        assertTrue(leaf.getPoints().contains(c));

        assertTrue(tree.remove(a));
        assertTrue(tree.remove(b));
        assertTrue(tree.remove(c));

        assertSame(EmptyNode.getInstance(), tree.getRoot());
    }


    /**
     * Counts occurrences of a substring.
     *
     * @param text
     *            text to search
     * @param target
     *            substring to count
     * @return number of occurrences
     */
    private int countOccurrences(String text, String target)
    {
        int count = 0;
        int index = 0;

        while ((index = text.indexOf(target, index)) != -1)
        {
            count++;
            index += target.length();
        }

        return count;
    }
}
