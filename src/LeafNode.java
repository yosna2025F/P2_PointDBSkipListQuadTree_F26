
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

    ***

    Returns the
    points stored in this leaf.**@return
    list of points*/

   public ArrayList<Point> getPoints()
   {
       return points;
   }


   /**
    * Checks whether all points have identical coordinates.
    *
    * @return true if all coordinates match
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
           if (point.getX() != first.getX()
               || point.getY() != first.getY())
           {
               return false;
           }
       }

       return true;
   }


   /**
    * Inserts a point into this leaf.
    * Splits the leaf if it contains more than three
    * points at different coordinates.
    *
    * @param point the point to insert
    * @param x region starting x-coordinate
    * @param y region starting y-coordinate
    * @param size region width and height
    * @return updated node
    */
   @Override
   public QuadNode insert(Point point, int x, int y, int size)
   {
       points.add(point);

       // A leaf can hold up to three points.
       if (points.size() <= 3)
       {
           return this;
       }

       // More than three points can stay together
       // when all coordinates are identical.
       if (allSameCoordinates())
       {
           return this;
       }

       // Split the leaf into an internal node.
       InternalNode internal = new InternalNode();

       // Redistribute every point into the new node.
       for (Point current : points)
       {
           internal.insert(current, x, y, size);
       }

       return internal;
   }
}
