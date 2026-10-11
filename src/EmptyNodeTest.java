
// -------------------------------------------------------------------------
import student.TestCase;

/**
 * Tests the EmptyNode class.
 * 
 * @author Saanvi
 * @version Oct 3, 2026
 */
public class EmptyNodeTest
    extends TestCase
{
    private EmptyNode empty;

    /**
     * Sets up an EmptyNode before each test.
     */
    public void setUp()
    {
        empty = EmptyNode.getInstance();
    }


    /**
     * Tests that an EmptyNode can be created.
     */
    public void testEmptyNode()
    {
        assertNotNull(empty);
    }


    /**
     * Tests that the same flyweight instance is returned.
     */
    public void testFlyweight()
    {
        EmptyNode first = EmptyNode.getInstance();
        EmptyNode second = EmptyNode.getInstance();

        assertSame(first, second);
    }


    /**
     * Tests inserting into an empty node.
     */
    public void testInsertIntoEmpty()
    {
        Point point = new Point("a", 100, 100);

        QuadNode result = empty.insert(point, 0, 0, 1024);

        assertTrue(result instanceof LeafNode);

        LeafNode leaf = (LeafNode)result;

        assertEquals(1, leaf.getPoints().size());
        assertSame(point, leaf.getPoints().get(0));
    }


    /**
     * Tests that insertion does not modify the flyweight.
     */
    public void testFlyweightUnchanged()
    {
        empty.insert(new Point("a", 100, 100), 0, 0, 1024);

        assertSame(empty, EmptyNode.getInstance());
    }
}
