import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HashTableWithChaining<K, V> extends Dictionary<K,V>{

    private int capacity;  // size of the table
    private int size;  // number of elements in the table

    private double loadFactor;
    private List<LinkedList<Entry<K, V>>> table;  // hash table

    // Entry class to hold key-value pairs
    private class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
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
    }

    public HashTableWithChaining() {
        this(11, 0.75);  // default initial capacity of 10


    }

    public HashTableWithChaining(int capacity) {
        this( capacity, 0.75);

    }

    /*
    TODO:
    This constructor takes a capacity and loadFactor, and sets those variables + relevant variables
    according to such. Additionally, it will set up the table according to the capacity.
     */
    public HashTableWithChaining(int capacity, double loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.table = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            table.add(new LinkedList<Entry<K, V>>());
        }
        this.size = 0;
    }

    // TODO:
    //  Put a key, value pair into the table.
    //  If the key already exists, update it with the new value.
    //  If there is no key at that index, add it into the table.
    //  Resize when the size is > the loadFactor * capacity. (DONE)
    //  Remember that multiple keys can exist at the same index.
    public void put(K key, V value) {
        //resize when size is above loadFactor * capacity
        int index = 0;
        LinkedList<Entry<K, V>> listToPut;
        if (size > loadFactor * capacity) {
            resize();
        }
        //1. find the index through hash
        index = hash(key);
        listToPut = this.table.get(index); //get the linked list from the index

        //2. search if the key already exists, if so, update it with the new value
        for (Entry<K, V> entry : listToPut) {
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
                return;
            }
        }

        //3. if the key doesn't exist, add a new entry to the end of the linked list
        listToPut.add(new Entry<K, V>(key, value));
        this.size++;

    }

    public int getCapacity() {
        return this.capacity;
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
        //find the next prime number twice that of capacity...
        int minNum = nextPrime(2 * this.capacity);

        //so we found the minimum prime number, now we have to do the following steps:
        //1. Create a new Hashtable with the desired capacity
        //2. Iterate through each entry in the existing Hashtable and re-hash it into the new Hashtable
        //3. Replace the existing Hashtable with the new Hashtable

        List<LinkedList<Entry<K, V>>> newTable = new ArrayList<>(capacity); //new capacity
        for (int i = 0; i < minNum; i++) {
            newTable.add(new LinkedList<>());
            //adds a new linked list for now
        }

        //for each linkedlist in the list of linkedlists
        //AND for each entry in these linkedlists
        //get the hashcode as the key, and the index to find which linkedlist
        for (LinkedList<Entry<K, V>> bucket: this.table) {
            for (Entry<K,V> entry: bucket) {
                int index = hash(entry.getKey());
                newTable.get(index).add(entry);
                //get function to find linkedlist, add entry on the linkedlist
            }
        }

        this.table = newTable;
        this.capacity = minNum;


    }


    // TODO:
    //  Retrieves the value of a key in the table.
    //  Return null if not there.
    public V get(K key) {
        V value = null;
        if (!containsKey(key)) {
            //if key isn't in HashTable, return null
            return null;
        }

        int index = hash(key);
        LinkedList<Entry<K, V>> list = this.table.get(index);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getKey().equals(key)) {
                value = list.get(i).getValue();
                break;
            }
        }

        return value;
    }

    // TODO: Searches the table to see if the key exists or not.
    public boolean containsKey(K key) {
        int index = hash(key);
        //if out of bounds, return false
        if (index < 0 || index >= this.capacity) {
            return false;
        }
        LinkedList<Entry<K, V>> list = this.table.get(index);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    // TODO:
    //  Remove the entry under that key. Return true.
    //  If there is no key, return false.
    public boolean remove(K key) {
        if (!containsKey(key)) {
            return false;
        }
        else {

            //this is the actual deletion process
            int index = hash(key);
            LinkedList<Entry<K, V>> list = this.table.get(index);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getKey().equals(key)) {
                    Entry removedEntry = list.get(i);
                    list.remove(removedEntry);
                    //remove first value from the list
                    this.size--;
                    return true;
                }
            }
        }
        return false;

    }

    public void clear() {
        for (LinkedList<Entry<K, V>> list : table) {
            list.clear();
        }
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    // TODO: Calculate the absolute hash of the key. Do not overthink this.
    private int hash(K key) {
        System.out.println(Math.abs(key.hashCode() % this.capacity));
        return Math.abs(key.hashCode()) % this.capacity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        int index = 0;
        if (this.table == null) {
            return "";
        }
        for (LinkedList<Entry<K, V>> list : this.table) {
            if(list.size() > 0 ) {
                sb.append(index + ": ");
                for (Entry<K, V> entry : list) {
                    sb.append(entry.getKey() + "=" + entry.getValue() + ",");
                }
                index++;
                sb.append(";");
            }
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }


    public static void main(String []args ) {
        HashTableWithChaining<String, Integer> hashTable = new HashTableWithChaining<>();

        hashTable.put("Hi", 2);
        hashTable.put("Ih", 2);
        hashTable.put("Hit", 2);
        hashTable.put("Him", 20);
        hashTable.put("His", 1);
        hashTable.put("Hiasdasd", 2);
        hashTable.put("Hiasdasds", 1);
        hashTable.put("Hiasdasadsd", 2);
        hashTable.put("H12is", 1);
        hashTable.put("H123iasdasd", 2);
        hashTable.put("Hita123s1d3asads", 2);
        System.out.println(hashTable);

    }
}

