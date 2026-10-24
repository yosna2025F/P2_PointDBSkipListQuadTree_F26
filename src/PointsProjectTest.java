
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import student.TestCase;

/**
 * Tests the PointsProject command file processor.
 *
 * @author Yosna
 * @version October 2026
 */
public class PointsProjectTest extends TestCase
{
    /**
     * Tests running the program without arguments.
     *
     * @throws IOException if an I/O error occurs
     */
    public void testNoArguments() throws IOException
    {
        PointsProject.main(new String[0]);

        assertTrue(systemOut().getHistory().contains(
            "Usage: PointsProject <command-file>"));
    }


    /**
     * Tests providing too many arguments.
     *
     * @throws IOException if an I/O error occurs
     */
    public void testTooManyArguments() throws IOException
    {
        PointsProject.main(new String[] { "file1", "file2" });

        assertTrue(systemOut().getHistory().contains(
            "Usage: PointsProject <command-file>"));
    }


    /**
     * Tests a nonexistent command file.
     *
     * @throws IOException if an I/O error occurs
     */
    public void testMissingFile() throws IOException
    {
        PointsProject.main(new String[] {
            "nonexistent_commands_12345.txt"
        });

        assertTrue(systemOut().getHistory().contains(
            "There is no such input file as"));
    }


    /**
     * Tests inserting and searching for points.
     *
     * @throws IOException if an I/O error occurs
     */
    public void testInsertAndSearch() throws IOException
    {
        File file = File.createTempFile("points", ".txt");

        try
        {
            try (FileWriter writer = new FileWriter(file))
            {
                writer.write("insert apple 10 20\n");
                writer.write("insert banana 30 40\n");
                writer.write("search apple\n");
                writer.write("search missing\n");
            }

            PointsProject.main(new String[] {
                file.getAbsolutePath()
            });

            String output = systemOut().getHistory();

            assertTrue(output.contains(
                "Point inserted: (apple, 10, 20)"));

            assertTrue(output.contains(
                "Point inserted: (banana, 30, 40)"));

            assertTrue(output.contains(
                "Found (apple, 10, 20)"));

            assertTrue(output.contains(
                "Point not found: missing"));
        }
        finally
        {
            file.delete();
        }
    }


    /**
     * Tests duplicate coordinates and region searches.
     *
     * @throws IOException if an I/O error occurs
     */
    public void testDuplicatesAndRegionSearch() throws IOException
    {
        File file = File.createTempFile("points", ".txt");

        try
        {
            try (FileWriter writer = new FileWriter(file))
            {
                writer.write("insert apple 10 20\n");
                writer.write("insert banana 10 20\n");
                writer.write("insert cherry 100 100\n");
                writer.write("duplicates\n");
                writer.write("regionsearch 0 0 50 50\n");
                writer.write("regionsearch 0 0 0 10\n");
            }

            PointsProject.main(new String[] {
                file.getAbsolutePath()
            });

            String output = systemOut().getHistory();

            assertTrue(output.contains("Duplicate points:"));
            assertTrue(output.contains("(10, 20)"));

            assertTrue(output.contains(
                "Points intersecting region (0, 0, 50, 50):"));

            assertTrue(output.contains(
                "Point found: (apple, 10, 20)"));

            assertTrue(output.contains(
                "Rectangle rejected: (0, 0, 0, 10)"));
        }
        finally
        {
            file.delete();
        }
    }


    /**
     * Tests removing a point by name and coordinates.
     *
     * @throws IOException if an I/O error occurs
     */
    public void testRemoveCommands() throws IOException
    {
        File file = File.createTempFile("points", ".txt");

        try
        {
            try (FileWriter writer = new FileWriter(file))
            {
                writer.write("insert apple 10 20\n");
                writer.write("insert banana 30 40\n");
                writer.write("remove apple\n");
                writer.write("remove 30 40\n");
                writer.write("search apple\n");
                writer.write("search banana\n");
            }

            PointsProject.main(new String[] {
                file.getAbsolutePath()
            });

            String output = systemOut().getHistory();

            assertTrue(output.contains(
                "Point removed: (apple, 10, 20)"));

            assertTrue(output.contains(
                "Point removed: (banana, 30, 40)"));

            assertTrue(output.contains(
                "Point not found: apple"));

            assertTrue(output.contains(
                "Point not found: banana"));
        }
        finally
        {
            file.delete();
        }
    }


    /**
     * Tests the dump command.
     *
     * @throws IOException if an I/O error occurs
     */
    public void testDumpCommand() throws IOException
    {
        File file = File.createTempFile("points", ".txt");

        try
        {
            try (FileWriter writer = new FileWriter(file))
            {
                writer.write("insert apple 10 20\n");
                writer.write("dump\n");
            }

            PointsProject.main(new String[] {
                file.getAbsolutePath()
            });

            String output = systemOut().getHistory();

            assertTrue(output.contains("SkipList dump:"));
            assertTrue(output.contains("QuadTree dump:"));
        }
        finally
        {
            file.delete();
        }
    }


    /**
     * Tests blank lines, whitespace, and unknown commands.
     *
     * @throws IOException if an I/O error occurs
     */
    public void testWhitespaceAndUnknownCommands()
        throws IOException
    {
        File file = File.createTempFile("points", ".txt");

        try
        {
            try (FileWriter writer = new FileWriter(file))
            {
                writer.write("\n");
                writer.write("   \n");
                writer.write("  insert   apple   10   20  \n");
                writer.write("unknowncommand\n");
                writer.write("search apple\n");
            }

            PointsProject.main(new String[] {
                file.getAbsolutePath()
            });

            String output = systemOut().getHistory();

            assertTrue(output.contains(
                "Point inserted: (apple, 10, 20)"));

            assertTrue(output.contains(
                "Found (apple, 10, 20)"));
        }
        finally
        {
            file.delete();
        }
    }


    /**
     * Tests a remove command with an unsupported argument count.
     *
     * @throws IOException if an I/O error occurs
     */
    public void testInvalidRemoveArgumentCount()
        throws IOException
    {
        File file = File.createTempFile("points", ".txt");

        try
        {
            try (FileWriter writer = new FileWriter(file))
            {
                writer.write("insert apple 10 20\n");
                writer.write("remove\n");
                writer.write("remove apple 10 20\n");
                writer.write("search apple\n");
            }

            PointsProject.main(new String[] {
                file.getAbsolutePath()
            });

            assertTrue(systemOut().getHistory().contains(
                "Found (apple, 10, 20)"));
        }
        finally
        {
            file.delete();
        }
    }
}
