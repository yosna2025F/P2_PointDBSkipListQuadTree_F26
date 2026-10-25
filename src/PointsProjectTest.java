
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import student.TestCase;

/**
 * Tests PointsProject using the provided syntax files.
 *
 * @author Yosna
 * @version October 2026
 */
public class PointsProjectTest extends TestCase
{
    /**
     * Tests running without arguments.
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
     * Tests the first provided syntax command file.
     *
     * @throws IOException if an I/O error occurs
     */
    public void testSyntax1() throws IOException
    {
        PointsProject.main(new String[] { "SyntaxTest1.txt" });

        String output = systemOut().getHistory();

        assertTrue(output.contains(
            "Point rejected: (p_p, -1, -20)"));
        assertTrue(output.contains(
            "Point rejected: (poi, 7, -8)"));
        assertTrue(output.contains("Duplicate points:"));
        assertTrue(output.contains("SkipList dump:"));
        assertTrue(output.contains("QuadTree dump:"));
        assertTrue(output.contains(
            "Point not found: p_p"));
        assertTrue(output.contains(
            "Point not removed: p_p"));
        assertTrue(output.contains(
            "Rectangle rejected: (5, 5, 4, -2)"));
    }


    /**
     * Tests the second provided syntax command file.
     *
     * @throws IOException if an I/O error occurs
     */
    public void testSyntax2() throws IOException
    {
        PointsProject.main(new String[] { "SyntaxTest2.txt" });

        String output = systemOut().getHistory();

        assertTrue(output.contains(
            "Point inserted: (p_p, 1, 20)"));
        assertTrue(output.contains(
            "Point inserted: (poi, 10, 30)"));
        assertTrue(output.contains(
            "Point inserted: (p_42, 1, 20)"));
        assertTrue(output.contains(
            "Point inserted: (far, 200, 200)"));
        assertTrue(output.contains("Duplicate points:"));
        assertTrue(output.contains("(1, 20)"));
        assertTrue(output.contains(
            "Found (p_p, 1, 20)"));
        assertTrue(output.contains(
            "Point removed: (p_p, 1, 20)"));
        assertTrue(output.contains(
            "Point removed: (poi, 10, 30)"));
        assertTrue(output.contains("SkipList dump:"));
        assertTrue(output.contains("QuadTree dump:"));
    }


    /**
     * Tests rejected insertions and region searches.
     *
     * @throws IOException if an I/O error occurs
     */
    public void testInvalidCommands() throws IOException
    {
        PointsProject.main(new String[] { "SyntaxTest1.txt" });

        String output = systemOut().getHistory();

        assertTrue(output.contains(
            "Point rejected: (p_p, -1, -20)"));

        assertTrue(output.contains(
            "Point not removed: p_p"));

        assertTrue(output.contains(
            "Rectangle rejected: (5, 5, 4, -2)"));
    }


    /**
     * Tests duplicate points and successful removal.
     *
     * @throws IOException if an I/O error occurs
     */
    public void testValidCommands() throws IOException
    {
        PointsProject.main(new String[] { "SyntaxTest2.txt" });

        String output = systemOut().getHistory();

        assertTrue(output.contains("Duplicate points:"));

        assertTrue(output.contains("(1, 20)"));

        assertTrue(output.contains(
            "Point removed: (p_p, 1, 20)"));

        assertTrue(output.contains(
            "Point removed: (poi, 10, 30)"));
    }


    /**
     * Normalizes line endings and trailing whitespace.
     *
     * @param text text to normalize
     * @return normalized text
     */
    private String normalize(String text)
    {
        return text.replace("\r\n", "\n")
            .replace("\r", "\n")
            .stripTrailing();
    }
}
