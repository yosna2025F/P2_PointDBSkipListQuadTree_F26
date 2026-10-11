// -------------------------------------------------------------------------
/**
 * Represents a node in the PR Quadtree. All node types in the Quadtree
 * implement this interface.
 * 
 * @author yosna
 * @version Oct 3, 2026
 */
public interface QuadNode
{
    // ----------------------------------------------------------
    /**
     * Inserts a point into node region
     * 
     * @param point
     *            to insert at
     * @param x
     *            starting x-coordinate
     * @param y
     *            starting y-coordinate
     * @param size
     *            height and width
     * @return updated node
     */
    QuadNode insert(Point point, int x, int y, int size);
}
