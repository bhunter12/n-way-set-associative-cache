/**
 * Created by Matthew Blake Hunter
 */
class CacheBlockImpl<K, V> implements CacheBlock<K, V> {

    private final K key;
    private final V value;

    public CacheBlockImpl(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    public K getKey() {
        return key;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CacheBlockImpl) {
            CacheBlockImpl<K, V> that = (CacheBlockImpl<K, V>) object;
            return this.key.equals(that.key) &&
                   this.value.equals(that.value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + key.hashCode();
        hash = hash * 13 + value.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return "CacheBlockImpl(" + key + ", " + value + ")";
    }

}