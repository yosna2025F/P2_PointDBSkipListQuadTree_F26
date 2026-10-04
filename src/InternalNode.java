// -------------------------------------------------------------------------
/**
 *  Represents an internal node in the PR Quadtree.
 * 
 *  @author yosna
 *  @version Oct 3, 2026
 */
public class InternalNode implements QuadNode
{
    private QuadNode northwest;
    private QuadNode northeast;
    private QuadNode southwest;
    private QuadNode southeast;

    /**
     * Creates an InternalNode whose four children are empty nodes.
     */
    public InternalNode()
    {
        northwest = new EmptyNode();
        northeast = new EmptyNode();
        southwest = new EmptyNode();
        southeast = new EmptyNode();
    }


    /**
     * Gets the northwest child.
     * 
     * @return northwest child
     */
    public QuadNode getNorthwest()
    {
        return northwest;
    }


    /**
     * Gets the northeast child.
     * 
     * @return northeast child
     */
    public QuadNode getNortheast()
    {
        return northeast;
    }


    /**
     * Gets the southwest child.
     * 
     * @return southwest child
     */
    public QuadNode getSouthwest()
    {
        return southwest;
    }


    /**
     * Gets the southeast child.
     * 
     * @return southeast child
     */
    public QuadNode getSoutheast()
    {
        return southeast;
    }


    /**
     * Sets the northwest child.
     * 
     * @param node
     *            new northwest child
     */
    public void setNorthwest(QuadNode node)
    {
        northwest = node;
    }


    /**
     * Sets the northeast child.
     * 
     * @param node
     *            new northeast child
     */
    public void setNortheast(QuadNode node)
    {
        northeast = node;
    }


    /**
     * Sets the southwest child.
     * 
     * @param node
     *            new southwest child
     */
    public void setSouthwest(QuadNode node)
    {
        southwest = node;
    }


    /**
     * Sets the southeast child.
     * 
     * @param node
     *            new southeast child
     */
    public void setSoutheast(QuadNode node)
    {
        southeast = node;
    }
}
