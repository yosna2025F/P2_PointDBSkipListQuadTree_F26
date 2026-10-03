// -------------------------------------------------------------------------
/**
 * Represents the PR Quadtree used to organize points by their spatial location.
 * 
 * @author Saanvi
 * @version Oct 3, 2026
 */
public class QuadTree
{
    private QuadNode root;

    /**
     * Creates an empty Quadtree.
     */
    public QuadTree()
    {
        root = new EmptyNode();
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
}
