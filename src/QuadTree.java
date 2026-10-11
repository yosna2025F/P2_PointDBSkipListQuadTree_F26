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


    /**
     * Prints the Quadtree in preorder.
     */
    public void dump()
    {
        System.out.println("QuadTree dump:");
        int count = dumpNode(root, 0, 0, WORLD_SIZE, 0);
        System.out.println(count + " quadtree nodes printed");
    }


    /**
     * Recursively prints a node and its children.
     *
     * @param node
     *            current node
     * @param x
     *            region x-coordinate
     * @param y
     *            region y-coordinate
     * @param size
     *            region size
     * @param depth
     *            indentation depth
     * @return number of nodes printed
     */
    private int dumpNode(QuadNode node, int x, int y, int size, int depth)
    {
        String indent = "";

        for (int i = 0; i < depth; i++)
        {
            indent += "  ";
        }

        String location = "Node at " + x + ", " + y + ", " + size + ":";

        if (node instanceof EmptyNode)
        {
            System.out.println(indent + location + " Empty");
            return 1;
        }

        if (node instanceof LeafNode)
        {
            System.out.println(indent + location);

            LeafNode leaf = (LeafNode)node;

            for (Point point : leaf.getPoints())
            {
                System.out.println(indent + point);
            }

            return 1;
        }

        System.out.println(indent + location + " Internal");

        InternalNode internal = (InternalNode)node;
        int half = size / 2;
        int count = 1;

        count += dumpNode(internal.getNorthwest(), x, y, half, depth + 1);

        count +=
            dumpNode(internal.getNortheast(), x + half, y, half, depth + 1);

        count +=
            dumpNode(internal.getSouthwest(), x, y + half, half, depth + 1);

        count += dumpNode(
            internal.getSoutheast(),
            x + half,
            y + half,
            half,
            depth + 1);

        return count;
    }


    /**
     * Prints locations containing multiple points.
     */
    public void duplicates()
    {
        System.out.println("Duplicate points:");
        printDuplicates(root);
    }


    /**
     * Recursively searches for duplicate locations.
     *
     * @param node
     *            current Quadtree node
     */
    private void printDuplicates(QuadNode node)
    {
        if (node instanceof EmptyNode)
        {
            return;
        }

        if (node instanceof InternalNode)
        {
            InternalNode internal = (InternalNode)node;

            printDuplicates(internal.getNorthwest());
            printDuplicates(internal.getNortheast());
            printDuplicates(internal.getSouthwest());
            printDuplicates(internal.getSoutheast());
            return;
        }

        LeafNode leaf = (LeafNode)node;
        java.util.ArrayList<Point> points = leaf.getPoints();

        for (int i = 0; i < points.size(); i++)
        {
            Point current = points.get(i);
            boolean alreadyPrinted = false;
            int matches = 0;

            for (int j = 0; j < points.size(); j++)
            {
                Point other = points.get(j);

                if (current.getX() == other.getX()
                    && current.getY() == other.getY())
                {
                    matches++;

                    if (j < i)
                    {
                        alreadyPrinted = true;
                    }
                }
            }

            if (matches > 1 && !alreadyPrinted)
            {
                System.out.println(
                    "(" + current.getX() + ", " + current.getY() + ")");
            }
        }
    }

}
