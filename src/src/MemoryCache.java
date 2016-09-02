/**
 * Created by Matthew Blake Hunter
 */
public interface MemoryCache<K, V> {

    void put(K key, V value);

    V get(K key);

    void clear();

}
