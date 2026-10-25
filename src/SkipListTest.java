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

        ArrayList<KVPair<String, String>> result = list.search("apple");

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


    /**
     * Tests inserting multiple values with the same key.
     */
    public void testDuplicateNames()
    {
        TestableRandom.setNextBooleans(false, false);

        list.insert(new KVPair<String, String>("apple", "value1"));

        list.insert(new KVPair<String, String>("apple", "value2"));

        ArrayList<KVPair<String, String>> results = list.search("apple");

        assertEquals(2, results.size());
    }


    /**
     * Tests removing one of two entries with the same key.
     */
    public void testRemoveDuplicateKey()
    {
        TestableRandom.setNextBooleans(false, false);

        list.insert(new KVPair<String, String>("apple", "one"));
        list.insert(new KVPair<String, String>("apple", "two"));

        assertNotNull(list.remove("apple"));
        assertEquals(1, list.size());
        assertEquals(1, list.search("apple").size());
    }


    /**
     * Tests removing a specific value when keys are duplicated.
     */
    public void testRemoveDuplicateValue()
    {
        TestableRandom.setNextBooleans(false, false);

        list.insert(new KVPair<String, String>("apple", "one"));
        list.insert(new KVPair<String, String>("apple", "two"));

        assertNotNull(list.removeByValue("one"));
        assertEquals(1, list.size());
        assertEquals("two", list.search("apple").get(0).value());
    }


    /**
     * Tests removing the first, middle, and last keys.
     */
    public void testRemoveAllKeys()
    {
        TestableRandom.setNextBooleans(false, false, false);

        list.insert(new KVPair<String, String>("apple", "one"));
        list.insert(new KVPair<String, String>("banana", "two"));
        list.insert(new KVPair<String, String>("cherry", "three"));

        assertNotNull(list.remove("banana"));
        assertNotNull(list.remove("apple"));
        assertNotNull(list.remove("cherry"));

        assertEquals(0, list.size());
        assertFalse(list.iterator().hasNext());
    }


    /**
     * Tests searching an empty list.
     */
    public void testSearchEmpty()
    {
        assertTrue(list.search("apple").isEmpty());
    }


    /**
     * Tests searching for first, middle, last, and nonexistent keys.
     */
    public void testSearchDifferentPositions()
    {
        list.insert(new KVPair<String, String>("apple", "A"));
        list.insert(new KVPair<String, String>("banana", "B"));
        list.insert(new KVPair<String, String>("cherry", "C"));

        assertEquals(1, list.search("apple").size());
        assertEquals(1, list.search("banana").size());
        assertEquals(1, list.search("cherry").size());

        assertTrue(list.search("aardvark").isEmpty());
        assertTrue(list.search("blueberry").isEmpty());
        assertTrue(list.search("zebra").isEmpty());
    }


    /**
     * Tests duplicate keys.
     */
    public void testSearchDuplicateKeys()
    {
        list.insert(new KVPair<String, String>("apple", "A"));
        list.insert(new KVPair<String, String>("apple", "B"));
        list.insert(new KVPair<String, String>("apple", "C"));

        assertEquals(3, list.search("apple").size());
    }


    /**
     * Tests removing keys from different positions.
     */
    public void testRemoveDifferentPositions()
    {
        list.insert(new KVPair<String, String>("apple", "A"));
        list.insert(new KVPair<String, String>("banana", "B"));
        list.insert(new KVPair<String, String>("cherry", "C"));

        assertNull(list.remove("missing"));

        assertNotNull(list.remove("banana"));
        assertNotNull(list.remove("apple"));
        assertNotNull(list.remove("cherry"));

        assertEquals(0, list.size());
        assertNull(list.remove("apple"));
    }


    /**
     * Tests removing one of multiple duplicate keys.
     */
    public void testRemoveDuplicateKeys()
    {
        list.insert(new KVPair<String, String>("apple", "A"));
        list.insert(new KVPair<String, String>("apple", "B"));

        assertNotNull(list.remove("apple"));
        assertEquals(1, list.size());

        assertNotNull(list.remove("apple"));
        assertEquals(0, list.size());

        assertNull(list.remove("apple"));
    }


    /**
     * Tests removing values from different positions.
     */
    public void testRemoveByValuePositions()
    {
        list.insert(new KVPair<String, String>("apple", "A"));
        list.insert(new KVPair<String, String>("banana", "B"));
        list.insert(new KVPair<String, String>("cherry", "C"));

        assertNull(list.removeByValue("missing"));

        assertNotNull(list.removeByValue("B"));
        assertNotNull(list.removeByValue("A"));
        assertNotNull(list.removeByValue("C"));

        assertEquals(0, list.size());
    }


    /**
     * Tests removing values from an empty list.
     */
    public void testRemoveByValueEmpty()
    {
        assertNull(list.removeByValue("A"));
    }


    /**
     * Tests dumping empty and populated lists.
     */
    public void testDumpCoverage()
    {
        list.dump();

        assertTrue(systemOut().getHistory().contains("SkipList size is: 0"));

        list.insert(new KVPair<String, String>("apple", "A"));
        list.insert(new KVPair<String, String>("banana", "B"));

        list.dump();

        assertTrue(systemOut().getHistory().contains("SkipList size is: 2"));
    }


    /**
     * Tests iterator behavior on empty and populated lists.
     */
    public void testIteratorCoverage()
    {
        java.util.Iterator<KVPair<String, String>> empty = list.iterator();

        assertFalse(empty.hasNext());

        list.insert(new KVPair<String, String>("apple", "A"));
        list.insert(new KVPair<String, String>("banana", "B"));

        java.util.Iterator<KVPair<String, String>> iter = list.iterator();

        assertTrue(iter.hasNext());
        assertEquals("apple", iter.next().key());

        assertTrue(iter.hasNext());
        assertEquals("banana", iter.next().key());

        assertFalse(iter.hasNext());
    }


    /**
     * Tests different randomly generated levels.
     */
    public void testRandomLevels()
    {
        student.TestableRandom.setNextBooleans(false);
        assertEquals(0, list.randomLevel());

        student.TestableRandom.setNextBooleans(true, false);
        assertEquals(1, list.randomLevel());

        student.TestableRandom.setNextBooleans(true, true, false);
        assertEquals(2, list.randomLevel());
    }


    /**
     * Tests inserting nodes with different randomly generated levels and
     * verifies that searching and removing nodes work correctly.
    */
    public void testDifferentNodeLevels()
    {
        student.TestableRandom
            .setNextBooleans(false, true, true, false, true, false, false);

        list.insert(new KVPair<String, String>("apple", "A"));
        list.insert(new KVPair<String, String>("banana", "B"));
        list.insert(new KVPair<String, String>("cherry", "C"));
        list.insert(new KVPair<String, String>("date", "D"));

        assertEquals(4, list.size());

        assertEquals(1, list.search("banana").size());

        assertNotNull(list.remove("banana"));
        assertNotNull(list.removeByValue("C"));

        assertEquals(2, list.size());
    }

}
