import java.util.*;

/**
 * Created by Matthew Blake Hunter
 */
public class SetAssociativeCache<K, V> implements MemoryCache<K, V> {

    private final Map<K, CacheBlockSet<K, V>> blockSetLookup;
    private final int numberOfWays;
    private final int cacheCapacity;
    private final CacheBlockDiscarder<Map<K, CacheBlockSet<K, V>>, CacheBlockSet<K, V>> cacheBlockDiscarder;

    SetAssociativeCache(int numberOfWays, int cacheCapacity,
                        CacheBlockDiscarder<Map<K, CacheBlockSet<K, V>>, CacheBlockSet<K, V>> cacheBlockDiscarder) {
        this.numberOfWays        = numberOfWays;
        this.cacheCapacity       = cacheCapacity;
        this.cacheBlockDiscarder = cacheBlockDiscarder;
        this.blockSetLookup      = createEmptyBlockSetLookup();
    }

    private Map<K, CacheBlockSet<K, V>> createEmptyBlockSetLookup() {
        return Collections.synchronizedMap(new LinkedHashMap<K, CacheBlockSet<K, V>>(cacheCapacity));
    }

    public V get(K key) {
        CacheBlockSet<K, V> blockSet = blockSetLookup.get(key);
        if (blockSet != null) {
            return blockSet.getBlock(key).getValue();
        }
        return null;
    }

    public void put(K key, V value) {
        if (cacheIsFull()) {
            insertIntoFullCache(key, value);
        } else {
            addBlockSetToCache(key, value, insertableBlockSet());
        }
    }

    private void insertIntoFullCache(K key, V value) {
        CacheBlockSet<K, V> blockSet = blockSetLookup.get(key);
        if (blockSet != null) {
            blockSet.writeBlock(key, value);
        } else {
            CacheBlockSet<K, V> insertableBlockSet = cacheBlockDiscarder.discardBlockIn(blockSetLookup);
            addBlockSetToCache(key, value, insertableBlockSet);
        }
    }

    private void addBlockSetToCache(K key, V value, CacheBlockSet<K, V> blockSet) {
        if (blockSet != null) {
            blockSet.writeBlock(key, value);
        }
        blockSetLookup.put(key, blockSet);
    }

    private CacheBlockSet<K, V> insertableBlockSet() {
        for (CacheBlockSet<K, V> currentBlockSet : blockSetLookup.values()) {
            if(!currentBlockSet.isFull()) {
                return currentBlockSet;
            }
        }
        return new CacheBlockSetImpl<K, V>(numberOfWays);
    }

    private boolean cacheIsFull() {
        return blockSetLookup.size() >= cacheCapacity;
    }

    public void clear() {
        blockSetLookup.clear();
    }

}