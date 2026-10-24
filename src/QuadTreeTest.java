
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
    
    
    /**
     * Tests null and invalid point insertion.
     */
    public void testInsertInvalidPoints()
    {
        tree.insert(null);
    
        tree.insert(new Point("a", -1, 10));
        tree.insert(new Point("b", 1024, 10));
        tree.insert(new Point("c", 10, -1));
        tree.insert(new Point("d", 10, 1024));
    
        tree.dump();
    
        assertTrue(systemOut().getHistory().contains(
            "QuadTree size is:") == false);
    }
    
    
    /**
     * Tests points on valid world boundaries.
     */
    public void testInsertBoundaryPoints()
    {
        tree.insert(new Point("a", 0, 0));
        tree.insert(new Point("b", 1023, 1023));
    
        tree.regionSearch(0, 0, 1024, 1024);
    
        String output = systemOut().getHistory();
    
        assertTrue(output.contains("(a, 0, 0)"));
        assertTrue(output.contains("(b, 1023, 1023)"));
    }
    
    
    /**
     * Tests an empty tree.
     */
    public void testEmptyTreeOperations()
    {
        tree.dump();
        tree.duplicates();
        tree.regionSearch(0, 0, 10, 10);
    
        String output = systemOut().getHistory();
    
        assertTrue(output.contains("QuadTree dump:"));
        assertTrue(output.contains("Duplicate points:"));
        assertTrue(output.contains("quadtree nodes visited"));
    }
    
    
    /**
     * Tests duplicate coordinates with different names.
     */
    public void testDuplicateLocations()
    {
        tree.insert(new Point("a", 10, 20));
        tree.insert(new Point("b", 10, 20));
        tree.insert(new Point("c", 10, 20));
    
        tree.duplicates();
    
        assertTrue(systemOut().getHistory().contains("(10, 20)"));
    }
    
    
    
    /**
     * Tests removal from all four quadrants.
     */
    public void testRemoveAllQuadrants()
    {
        tree.insert(new Point("nw", 100, 100));
        tree.insert(new Point("ne", 900, 100));
        tree.insert(new Point("sw", 100, 900));
        tree.insert(new Point("se", 900, 900));
    
        assertNotNull(tree.remove(100, 100));
        assertNotNull(tree.remove(900, 100));
        assertNotNull(tree.remove(100, 900));
        assertNotNull(tree.remove(900, 900));
    
        assertNull(tree.remove(100, 100));
    }
    
    
    /**
     * Tests removing an exact point object.
     */
    public void testRemoveExactPoint()
    {
        Point first = new Point("first", 10, 20);
        Point second = new Point("second", 10, 20);
    
        tree.insert(first);
        tree.insert(second);
    
        assertTrue(tree.remove(first));
        assertFalse(tree.remove(first));
    
        assertNotNull(tree.remove(10, 20));
    }
    
    
    /**
     * Tests invalid and missing removals.
     */
    public void testRemoveInvalidAndMissing()
    {
        assertFalse(tree.remove((Point)null));
    
        assertNull(tree.remove(-1, 10));
        assertNull(tree.remove(1024, 10));
        assertNull(tree.remove(10, -1));
        assertNull(tree.remove(10, 1024));
    
        assertNull(tree.remove(500, 500));
    }


    
    /**
     * Tests region searches in different quadrants.
     */
    public void testRegionSearchQuadrants()
    {
        tree.insert(new Point("nw", 100, 100));
        tree.insert(new Point("ne", 900, 100));
        tree.insert(new Point("sw", 100, 900));
        tree.insert(new Point("se", 900, 900));
    
        tree.regionSearch(0, 0, 512, 512);
        tree.regionSearch(512, 0, 512, 512);
        tree.regionSearch(0, 512, 512, 512);
        tree.regionSearch(512, 512, 512, 512);
    
        tree.regionSearch(0, 0, 1024, 1024);
    
        assertTrue(systemOut().getHistory().contains(
            "quadtree nodes visited"));
    }


    /**
     * Tests dumping a tree containing internal, leaf,
     * and empty nodes.
     */
    public void testDumpInternalTree()
    {
        tree.insert(new Point("nw", 100, 100));
        tree.insert(new Point("ne", 900, 100));
        tree.insert(new Point("sw", 100, 900));
        tree.insert(new Point("se", 900, 900));
    
        tree.dump();
    
        String output = systemOut().getHistory();
    
        assertTrue(output.contains("Internal"));
        assertTrue(output.contains("Node at 0, 0, 1024:"));
        assertTrue(output.contains("(nw, 100, 100)"));
        assertTrue(output.contains("(se, 900, 900)"));
        assertTrue(output.contains("5 quadtree nodes printed"));
    }
    
    
    /**
     * Tests dumping internal nodes with empty children.
     */
    public void testDumpWithEmptyChildren()
    {
        tree.insert(new Point("a", 10, 10));
        tree.insert(new Point("b", 20, 20));
        tree.insert(new Point("c", 30, 30));
        tree.insert(new Point("d", 900, 900));
    
        tree.dump();
    
        String output = systemOut().getHistory();
    
        assertTrue(output.contains("Internal"));
        assertTrue(output.contains("Empty"));
        assertTrue(output.contains("(a, 10, 10)"));
    }
    
    
    /**
     * Tests duplicates in a tree with internal nodes.
     */
    public void testDuplicatesInternalTree()
    {
        tree.insert(new Point("a", 100, 100));
        tree.insert(new Point("b", 100, 100));
        tree.insert(new Point("c", 900, 100));
        tree.insert(new Point("d", 100, 900));
        tree.insert(new Point("e", 900, 900));
    
        assertTrue(tree.getRoot() instanceof InternalNode);
    
        tree.duplicates();
    
        String output = systemOut().getHistory();
    
        assertTrue(output.contains("Duplicate points:"));
        assertTrue(output.contains("(100, 100)"));
    }
    
    
    /**
     * Tests that duplicate coordinates are printed only once.
     */
    public void testDuplicatesPrintedOnce()
    {
        tree.insert(new Point("a", 50, 50));
        tree.insert(new Point("b", 50, 50));
        tree.insert(new Point("c", 50, 50));
        tree.insert(new Point("d", 100, 100));
    
        tree.duplicates();
    
        String output = systemOut().getHistory();
    
        assertEquals(1, countOccurrences(output, "(50, 50)"));
    }
    
    
    /**
     * Tests searching all quadrants and their boundaries.
     */
    public void testRegionSearchAcrossQuadrants()
    {
        tree.insert(new Point("nw", 100, 100));
        tree.insert(new Point("ne", 900, 100));
        tree.insert(new Point("sw", 100, 900));
        tree.insert(new Point("se", 900, 900));
    
        tree.regionSearch(400, 400, 600, 600);
    
        String output = systemOut().getHistory();
    
        assertTrue(output.contains("Point found: (se, 900, 900)"));
        assertFalse(output.contains("Point found: (nw, 100, 100)"));
        assertTrue(output.contains("quadtree nodes visited"));
    }
    
    
    /**
     * Tests a region that does not intersect the tree.
     */
    public void testRegionSearchOutsideWorld()
    {
        tree.insert(new Point("a", 100, 100));
        tree.insert(new Point("b", 900, 100));
        tree.insert(new Point("c", 100, 900));
        tree.insert(new Point("d", 900, 900));
    
        tree.regionSearch(2000, 2000, 100, 100);
    
        String output = systemOut().getHistory();
    
        assertFalse(output.contains("Point found:"));
        assertTrue(output.contains("1 quadtree nodes visited"));
    }
    
    
    /**
     * Tests a region that intersects every quadrant.
     */
    public void testRegionSearchEntireTree()
    {
        tree.insert(new Point("nw", 100, 100));
        tree.insert(new Point("ne", 900, 100));
        tree.insert(new Point("sw", 100, 900));
        tree.insert(new Point("se", 900, 900));
    
        tree.regionSearch(0, 0, 1024, 1024);
    
        String output = systemOut().getHistory();
    
        assertTrue(output.contains("Point found: (nw, 100, 100)"));
        assertTrue(output.contains("Point found: (ne, 900, 100)"));
        assertTrue(output.contains("Point found: (sw, 100, 900)"));
        assertTrue(output.contains("Point found: (se, 900, 900)"));
        assertTrue(output.contains("5 quadtree nodes visited"));
    }
    
    
    /**
     * Tests removing a nonexistent point from an internal tree.
     */
    public void testRemoveMissingFromInternal()
    {
        tree.insert(new Point("a", 100, 100));
        tree.insert(new Point("b", 900, 100));
        tree.insert(new Point("c", 100, 900));
        tree.insert(new Point("d", 900, 900));
    
        assertNull(tree.remove(200, 200));
    
        assertTrue(tree.getRoot() instanceof InternalNode);
    }
    
    
    /**
     * Tests removing an exact object that is not in the tree.
     */
    public void testRemoveDifferentObject()
    {
        Point original = new Point("a", 100, 100);
        Point different = new Point("a", 100, 100);
    
        tree.insert(original);
    
        assertFalse(tree.remove(different));
        assertTrue(tree.getRoot() instanceof LeafNode);
    }
    
    
    /**
     * Tests removal when multiple points remain in one leaf.
     */
    public void testRemoveFromNonemptyLeaf()
    {
        tree.insert(new Point("a", 100, 100));
        tree.insert(new Point("b", 200, 200));
        tree.insert(new Point("c", 300, 300));
    
        assertNotNull(tree.remove(200, 200));
    
        assertTrue(tree.getRoot() instanceof LeafNode);
    
        LeafNode leaf = (LeafNode)tree.getRoot();
    
        assertEquals(2, leaf.getPoints().size());
    }
    
    
    /**
     * Tests collapsing an internal tree after several removals.
     */
    public void testCollapseAfterMultipleRemovals()
    {
        tree.insert(new Point("a", 100, 100));
        tree.insert(new Point("b", 900, 100));
        tree.insert(new Point("c", 100, 900));
        tree.insert(new Point("d", 900, 900));
        tree.insert(new Point("e", 950, 950));
    
        assertTrue(tree.getRoot() instanceof InternalNode);
    
        assertNotNull(tree.remove(950, 950));
        assertNotNull(tree.remove(900, 900));
    
        assertTrue(tree.getRoot() instanceof LeafNode);
    
        LeafNode leaf = (LeafNode)tree.getRoot();
    
        assertEquals(3, leaf.getPoints().size());
    }
    
    
    /**
     * Tests keeping an internal node when too many
     * distinct points remain.
     */
    public void testInternalNodeDoesNotCollapse()
    {
        tree.insert(new Point("a", 100, 100));
        tree.insert(new Point("b", 900, 100));
        tree.insert(new Point("c", 100, 900));
        tree.insert(new Point("d", 900, 900));
        tree.insert(new Point("e", 950, 950));
    
        assertNotNull(tree.remove(950, 950));
    
        assertTrue(tree.getRoot() instanceof InternalNode);
    }
    
    
    /**
     * Counts occurrences of a substring.
     *
     * @param text the text to search
     * @param target the substring
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
    
    

    /**
     * Tests collapse when remaining points share coordinates.
     */
    public void testCollapseSameCoordinates()
    {
        tree.insert(new Point("a", 100, 100));
        tree.insert(new Point("b", 100, 100));
        tree.insert(new Point("c", 100, 100));
        tree.insert(new Point("d", 100, 100));
        tree.insert(new Point("e", 900, 900));
    
        assertTrue(tree.getRoot() instanceof InternalNode);
    
        assertNotNull(tree.remove(900, 900));
    
        assertTrue(tree.getRoot() instanceof LeafNode);
    
        LeafNode leaf = (LeafNode)tree.getRoot();
    
        assertEquals(4, leaf.getPoints().size());
    }
    
    

    /**
     * Tests keeping an internal node when a child
     * is still an internal node after removal.
     */
    public void testCollapseWithInternalChild()
    {
        tree.insert(new Point("a", 10, 10));
        tree.insert(new Point("b", 20, 20));
        tree.insert(new Point("c", 30, 30));
        tree.insert(new Point("d", 40, 40));
        tree.insert(new Point("e", 900, 900));
    
        assertTrue(tree.getRoot() instanceof InternalNode);
    
        InternalNode root = (InternalNode)tree.getRoot();
    
        assertTrue(root.getNorthwest() instanceof InternalNode);
    
        // Remove a point from another quadrant.
        assertNotNull(tree.remove(900, 900));
    
        // The northwest subtree still requires
        // an internal node.
        assertTrue(tree.getRoot() instanceof InternalNode);
    }

        
    
    /**
     * Tests removal through all four internal quadrants.
     */
    public void testRemoveFromEveryQuadrant()
    {
        tree.insert(new Point("nw", 100, 100));
        tree.insert(new Point("ne", 900, 100));
        tree.insert(new Point("sw", 100, 900));
        tree.insert(new Point("se", 900, 900));
    
        assertTrue(tree.getRoot() instanceof InternalNode);
    
        // Remove from southwest.
        assertNotNull(tree.remove(100, 900));
    
        // Restore the point so the tree splits again.
        tree.insert(new Point("sw2", 100, 900));
    
        // Remove from northeast.
        assertNotNull(tree.remove(900, 100));
    
        tree.insert(new Point("ne2", 900, 100));
    
        // Remove from northwest.
        assertNotNull(tree.remove(100, 100));
    
        tree.insert(new Point("nw2", 100, 100));
    
        // Remove from southeast.
        assertNotNull(tree.remove(900, 900));
    }

        
            
    
    /**
     * Tests an internal child in the northeast quadrant.
     */
    public void testCollapseNortheastInternalChild()
    {
        tree.insert(new Point("a", 600, 100));
        tree.insert(new Point("b", 650, 150));
        tree.insert(new Point("c", 700, 200));
        tree.insert(new Point("d", 750, 250));
        tree.insert(new Point("e", 100, 900));
    
        assertTrue(tree.getRoot() instanceof InternalNode);
    
        InternalNode root = (InternalNode)tree.getRoot();
    
        assertTrue(root.getNortheast() instanceof InternalNode);
    
        assertNotNull(tree.remove(100, 900));
    
        assertTrue(tree.getRoot() instanceof InternalNode);
    }

}
