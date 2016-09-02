import java.util.*;

/**
 * Created by Matthew Blake Hunter
 */
class CacheBlockSetImpl<K, V> implements CacheBlockSet<K, V> {

    private final Map<K, CacheBlock<K, V>> blocks;
    private final int blockSetCapacity;

    public CacheBlockSetImpl(int blockSetCapacity) {
        this.blockSetCapacity = blockSetCapacity;
        this.blocks = initializeBlockSet(blockSetCapacity);
    }

    private Map<K, CacheBlock<K, V>> initializeBlockSet(int blockSetCapacity) {
        if (blockSetCapacity < 1) {
            throw new RuntimeException("BlockSet capacity cannot be less than one (1)");
        }
        return Collections.synchronizedMap(new LinkedHashMap<K, CacheBlock<K, V>>(blockSetCapacity));
    }

    public CacheBlock<K, V> getBlock(K key) {
        CacheBlock<K, V> block = blocks.get(key);
        if (block != null) {
            makeMostRecentlyUsed(key, block);
        }
        return block;
    }

    public void writeBlock(K key, V value) {
        CacheBlock<K, V> block = blocks.get(key);
        if (block != null) {
            makeMostRecentlyUsed(key, block);
            return;
        }
        blocks.put(key, new CacheBlockImpl<K, V>(key, value));
    }

    private void makeMostRecentlyUsed(K key, CacheBlock<K, V> block) {
        blocks.remove(key);
        blocks.put(key, block);
    }

    public List<CacheBlock<K, V>> getBlocks() {
        return Collections.synchronizedList(new ArrayList<CacheBlock<K, V>>(blocks.values()));
    }

    public boolean isFull() {
        return blocks.size() == blockSetCapacity;
    }

    @Override
    public void remove(CacheBlock<K, V> block) {
        blocks.remove(block.getKey());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CacheBlockSetImpl) {
            CacheBlockSetImpl<K, V> that = (CacheBlockSetImpl<K, V>) object;
            return this.blocks.equals(that.blocks) &&
                   this.blockSetCapacity == that.blockSetCapacity;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + blocks.hashCode();
        hash = hash * 13 + blockSetCapacity;
        return hash;
    }

    @Override
    public String toString() {
        return "CacheBlockSetImpl(" + blocks + ", " + blockSetCapacity + ")";
    }

}
