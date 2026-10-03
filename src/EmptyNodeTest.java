
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
        empty = new EmptyNode();
    }


    /**
     * Tests that an EmptyNode can be created.
     */
    public void testEmptyNode()
    {
        assertNotNull(empty);
    }
}
