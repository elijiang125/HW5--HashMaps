abstract class Dictionary<K,V> {
    public abstract boolean remove(K key);
    public abstract boolean containsKey(K key);
    public abstract V get(K key);
    public abstract void put(K key, V value);
}
