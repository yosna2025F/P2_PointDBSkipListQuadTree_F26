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
}