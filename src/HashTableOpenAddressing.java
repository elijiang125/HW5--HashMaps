import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.abs;

public class HashTableOpenAddressing<K, V> extends Dictionary<K,V>{

    private int capacity;  // size of the table
    private int size;  // number of elements in the table
    private int previousPrime; //store prev prime so that it is not calculated again and again in double hashing.
    private int mode;

    public static int LINEARPROBING = 1;
    public static int QUADRATICPROBING = 2;
    public static int DOUBLEHASHING = 3;
    private double loadFactor;
    private Entry<K, V>[] table;

    public HashTableOpenAddressing() {
        this(DOUBLEHASHING, 11, 0.75);  // default initial capacity of 10
        this.table = new Entry[11];
        for (int i = 0; i < this.table.length; i++) {
            this.table[i] = null;
        }
        this.previousPrime = previousPrime(this.capacity);
    }

    public HashTableOpenAddressing(int mode) {
        this( mode, 11, 0.75);

        this.table = new Entry[11];
        for (int i = 0; i < this.table.length; i++) {
            this.table[i] = null;
        }
        this.previousPrime = previousPrime(this.capacity);
    }

    public HashTableOpenAddressing(int capacity, double loadFactor) {
        this(DOUBLEHASHING, capacity, loadFactor);

        this.previousPrime = previousPrime(this.capacity);
        this.table = new Entry[capacity];
        for (int i = 0; i < this.table.length; i++) {
            this.table[i] = null;
        }
    }

    /*
    TODO:
    This constructor takes a mode, capacity, loadFactor, and sets those variables + relevant variables
    according to such. Additionally, it will set up the table according to the capacity.
    If the mode is DOUBLEHASHING, please calculate the previousPrime and set it.
     */
    public HashTableOpenAddressing(int mode, int capacity, double loadFactor) {
        this.mode = mode;
        this.capacity = capacity;
        this.loadFactor = loadFactor;

        if (mode == DOUBLEHASHING) {
            this.previousPrime = previousPrime(this.capacity);
        }

        this.table = new Entry[capacity];

    }

    private int previousPrime(int number) {
        while( true ) {
            if( isPrime( number ) ) {
                return number;
            }
            number--;
        }
    }


    // TODO:
    //  second hash should be prevPrime - (key % prevPrime)...shouldn't be negative
    private int hash2(K key) {
        return Math.abs(this.previousPrime - (key.hashCode() % this.previousPrime));
    }


    public int getCapacity() {
        return this.capacity;
    }
    // TODO: gets the next index given the index and the offset. Please take into account the mode.
    private int getNextIndex(K key, int offset) {
        int currentIndex = hash(key);
        if (mode == LINEARPROBING) {
            return ((currentIndex + offset) % this.capacity);
        }
        else if (mode == QUADRATICPROBING) {
            return ((int)(currentIndex + Math.pow(offset, 2)) % this.capacity);
        }
        else if (mode == DOUBLEHASHING) {
            return (currentIndex + (offset * hash2(key))) % this.capacity;
        }
        else {
            return 0;
        }
    }

    // TODO:
    //  Put a key, value pair into the table.
    //  If the key already exists/inactive, override it. Else, put it into the table.
    //  Throw a RuntimeException if there is an infinite loops.
    public void put(K key, V value) {
        Entry newEntry = new Entry(key,value);

        if (size > loadFactor * capacity) {
            resize();
        }

        try {
                int index = hash(key);
                int offset = 1;
                if (this.table[index] != null) { //if value already exists
                    while (this.table[index] != null) {
                        index = getNextIndex(key, offset);
                        offset++;
                        if (offset > this.capacity) { // if we've searched the entire table
                            throw new RuntimeException("Infinite loop while inserting key " + key);
                        }
                    }
                    this.table[index] = newEntry;
                }
                else {
                    this.table[index] = newEntry;
                }
                this.size++;
        }
        catch(RuntimeException e) { //catch infinite loops
             throw new RuntimeException("Infinite Loop while inserting key" + key, e);
        }

    }

    // TODO:
    //  Retrieves the value of a key in the table.
    //  If there is an infinite loop, throw a RuntimeException.
    //  Return null if not there.
    public V get(K key) {

        try {
            if (!containsKey(key)) {
                //if key isn't in HashTable, return null
                return null;
            }
            else {
                int index = hash(key);
                return this.table[index].getValue();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Infinite Loop while inserting key" + key, e);
        }
    }

    // TODO: Searches the table to see if the key exists or not.
    public boolean containsKey(K key) {
        for (int i = 0; i < this.capacity; i++) {
            int index = getNextIndex(key, i);
            Entry entry = this.table[index];
            if (entry == null) {
                return false;
            }
            else if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    // TODO:
    //  Set the key as inactive if it exists in the table. Return true.
    //  If there is no key, return false.
    //  If there's an infinite loop, throw a RuntimeException.
    public boolean remove(K key) {
        try {
            if (!containsKey(key)) {
                //if key is not there, return false
                return false;
            }
            else {
                int index = hash(key);
                this.table[index] = null; //gets rid of the Entry, sets key as inactive too
                this.size--;
                return true;
            }
        } catch(RuntimeException e) {
            throw new RuntimeException("Infinite loop while inserting key" + key, e);
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // TODO:
    //  Calculate the absolute hash of the key. Do not overthink this.
    private int hash(K key) {
        return Math.abs(key.hashCode() % this.capacity);
    }


    private boolean isPrime(int number) {
        for( int i = 2; i <= number / 2; i++ ) {
            if( number % i == 0 ) {
                return false;
            }
        }
        return true;
    }

    private int nextPrime(int number) {
        while( true ) {
            if( isPrime( number ) ) {
                return number;
            }
            number++;
        }
    }

    // TODO:
    //  Set the capacity to the nextPrime of the capacity doubled.
    //  Calculate the previousPrime and set up the new table with the old tables'
    //  contents now hashed to the new.
    private void resize() {
        // find the next prime number twice that of capacity...
        int minNum = nextPrime(2 * this.capacity);

        // calculate previous prime
        int prevNum = previousPrime(minNum);

        Entry[] newTable = new Entry[minNum]; // new capacity

        // re-hash old entries into new table
        for (int i = 0; i < this.table.length; i++) {
            Entry entry = this.table[i];
            if (entry != null) {
                int newIndex = hash((K)entry.getKey());
                int offset = 1;
                while (newTable[newIndex] != null) {
                    newIndex = getNextIndex((K)entry.getKey(), offset++);
                }
                newTable[newIndex] = entry;
            }
        }

        this.table = newTable;
        this.capacity = minNum;
        this.previousPrime = prevNum;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        int index = 0;

        for (Entry<K, V> entry : table) {
            sb.append(index + ": ");
            index++;
            if (entry != null) {
                sb.append(entry.getKey() + "=" + entry.getValue() + ",");
            }
            sb.append(";");
        }

        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    private class Entry<K, V> {
        private K key;
        private V value;

        private boolean isActive;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
            isActive = true;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public boolean getActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }
    }

    public static void main(String []args ) {
        HashTableOpenAddressing<Integer, Integer> hashTable = new HashTableOpenAddressing<>(QUADRATICPROBING, 10, 1);

        hashTable.put(2,2);
        System.out.println(hashTable);
        for (int i = 0; i < 280; i += 10) {
            hashTable.put(i, i);
            hashTable.remove(0);
            System.out.println(hashTable.get(i));
        }
    }

}
