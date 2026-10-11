// -------------------------------------------------------------------------
/**
 * Represents the PR Quadtree used to organize points by their spatial location.
 * 
 * @author Saanvi
 * @version Oct 3, 2026
 */
public class QuadTree
{
    private static final int WORLD_SIZE = 1024;
    private QuadNode root;

    /**
     * Creates an empty Quadtree.
     */
    public QuadTree()
    {
        root = EmptyNode.getInstance();
    }


    /**
     * Returns the root node of the Quadtree.
     * 
     * @return the root node
     */
    public QuadNode getRoot()
    {
        return root;
    }


    /**
     * Inserts point into Quadtree.
     *
     * @param point
     *            point to insert
     */
    public void insert(Point point)
    {
        if (point == null)
        {
            return;
        }

        if (point.getX() < 0 || point.getX() >= WORLD_SIZE || point.getY() < 0
            || point.getY() >= WORLD_SIZE)
        {
            return;
        }

        root = root.insert(point, 0, 0, WORLD_SIZE);
    }
}
