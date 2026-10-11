// -------------------------------------------------------------------------
/**
 *  Represents a point in the database.
 * 
 *  @author yosna
 *  @version Oct 11, 2026
 */
public class Point 
{
    //~ Fields ................................................................
    private final String name;
    private final int x;
    private final int y;

    
    
    //~ Constructors ..........................................................
    /**
     * Creates a point.
     *
     * @param name point name
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Point(String name, int x, int y) 
    {
        this.name = name;
        this.x = x;
        this.y = y;
    }
    
    
    
    //~Public  Methods ........................................................
    public String getName() 
    {
        return name;
    }

    public int getX() 
    {
        return x;
    }

    public int getY() 
    {
        return y;
    }

    @Override
    public String toString() 
    {
        return "(" + name + ", " + x + ", " + y + ")";
    }
    
    
    
}
