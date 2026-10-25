
import java.io.IOException;
import student.TestCase;
import student.TestableRandom;

/**
 * Tests the PointsProject program and its database operations.
 *
 * @author Yosna
 * @version October 2026
 */
public class PointsProjectTest
    extends TestCase
{
    /**
     * Tests running the program without arguments.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    public void testNoArguments()
        throws IOException
    {
        PointsProject.main(new String[0]);

        assertTrue(
            systemOut().getHistory()
                .contains("Usage: PointsProject <command-file>"));
    }


    /**
     * Tests providing too many arguments.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    public void testTooManyArguments()
        throws IOException
    {
        PointsProject.main(new String[] { "file1", "file2" });

        assertTrue(
            systemOut().getHistory()
                .contains("Usage: PointsProject <command-file>"));
    }


    /**
     * Tests a nonexistent command file.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    public void testMissingFile()
        throws IOException
    {
        PointsProject.main(new String[] { "nonexistent_commands_12345.txt" });

        assertTrue(
            systemOut().getHistory()
                .contains("There is no such input file as"));
    }


    /**
     * Tests inserting and searching for points.
     */
    public void testInsertAndSearch()
    {
        TestableRandom.setNextBooleans(false);
        Database database = new Database();

        database.insert("apple", 10, 20);
        database.insert("banana", 30, 40);
        database.search("apple");
        database.search("missing");

        String output = systemOut().getHistory();

        assertTrue(output.contains("Point inserted: (apple, 10, 20)"));

        assertTrue(output.contains("Point inserted: (banana, 30, 40)"));

        assertTrue(output.contains("Found (apple, 10, 20)"));

        assertTrue(output.contains("Point not found: missing"));
    }


    /**
     * Tests duplicate coordinates and region searches.
     */
    public void testDuplicatesAndRegionSearch()
    {
        TestableRandom.setNextBooleans(false);
        Database database = new Database();

        database.insert("apple", 10, 20);
        database.insert("banana", 10, 20);
        database.insert("cherry", 100, 100);

        database.duplicates();
        database.regionSearch(0, 0, 50, 50);
        database.regionSearch(0, 0, 0, 10);

        String output = systemOut().getHistory();

        assertTrue(output.contains("Duplicate points:"));
        assertTrue(output.contains("(10, 20)"));

        assertTrue(
            output.contains("Points intersecting region (0, 0, 50, 50):"));

        assertTrue(output.contains("Point found: (apple, 10, 20)"));

        assertTrue(output.contains("Rectangle rejected: (0, 0, 0, 10)"));
    }


    /**
     * Tests removing points by name and coordinates.
     */
    public void testRemoveCommands()
    {
        TestableRandom.setNextBooleans(false);
        Database database = new Database();

        database.insert("apple", 10, 20);
        database.insert("banana", 30, 40);

        database.remove("apple");
        database.remove(30, 40);

        database.search("apple");
        database.search("banana");

        String output = systemOut().getHistory();

        assertTrue(output.contains("Point removed: (apple, 10, 20)"));

        assertTrue(output.contains("Point removed: (banana, 30, 40)"));

        assertTrue(output.contains("Point not found: apple"));

        assertTrue(output.contains("Point not found: banana"));
    }


    /**
     * Tests the dump operation.
     */
    public void testDumpCommand()
    {
        TestableRandom.setNextBooleans(false);
        Database database = new Database();

        database.insert("apple", 10, 20);
        database.dump();

        String output = systemOut().getHistory();

        assertTrue(output.contains("SkipList dump:"));
        assertTrue(output.contains("QuadTree dump:"));
        assertTrue(output.contains("(apple, 10, 20)"));
    }


    /**
     * Tests rejecting invalid point coordinates.
     */
    public void testInvalidInsert()
    {
        Database database = new Database();

        database.insert("apple", -1, 20);
        database.insert("banana", 10, 1024);

        String output = systemOut().getHistory();

        assertTrue(output.contains("Point rejected: (apple, -1, 20)"));

        assertTrue(output.contains("Point rejected: (banana, 10, 1024)"));
    }


    /**
     * Tests removing points that do not exist.
     */
    public void testRemoveMissingPoints()
    {
        Database database = new Database();

        database.remove("missing");
        database.remove(10, 20);

        String output = systemOut().getHistory();

        assertTrue(output.contains("Point not removed: missing"));

        assertTrue(output.contains("Point not found: (10, 20)"));
    }
}
