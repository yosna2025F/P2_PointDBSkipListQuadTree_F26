
// -------------------------------------------------------------------------
import java.util.ArrayList;

/**
 * manages database of points ======= Manages the point database using a
 * SkipList and QuadTree. >>>>>>>
 * 
 * @author yosna
 * @version Oct 11, 2026
 */
public class Database
{
    // ~ Fields
    // ................................................................
    private final SkipList<String, Point> names;
    private final QuadTree spatial;

    // ~ Constructors
    // ..........................................................
    /**
     * Creates an empty database.
     */
    public Database()
    {
        names = new SkipList<String, Point>();
        spatial = new QuadTree();
    }


    // ~Public Methods
    // ........................................................
    /**
     * Inserts a valid point into both data structures.
     *
     * @param name
     *            point name
     * @param x
     *            x-coordinate
     * @param y
     *            y-coordinate
     */
    public void insert(String name, int x, int y)
    {
        if (!isValidPoint(name, x, y))
        {
            System.out.println(
                "Point rejected: (" + name + ", " + x + ", " + y + ")");
            return;
        }

        Point point = new Point(name, x, y);

        names.insert(new KVPair<String, Point>(name, point));
        spatial.insert(point);

        System.out.println("Point inserted: " + point);
    }


    /**
     * Searches for all points with the given name.
     *
     * @param name
     *            name to search for
     */
    public void search(String name)
    {
        ArrayList<KVPair<String, Point>> found = names.search(name);

        if (found.isEmpty())
        {
            System.out.println("Point not found: " + name);
        }
        else
        {
            for (KVPair<String, Point> pair : found)
            {
                System.out.println("Found " + pair.value());
            }
        }
    }


    /**
     * Prints both data structures.
     */
    public void dump()
    {
        names.dump();
        spatial.dump();
    }


    /**
     * Prints all duplicate point locations.
     */
    public void duplicates()
    {
        spatial.duplicates();
    }


    /**
     * Checks whether a point is valid.
     *
     * @param name
     *            point name
     * @param x
     *            x-coordinate
     * @param y
     *            y-coordinate
     * @return true if the point is valid
     */
    public boolean isValidPoint(String name, int x, int y)
    {
        return name != null && name.matches("[A-Za-z][A-Za-z0-9_]*") && x >= 0
            && x < 1024 && y >= 0 && y < 1024;
    }

}
