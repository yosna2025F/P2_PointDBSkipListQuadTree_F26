// -------------------------------------------------------------------------
/**
 * Represents an empty node in the PR Quadtree.
 * 
 * @author Saanvi
 * @version Oct 3, 2026
 */

public class EmptyNode
    implements QuadNode
{
    private static final EmptyNode INSTANCE = new EmptyNode();

    /**
     * Creates an empty node.
     */
    private EmptyNode()
    {
        // fill
    }


    /**F
     * Returns the shared empty node instance.
     *
     * @return shared EmptyNode
     */
    public static EmptyNode getInstance()
    {
        return INSTANCE;
    }


    /**
     * Inserts point into empty region
     *
     * @param point
     *            point to insert
     * @param x
     *            x-coordinate
     * @param y
     *            y-coordinate
     * @param size
     *            width and height
     * @return new leaf with point
     */
    @Override
    public QuadNode insert(Point point, int x, int y, int size)
    {
        LeafNode leaf = new LeafNode();
        return leaf.insert(point, x, y, size);
    }
}
