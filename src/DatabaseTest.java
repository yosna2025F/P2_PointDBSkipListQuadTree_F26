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
    
    /**
     * Tests removing a point by name.
     */
    public void testRemoveByName()
    {
        database.insert("alpha", 10, 20);
        database.remove("alpha");

        assertTrue(systemOut().getHistory().contains(
            "Point removed: (alpha, 10, 20)"));

        database.search("alpha");

        assertTrue(systemOut().getHistory().contains(
            "Point not found: alpha"));
    }


    /**
     * Tests removing a point by coordinates.
     */
    public void testRemoveByCoordinates()
    {
        database.insert("alpha", 10, 20);
        database.remove(10, 20);

        assertTrue(systemOut().getHistory().contains(
            "Point removed: (alpha, 10, 20)"));

        database.search("alpha");

        assertTrue(systemOut().getHistory().contains(
            "Point not found: alpha"));
    }


    /**
     * Tests removing a nonexistent name.
     */
    public void testRemoveMissingName()
    {
        database.remove("missing");

        assertTrue(systemOut().getHistory().contains(
            "Point not removed: missing"));
    }


    /**
     * Tests removing nonexistent coordinates.
     */
    public void testRemoveMissingCoordinates()
    {
        database.remove(10, 20);

        assertTrue(systemOut().getHistory().contains(
            "Point not found: (10, 20)"));
    }


    /**
     * Tests rejecting invalid removal coordinates.
     */
    public void testRemoveInvalidCoordinates()
    {
        database.remove(-1, 20);
        database.remove(1024, 20);

        String output = systemOut().getHistory();

        assertTrue(output.contains("Point rejected: (-1, 20)"));
        assertTrue(output.contains("Point rejected: (1024, 20)"));
    }


    /**
     * Tests removing only one of two points with the same name.
     */
    public void testRemoveDuplicateName()
    {
        database.insert("alpha", 10, 20);
        database.insert("alpha", 30, 40);

        database.remove("alpha");
        database.search("alpha");

        String output = systemOut().getHistory();

        assertTrue(output.contains("Point removed: (alpha,"));
        assertFalse(output.contains("Point not found: alpha"));
    }
    

    /**
     * Tests all point validation conditions.
     */
    public void testIsValidPointBranches()
    {
        assertFalse(database.isValidPoint(null, 10, 20));
        assertFalse(database.isValidPoint("1invalid", 10, 20));
        assertFalse(database.isValidPoint("alpha", -1, 20));
        assertFalse(database.isValidPoint("alpha", 1024, 20));
        assertFalse(database.isValidPoint("alpha", 10, -1));
        assertFalse(database.isValidPoint("alpha", 10, 1024));
    
        assertTrue(database.isValidPoint("alpha", 0, 0));
        assertTrue(database.isValidPoint("alpha", 1023, 1023));
    }
    

    /**
     * Tests both valid and invalid region dimensions.
     */
    public void testRegionSearchBranches()
    {
        database.regionSearch(0, 0, 10, 10);
        database.regionSearch(0, 0, 0, 10);
        database.regionSearch(0, 0, 10, 0);
        database.regionSearch(0, 0, -1, -1);
    
        String output = systemOut().getHistory();
    
        assertTrue(output.contains(
            "Points intersecting region (0, 0, 10, 10):"));
    
        assertTrue(output.contains(
            "Rectangle rejected: (0, 0, 0, 10)"));
    
        assertTrue(output.contains(
            "Rectangle rejected: (0, 0, 10, 0)"));
    
        assertTrue(output.contains(
            "Rectangle rejected: (0, 0, -1, -1)"));
    }
    

    /**
     * Tests every coordinate validation branch for removal.
     */
    public void testRemoveCoordinateBranches()
    {
        database.remove(-1, 10);
        database.remove(1024, 10);
        database.remove(10, -1);
        database.remove(10, 1024);
    
        String output = systemOut().getHistory();
    
        assertTrue(output.contains("Point rejected: (-1, 10)"));
        assertTrue(output.contains("Point rejected: (1024, 10)"));
        assertTrue(output.contains("Point rejected: (10, -1)"));
        assertTrue(output.contains("Point rejected: (10, 1024)"));
    }
    
    
    /**
     * Tests removal with valid coordinates.
     */
    public void testRemoveValidCoordinates()
    {
        database.insert("alpha", 10, 20);
        database.remove(10, 20);
    
        assertTrue(systemOut().getHistory().contains(
            "Point removed: (alpha, 10, 20)"));
    }



}
