
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
    }
}
