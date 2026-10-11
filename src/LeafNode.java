
// -------------------------------------------------------------------------
import java.util.ArrayList;

/**
 * Represents a leaf node in the PR Quadtree. Leaf nodes store points in the
 * tree.
 * 
 * @author Saanvi
 * @version Oct 3, 2026
 */
public class LeafNode
    implements QuadNode
{
    private ArrayList<Point> points;

    /**
     * Creates an empty leaf node.
     */
    public LeafNode()
    {
        points = new ArrayList<Point>();
    }


    // ----------------------------------------------------------
    /**
     * Returns points stored in leaf
     * 
     * @return list of points
     */
    public ArrayList<Point> getPoints()
    {
        return points;
    }


    // ----------------------------------------------------------
    /**
     * check if points have same coordinates
     * 
     * @return true if same coordinates
     */
    public boolean allSameCoordinates()
    {
        if (points.size() <= 1)
        {
            return true;
        }

        Point first = points.get(0);

        for (Point point : points)
        {
            if (point.getX() != first.getX() || point.getY() != first.getY())
            {
                return false;
            }
        }

        return true;
    }


    /**
     * Inserts a point into this leaf.
     *
     * @param point
     *            point to insert
     * @param x
     *            x-coordinate
     * @param y
     *            y-coordinate
     * @param size
     *            width and height
     * @return updated node
     */
    @Override
    public QuadNode insert(Point point, int x, int y, int size)
    {
        points.add(point);

        if (points.size() <= 3)
        {
            return this;
        }

        if (allSameCoordinates())
        {
            return this;
        }

        InternalNode internal = new InternalNode();

        for (Point current : points)
        {
            internal.insert(current, x, y, size);
        }

        return internal;

    }

}
