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


    /**
     * Searches for points inside a rectangular region.
     *
     * @param x
     *            rectangle x-coordinate
     * @param y
     *            rectangle y-coordinate
     * @param width
     *            rectangle width
     * @param height
     *            rectangle height
     */
    public void regionSearch(int x, int y, int width, int height)
    {
        if (width <= 0 || height <= 0)
        {
            System.out.println(
                "Rectangle rejected: (" + x + ", " + y + ", " + width + ", "
                    + height + ")");
            return;
        }

        System.out.println(
            "Points intersecting region (" + x + ", " + y + ", " + width + ", "
                + height + "):");

        int visited =
            searchRegionNode(root, 0, 0, WORLD_SIZE, x, y, width, height);

        System.out.println(visited + " quadtree nodes visited");
    }


    /**
     * Checks whether two rectangular regions intersect.
     *
     * @param x1
     *            rectangle 1 x-coordinate
     * @param y1
     *            rectangle 1 y-coordinate
     * @param w1
     *            rectangle 1 width
     * @param h1
     *            rectangle 1 height
     * @param x2
     *            rectangle 2 x-coordinate
     * @param y2
     *            rectangle 2 y-coordinate
     * @param w2
     *            rectangle 2 width
     * @param h2
     *            rectangle 2 height
     * @return true if the rectangles intersect
     */
    private boolean intersects(
        int x1,
        int y1,
        int w1,
        int h1,
        int x2,
        int y2,
        int w2,
        int h2)
    {
        return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
    }


    /**
     * Recursively searches nodes intersecting a rectangle.
     *
     * @param node
     *            current node
     * @param nodeX
     *            node x-coordinate
     * @param nodeY
     *            node y-coordinate
     * @param size
     *            node region size
     * @param x
     *            rectangle x-coordinate
     * @param y
     *            rectangle y-coordinate
     * @param width
     *            rectangle width
     * @param height
     *            rectangle height
     * @return number of Quadtree nodes visited
     */
    private int searchRegionNode(
        QuadNode node,
        int nodeX,
        int nodeY,
        int size,
        int x,
        int y,
        int width,
        int height)
    {
        int visited = 1;

        if (node instanceof EmptyNode)
        {
            return visited;
        }

        if (node instanceof LeafNode)
        {
            LeafNode leaf = (LeafNode)node;

            for (Point point : leaf.getPoints())
            {
                if (point.getX() >= x && point.getX() < (long)x + width
                    && point.getY() >= y && point.getY() < (long)y + height)
                {
                    System.out.println("Point found: " + point);
                }
            }

            return visited;
        }

        InternalNode internal = (InternalNode)node;
        int half = size / 2;

        if (intersects(nodeX, nodeY, half, half, x, y, width, height))
        {
            visited += searchRegionNode(
                internal.getNorthwest(),
                nodeX,
                nodeY,
                half,
                x,
                y,
                width,
                height);
        }

        if (intersects(nodeX + half, nodeY, half, half, x, y, width, height))
        {
            visited += searchRegionNode(
                internal.getNortheast(),
                nodeX + half,
                nodeY,
                half,
                x,
                y,
                width,
                height);
        }

        if (intersects(nodeX, nodeY + half, half, half, x, y, width, height))
        {
            visited += searchRegionNode(
                internal.getSouthwest(),
                nodeX,
                nodeY + half,
                half,
                x,
                y,
                width,
                height);
        }

        if (intersects(
            nodeX + half,
            nodeY + half,
            half,
            half,
            x,
            y,
            width,
            height))
        {
            visited += searchRegionNode(
                internal.getSoutheast(),
                nodeX + half,
                nodeY + half,
                half,
                x,
                y,
                width,
                height);
        }

        return visited;
    }

}
