import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HTCTest {
    // TODO: accuracy tests

    HashTableWithChaining HTC1 = new HashTableWithChaining<Integer, String>();

    HashTableWithChaining HTC2 = new HashTableWithChaining<Integer, String>(13);

    HashTableWithChaining HTC3 = new HashTableWithChaining<Integer, String>(13, 0.8);

    @Test
    public void test1() {
        int key1 = 3;
        int key2 = 24;
        String value1 = "apple";

        HTC1.put(key1, value1);
        assertEquals(value1, HTC1.get(key1));
        HTC1.put(4, "banana");
        assertEquals("banana", HTC1.get(4));

        HTC1.put(24, "orange"); //should return 2
        //System.out.println(Math.abs(key2.hashCode()) % 11);

        assertTrue(HTC1.containsKey(24));
        assertEquals("orange", HTC1.get(24));

        HTC1.put(14, "cucumber"); //should be in 3 position, chained with "apple"
        assertEquals("cucumber", HTC1.get(14));

        HTC1.remove(14);
        assertFalse(HTC1.containsKey(14));

        HTC1.put(8, "cucumber");
        HTC1.put(0, "credit");
        HTC1.put(1, "food");
        HTC1.put(6, "cool");
        HTC1.put(9, "confused");
        HTC1.put(10, "okay");
        HTC1.put(5, "believe");


        //checking if resize works or not
        assertEquals(23, HTC1.getCapacity());


    }
}
