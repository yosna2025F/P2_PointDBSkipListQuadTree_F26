import student.TestCase;
import student.TestableRandom;

/**
<<<<<<< HEAD
 * tests database class
 * 
 * @author yosna
 * @version Oct 11, 2026
 */
public class DatabaseTest
{

    // ----------------------------------------------------------
    /**
     * tests methods in database
     */
    @Test
    public void test()
    {
        // fill
    }
    // ~ Fields ................................................................

    // ~ Constructors ..........................................................

    // ~Public Methods ........................................................
=======
 * Tests the Database class.
 *
 * @author Yosna
 * @version Oct 11, 2026
 */
public class DatabaseTest extends TestCase 
{
    private Database database;

    /**
     * Sets up a database before each test.
     */
    public void setUp() 
    {
        database = new Database();
        TestableRandom.setNextBooleans(false);
    }

    /**
     * Tests inserting a valid point.
     */
    public void testValidInsert() 
    {
        database.insert("alpha", 10, 20);

        assertTrue(systemOut().getHistory().contains(
            "Point inserted: (alpha, 10, 20)"));
    }
>>>>>>> bb70c0bd4b9a02e20347cee7b886ad96da4fffa7

    /**
     * Tests rejecting an invalid point.
     */
    public void testInvalidInsert() 
    {
        database.insert("alpha", -1, 20);

        assertTrue(systemOut().getHistory().contains(
            "Point rejected:"));
    }

    /**
     * Tests searching for an existing point.
     */
    public void testSearchFound() 
    {
        database.insert("alpha", 10, 20);
        database.search("alpha");

        assertTrue(systemOut().getHistory().contains(
            "Found (alpha, 10, 20)"));
    }

    /**
     * Tests searching for a missing point.
     */
    public void testSearchNotFound() 
    {
        database.search("missing");

        assertTrue(systemOut().getHistory().contains(
            "Point not found: missing"));
    }

    /**
     * Tests multiple points with the same name.
     */
    public void testDuplicateNames() 
    {
        database.insert("alpha", 10, 20);
        database.insert("alpha", 30, 40);

        database.search("alpha");

        assertTrue(systemOut().getHistory().contains(
            "Found (alpha, 10, 20)"));

        assertTrue(systemOut().getHistory().contains(
            "Found (alpha, 30, 40)"));
    }
}
