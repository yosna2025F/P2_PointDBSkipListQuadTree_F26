import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import student.TestableRandom;

/**
 * This class implements SkipList data structure and contains an inner SkipNode
 * class which the SkipList will make an array of to store data.
 * 
 * @author Saanvi Movva
 * @version 2026-09-27
 * @param <K>
 *            Key
 * @param <V>
 *            Value
 */
public class SkipList<K extends Comparable<K>, V>
    implements Iterable<KVPair<K, V>>
{
    private SkipNode head; // First element (Sentinel Node)
    private int size; // number of entries in the Skip List
    private Random rng;

    /**
     * Initializes the fields head, size and level
     */
    public SkipList()
    {
        head = new SkipNode(null, 0);
        size = 0;
        this.rng = new TestableRandom();
    }


    /**
     * Generates a random level using a geometric distribution.
     *
     * @return the generated level
     */
    public int randomLevel()
    {
        int level = 1;
        while (rng.nextBoolean())
            level++;
        return level;
    }


    /**
     * Searches for the KVPair using the key which is a Comparable object.
     * 
     * @param key
     *            key to be searched for
     * @return a list containing all KVPairs with the specified key
     */
    public ArrayList<KVPair<K, V>> search(K key)
    {
        ArrayList<KVPair<K, V>> results = new ArrayList<KVPair<K, V>>();
        SkipNode current = head;

        for (int i = head.level; i >= 0; i--)
        {
            while (current.forward[i] != null
                && current.forward[i].element().key().compareTo(key) < 0)
            {
                current = current.forward[i];
            }
        }

        current = current.forward[0];

        while (current != null
            && current.element().key().compareTo(key) == 0)
        {
            results.add(current.element());
            current = current.forward[0];
        }

        return results;
    }


    /**
     * @return the size of the SkipList
     */
    public int size()
    {
        return size;
    }


    /**
     * Inserts the KVPair in the SkipList at its appropriate spot as designated
     * by its lexicoragraphical order.
     * 
     * @param it
     *            the KVPair to be inserted
     */
    @SuppressWarnings("unchecked")
    public void insert(KVPair<K, V> it)
    {
        int newLevel = randomLevel();
        if (newLevel > head.level)
        {
            adjustHead(newLevel);
        }

        SkipNode[] update =
            (SkipNode[])Array.newInstance(SkipNode.class, head.level + 1);
        SkipNode x = head;

        for (int i = head.level; i >= 0; i--)
        {
            while (x.forward[i] != null
                && x.forward[i].element().key().compareTo(it.key()) < 0)
            {
                x = x.forward[i];
            }
            update[i] = x;
        }

        x = new SkipNode(it, newLevel);

        for (int i = 0; i <= newLevel; i++)
        {
            x.forward[i] = update[i].forward[i];
            update[i].forward[i] = x;
        }
        size++;

    }


    /**
     * Increases the number of levels in head so that no element has more
     * indices than the head.
     * 
     * @param newLevel
     *            the number of levels to be added to head
     */
    @SuppressWarnings("unchecked")
    public void adjustHead(int newLevel)
    {
        SkipNode temp = head;
        head = new SkipNode(null, newLevel);
        for (int i = 0; i <= temp.level; i++)
        {
            head.forward[i] = temp.forward[i];
        }
    }


    /**
     * Removes one KVPair with the specified key from the SkipList.
     *
     * @param key
     *            the key of the KVPair to remove
     * @return the removed KVPair, or null if no matching key exists
     */
    @SuppressWarnings("unchecked")
    public KVPair<K, V> remove(K key)
    {
        SkipNode[] update =
            (SkipNode[])Array.newInstance(SkipNode.class, head.level + 1);

        SkipNode current = head;

        for (int i = head.level; i >= 0; i--)
        {
            while (current.forward[i] != null
                && current.forward[i].element().key().compareTo(key) < 0)
            {
                current = current.forward[i];
            }

            update[i] = current;
        }

        current = current.forward[0];

        if (current == null || current.element().key().compareTo(key) != 0)
        {
            return null;
        }

        KVPair<K, V> removed = current.element();

        for (int i = 0; i <= head.level; i++)
        {
            if (update[i].forward[i] == current)
            {
                update[i].forward[i] = current.forward[i];
            }
        }

        size--;

        return removed;
    }


    /**
     * Removes a KVPair with the specified value.
     * 
     * @param val
     *            the value of the KVPair to be removed
     * @return returns true if the removal was successful
     */
    public KVPair<K, V> removeByValue(V val)
    {

        SkipNode target = head.forward[0];

        while (target != null && !target.element().value().equals(val))
        {
            target = target.forward[0];
        }

        if (target == null)
        {
            return null;
        }

        KVPair<K, V> removed = target.element();

        for (int i = 0; i <= head.level; i++)
        {
            SkipNode current = head;

            while (current.forward[i] != null && current.forward[i] != target)
            {
                current = current.forward[i];
            }

            if (current.forward[i] == target)
            {
                current.forward[i] = target.forward[i];
            }
        }

        size--;

        return removed;
    }


    /**
     * Prints out the SkipList in a human readable format to the console.
     */
    public void dump()
    {
        System.out.println("SkipList dump:");

        SkipNode temp = head;

        System.out.println(
            "Node with depth " + (temp.level + 1) + ", Value "
                + temp.element());

        temp = head.forward[0];

        while (temp != null)
        {
            System.out.println(
                "Node with depth " + (temp.level + 1) + ", Value "
                    + temp.element());

            temp = temp.forward[0];
        }

        System.out.println("SkipList size is: " + size);
    }

    /**
     * This class implements a SkipNode for the SkipList data structure.
     * 
     * @author CS Staff
     * @version 2016-01-30
     */
    private class SkipNode
    {

        // the KVPair to hold
        private KVPair<K, V> pair;
        // An array of pointers to subsequent nodes
        private SkipNode[] forward;
        // the level of the node
        private int level;

        /**
         * Initializes the fields with the required KVPair and the number of
         * levels from the random level method in the SkipList.
         * 
         * @param tempPair
         *            the KVPair to be inserted
         * @param level
         *            the number of levels that the SkipNode should have
         */
        @SuppressWarnings("unchecked")
        public SkipNode(KVPair<K, V> tempPair, int level)
        {
            pair = tempPair;

            forward = (SkipNode[])Array.newInstance(SkipNode.class, level + 1);

            this.level = level;

        }


        /**
         * Returns the KVPair stored in the SkipList.
         * 
         * @return the KVPair
         */
        public KVPair<K, V> element()
        {
            return pair;
        }

    }


    // Iterates through the SkipList
    private class SkipListIterator
        implements Iterator<KVPair<K, V>>
    {
        private SkipNode current;

        // sets current node to head
        public SkipListIterator()
        {
            current = head;
        }


        // checks next node
        @Override
        public boolean hasNext()
        {
            return current.forward[0] != null;
        }


        // iterates through SkipList
        @Override
        public KVPair<K, V> next()
        {
            KVPair<K, V> elem = current.forward[0].element();
            current = current.forward[0];
            return elem;
        }

    }

    // Creates a new Iterator object
    @Override
    public Iterator<KVPair<K, V>> iterator()
    {
        return new SkipListIterator();
    }

}