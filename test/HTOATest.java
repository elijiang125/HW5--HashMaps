import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HTOATest {
    // TODO: accuracy tests

    //Standard, ONLY doublehashing
    HashTableOpenAddressing HTOA1 = new HashTableOpenAddressing<Integer, String>();

    //Each mode
    HashTableOpenAddressing HTOA2 = new HashTableOpenAddressing<Integer, String>(1);

    HashTableOpenAddressing HTOA3 = new HashTableOpenAddressing<Integer, String>(2);

    HashTableOpenAddressing HTOA4 = new HashTableOpenAddressing<Integer, String>(3);

    //int capacity and loadFactor ONLY for double hashing
    HashTableOpenAddressing HTOA5 = new HashTableOpenAddressing<Integer, String>(13, 0.8);


    //All variables involved
    HashTableOpenAddressing HTOA6 = new HashTableOpenAddressing<Integer, String>(1, 13, 0.8);

    HashTableOpenAddressing HTOA7 = new HashTableOpenAddressing<Integer, String>(2, 17, 0.6);

    HashTableOpenAddressing HTOA8 = new HashTableOpenAddressing<Integer, String>(3, 19, 0.75);

    @Test
    public void test1() { //capacity: 11, loadFactor: 0.75
        HTOA1.put(0, "apple");
        assertTrue(HTOA1.containsKey(0));
        assertEquals("apple", HTOA1.get(0));

        HTOA1.put(1, "banana");
        assertTrue(HTOA1.containsKey(1));
        assertEquals("banana", HTOA1.get(1));

        HTOA1.put(2, "orange");
        assertTrue(HTOA1.containsKey(2));
        assertEquals("orange", HTOA1.get(2));

        HTOA1.put(14, "nina"); //index 3
        assertTrue(HTOA1.containsKey(14));
        assertEquals("nina", HTOA1.get(14));

        HTOA1.put(15, "elizabeth"); //index 4
        assertTrue(HTOA1.containsKey(15));
        assertEquals("elizabeth", HTOA1.get(15));



    }


}
