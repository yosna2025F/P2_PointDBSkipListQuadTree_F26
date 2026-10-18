import student.TestCase;
import student.TestableRandom;
import java.util.ArrayList;

/**
 * tests database class
 * 
 * @author yosna
 * @version Oct 11, 2026
 */

public class DatabaseTest
    extends TestCase
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

        assertTrue(
            systemOut().getHistory()
                .contains("Point inserted: (alpha, 10, 20)"));
    }


    /**
     * Tests rejecting an invalid point.
     */
    public void testInvalidInsert()
    {
        database.insert("alpha", -1, 20);

        assertTrue(systemOut().getHistory().contains("Point rejected:"));
    }


    /**
     * Tests searching for an existing point.
     */
    public void testSearchFound()
    {
        database.insert("alpha", 10, 20);
        database.search("alpha");

        assertTrue(systemOut().getHistory().contains("Found (alpha, 10, 20)"));
    }


    /**
     * Tests searching for a missing point.
     */
    public void testSearchNotFound()
    {
        database.search("missing");

        assertTrue(
            systemOut().getHistory().contains("Point not found: missing"));
    }


    /**
     * Tests multiple points with the same name.
     */
    public void testDuplicateNames()
    {
        database.insert("alpha", 10, 20);
        database.insert("alpha", 30, 40);

        database.search("alpha");

        assertTrue(systemOut().getHistory().contains("Found (alpha, 10, 20)"));

        assertTrue(systemOut().getHistory().contains("Found (alpha, 30, 40)"));
    }
    
    /**
     * Tests rejecting a rectangle with zero width.
     */
    public void testRegionZeroWidth()
    {
        database.regionSearch(5, 5, 0, 10);

        assertTrue(systemOut().getHistory().contains(
            "Rectangle rejected: (5, 5, 0, 10)"));
    }


    /**
     * Tests rejecting a rectangle with negative height.
     */
    public void testRegionNegativeHeight()
    {
        database.regionSearch(5, 5, 10, -2);

        assertTrue(systemOut().getHistory().contains(
            "Rectangle rejected: (5, 5, 10, -2)"));
    }


    /**
     * Tests rejecting a rectangle with negative width.
     */
    public void testRegionNegativeWidth()
    {
        database.regionSearch(0, 0, -5, 10);

        assertTrue(systemOut().getHistory().contains(
            "Rectangle rejected: (0, 0, -5, 10)"));
    }


    /**
     * Tests rejecting a rectangle with zero height.
     */
    public void testRegionZeroHeight()
    {
        database.regionSearch(5, 5, 10, 0);

        assertTrue(systemOut().getHistory().contains(
            "Rectangle rejected: (5, 5, 10, 0)"));
    }


    /**
     * Tests searching a valid region.
     */
    public void testValidRegionSearch()
    {
        database.insert("alpha", 10, 20);
        database.insert("beta", 30, 40);

        database.regionSearch(0, 0, 25, 25);

        String output = systemOut().getHistory();
        String regionOutput = output.substring(
            output.indexOf("Points intersecting region"));

        assertTrue(regionOutput.contains(
            "Points intersecting region (0, 0, 25, 25):"));

        assertTrue(regionOutput.contains(
            "(alpha, 10, 20)"));

        assertFalse(regionOutput.contains(
            "(beta, 30, 40)"));
    }


    /**
     * Tests detecting duplicate coordinates.
     */
    public void testDuplicateCoordinates()
    {
        database.insert("alpha", 10, 20);
        database.insert("beta", 10, 20);

        database.duplicates();

        String output = systemOut().getHistory();

        assertTrue(output.contains("Duplicate points:"));
        assertTrue(output.contains("(10, 20)"));
    }


    /**
     * Tests that duplicate coordinates print only once.
     */
    public void testThreeDuplicateCoordinates()
    {
        database.insert("alpha", 10, 20);
        database.insert("beta", 10, 20);
        database.insert("gamma", 10, 20);

        database.duplicates();

        String output = systemOut().getHistory();
        int start = output.indexOf("Duplicate points:");

        assertTrue(start >= 0);

        String duplicateOutput = output.substring(start);

        assertEquals(
            duplicateOutput.indexOf("(10, 20)"),
            duplicateOutput.lastIndexOf("(10, 20)"));
    }


    /**
     * Tests a region extending outside the world.
     */
    public void testRegionOutsideWorld()
    {
        database.insert("alpha", 10, 20);

        database.regionSearch(-5, -5, 30, 30);

        String output = systemOut().getHistory();

        assertTrue(output.contains(
            "Points intersecting region (-5, -5, 30, 30):"));
    }


    /**
     * Tests database dump output.
     */
    public void testDump()
    {
        database.insert("alpha", 10, 20);
        database.dump();

        String output = systemOut().getHistory();

        assertTrue(output.contains("SkipList dump:"));
        assertTrue(output.contains("QuadTree dump:"));
    }
}
