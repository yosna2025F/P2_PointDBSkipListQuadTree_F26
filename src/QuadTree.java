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


    /**
     * Removes a point from Quadtree.
     *
     * @param point
     *            point to remove
     * @return true if point was removed
     */
    public boolean remove(Point point)
    {
        if (point == null)
        {
            return false;
        }

        Point[] removed = new Point[1];

        root = removeNode(
            root,
            point.getX(),
            point.getY(),
            point,
            0,
            0,
            WORLD_SIZE,
            removed);

        return removed[0] != null;
    }


    /**
     * Removes point at coordinates.
     *
     * @param x
     *            x-coordinate
     * @param y
     *            y-coordinate
     * @return removed point
     */
    public Point remove(int x, int y)
    {
        if (x < 0 || x >= WORLD_SIZE || y < 0 || y >= WORLD_SIZE)
        {
            return null;
        }

        Point[] removed = new Point[1];

        root = removeNode(root, x, y, null, 0, 0, WORLD_SIZE, removed);

        return removed[0];
    }


    /**
     * Recursively removes a point from the Quadtree.
     *
     * @param node
     *            current node
     * @param pointX
     *            target x-coordinate
     * @param pointY
     *            target y-coordinate
     * @param target
     *            point for coordinate removal
     * @param x
     *            current region x-coordinate
     * @param y
     *            current region y-coordinate
     * @param size
     *            current region size
     * @param removed
     *            stores the removed point
     * @return updated node
     */
    private QuadNode removeNode(
        QuadNode node,
        int pointX,
        int pointY,
        Point target,
        int x,
        int y,
        int size,
        Point[] removed)
    {
        if (node instanceof EmptyNode)
        {
            return node;
        }

        if (node instanceof LeafNode)
        {
            LeafNode leaf = (LeafNode)node;

            for (int i = 0; i < leaf.getPoints().size(); i++)
            {
                Point current = leaf.getPoints().get(i);

                boolean matches;

                if (target != null)
                {
                    matches = current == target;
                }
                else
                {
                    matches =
                        current.getX() == pointX && current.getY() == pointY;
                }

                if (matches)
                {
                    removed[0] = leaf.getPoints().remove(i);

                    if (leaf.getPoints().isEmpty())
                    {
                        return EmptyNode.getInstance();
                    }

                    return leaf;
                }
            }

            return leaf;
        }

        InternalNode internal = (InternalNode)node;

        int half = size / 2;
        int middleX = x + half;
        int middleY = y + half;

        if (pointX < middleX)
        {
            if (pointY < middleY)
            {
                internal.setNorthwest(
                    removeNode(
                        internal.getNorthwest(),
                        pointX,
                        pointY,
                        target,
                        x,
                        y,
                        half,
                        removed));
            }
            else
            {
                internal.setSouthwest(
                    removeNode(
                        internal.getSouthwest(),
                        pointX,
                        pointY,
                        target,
                        x,
                        middleY,
                        half,
                        removed));
            }
        }
        else
        {
            if (pointY < middleY)
            {
                internal.setNortheast(
                    removeNode(
                        internal.getNortheast(),
                        pointX,
                        pointY,
                        target,
                        middleX,
                        y,
                        half,
                        removed));
            }
            else
            {
                internal.setSoutheast(
                    removeNode(
                        internal.getSoutheast(),
                        pointX,
                        pointY,
                        target,
                        middleX,
                        middleY,
                        half,
                        removed));
            }
        }

        if (removed[0] == null)
        {
            return internal;
        }

        return collapseNode(internal);
    }


    /**
     * Collapses an internal node when children can be combined into one leaf.
     *
     * @param internal
     *            internal nod
     * @return collapsed leaf, empty node, or original node
     */
    private QuadNode collapseNode(InternalNode internal)
    {
        QuadNode nw = internal.getNorthwest();
        QuadNode ne = internal.getNortheast();
        QuadNode sw = internal.getSouthwest();
        QuadNode se = internal.getSoutheast();

        if (nw instanceof InternalNode || ne instanceof InternalNode
            || sw instanceof InternalNode || se instanceof InternalNode)
        {
            return internal;
        }

        LeafNode combined = new LeafNode();

        addLeafPoints(nw, combined);
        addLeafPoints(ne, combined);
        addLeafPoints(sw, combined);
        addLeafPoints(se, combined);

        if (combined.getPoints().isEmpty())
        {
            return EmptyNode.getInstance();
        }

        if (combined.getPoints().size() <= 3 || combined.allSameCoordinates())
        {
            return combined;
        }

        return internal;
    }


    /**
     * Adds points from a leaf to another leaf.
     *
     * @param node
     *            node containing points
     * @param combined
     *            leaf getting the points
     */
    private void addLeafPoints(QuadNode node, LeafNode combined)
    {
        if (node instanceof LeafNode)
        {
            LeafNode leaf = (LeafNode)node;

            combined.getPoints().addAll(leaf.getPoints());
        }
    }
    
    

}
