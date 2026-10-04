import java.util.ArrayList;
import student.TestCase;
import student.TestableRandom;

/**
 * Tests the SkipList class.
 * 
 * @author Yosna
 * @version Oct 3, 2026
 */
public class SkipListTest
    extends TestCase
{
    private SkipList<String, String> list;

    /**
     * Sets up the SkipList before each test.
     */
    public void setUp()
    {
        list = new SkipList<String, String>();
    }


    /**
     * Tests creation of an empty SkipList.
     */
    public void testEmpty()
    {
        assertEquals(0, list.size());
    }


    /**
     * Tests inserting an element.
     */
    public void testInsert()
    {
        TestableRandom.setNextBooleans(false);

        KVPair<String, String> pair =
            new KVPair<String, String>("apple", "value1");

        list.insert(pair);

        assertEquals(1, list.size());
    }


    /**
     * Tests searching for an element.
     */
    public void testSearch()
    {
        TestableRandom.setNextBooleans(false);

        KVPair<String, String> pair =
            new KVPair<String, String>("apple", "value1");

        list.insert(pair);

        ArrayList<KVPair<String, String>> result =
            list.search("apple");

        assertEquals(1, result.size());
        assertEquals(pair, result.get(0));
    }


    /**
     * Tests searching for an element that does not exist.
     */
    public void testSearchMissing()
    {
        assertEquals(0, list.search("missing").size());
    }


    /**
     * Tests removing an element by key.
     */
    public void testRemove()
    {
        TestableRandom.setNextBooleans(false);

        KVPair<String, String> pair =
            new KVPair<String, String>("apple", "value1");

        list.insert(pair);

        assertEquals(pair, list.remove("apple"));
        assertEquals(0, list.size());
    }


    /**
     * Tests removing an element that does not exist.
     */
    public void testRemoveMissing()
    {
        assertNull(list.remove("missing"));
    }


    /**
     * Tests removing an element by value.
     */
    public void testRemoveByValue()
    {
        TestableRandom.setNextBooleans(false);

        KVPair<String, String> pair =
            new KVPair<String, String>("apple", "value1");

        list.insert(pair);

        assertEquals(pair, list.removeByValue("value1"));
        assertEquals(0, list.size());
    }
}
