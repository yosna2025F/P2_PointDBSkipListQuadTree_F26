
// -------------------------------------------------------------------------
import student.TestCase;

/**
 * Tests the InternalNode class.
 * 
 * @author Yosna
 * @version Oct 3, 2026
 */
public class InternalNodeTest
    extends TestCase
{
    private InternalNode node;

    /**
     * Sets up an InternalNode before each test.
     */
    public void setUp()
    {
        node = new InternalNode();
    }


    /**
     * Tests creation of an InternalNode.
     */
    public void testInternalNode()
    {
        assertNotNull(node);
    }


    /**
     * Tests the initial children.
     */
    public void testChildren()
    {
        assertTrue(node.getNorthwest() instanceof EmptyNode);
        assertTrue(node.getNortheast() instanceof EmptyNode);
        assertTrue(node.getSouthwest() instanceof EmptyNode);
        assertTrue(node.getSoutheast() instanceof EmptyNode);
    }


    /**
     * Tests changing the children.
     */
    public void testSetChildren()
    {
        LeafNode northwest = new LeafNode();
        LeafNode northeast = new LeafNode();
        LeafNode southwest = new LeafNode();
        LeafNode southeast = new LeafNode();

        node.setNorthwest(northwest);
        node.setNortheast(northeast);
        node.setSouthwest(southwest);
        node.setSoutheast(southeast);

        assertSame(northwest, node.getNorthwest());
        assertSame(northeast, node.getNortheast());
        assertSame(southwest, node.getSouthwest());
        assertSame(southeast, node.getSoutheast());
    }


    /**
     * Tests inserting a point into the northwest quadrant.
     */
    public void testInsertNorthwest()
    {
        node.insert(new Point("a", 100, 100), 0, 0, 1024);

        assertTrue(node.getNorthwest() instanceof LeafNode);
        assertTrue(node.getNortheast() instanceof EmptyNode);
        assertTrue(node.getSouthwest() instanceof EmptyNode);
        assertTrue(node.getSoutheast() instanceof EmptyNode);
    }


    /**
     * Tests inserting a point into the northeast quadrant.
     */
    public void testInsertNortheast()
    {
        node.insert(new Point("a", 600, 100), 0, 0, 1024);

        assertTrue(node.getNortheast() instanceof LeafNode);
    }


    /**
     * Tests inserting a point into southwest quadrant.
     */
    public void testInsertSouthwest()
    {
        node.insert(new Point("a", 100, 600), 0, 0, 1024);

        assertTrue(node.getSouthwest() instanceof LeafNode);
    }


    /**
     * Tests inserting a point into southeast quadrant.
     */
    public void testInsertSoutheast()
    {
        node.insert(new Point("a", 600, 600), 0, 0, 1024);

        assertTrue(node.getSoutheast() instanceof LeafNode);
    }


    /**
     * Tests inserting points on the quadrant boundaries.
     */
    public void testQuadrantBoundaries()
    {
        node.insert(new Point("a", 512, 0), 0, 0, 1024);
        node.insert(new Point("b", 0, 512), 0, 0, 1024);
        node.insert(new Point("c", 512, 512), 0, 0, 1024);

        assertTrue(node.getNortheast() instanceof LeafNode);
        assertTrue(node.getSouthwest() instanceof LeafNode);
        assertTrue(node.getSoutheast() instanceof LeafNode);
    }
}
