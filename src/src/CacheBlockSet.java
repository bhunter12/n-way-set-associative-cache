import java.util.List;

/**
 * Created by Matthew Blake Hunter
 */
public interface CacheBlockSet<K, V> {

    CacheBlock<K, V> getBlock(K key);

    List<CacheBlock<K, V>> getBlocks();

    void writeBlock(K key, V value);

    boolean isFull();

    void remove(CacheBlock<K, V> block);

}
