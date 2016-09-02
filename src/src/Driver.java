import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Matthew Blake Hunter
 */
public class Driver {

    public static void main(String[] args) {

        // LRU Algorithm
        // If unused block is available then write to it
        // Else purge the least recently used by discarding the head block off of the insertion ordered list of blocks
        CacheBlockDiscarder<Map<Integer, CacheBlockSet<Integer, Integer>>,
                            CacheBlockSet<Integer, Integer>> lruCacheBlockDiscarder =
                new CacheBlockDiscarder<Map<Integer, CacheBlockSet<Integer, Integer>>,
                                        CacheBlockSet<Integer, Integer>>() {
            public synchronized CacheBlockSet<Integer, Integer> discardBlockIn(Map<Integer, CacheBlockSet<Integer, Integer>> blockSetLookup) {
                final Iterator<Map.Entry<Integer, CacheBlockSet<Integer, Integer>>> blocksIterator = blockSetLookup.entrySet().iterator();
                CacheBlockSet<Integer, Integer> leastRecentlyUsedBlockSet = null;
                if (blocksIterator.hasNext()) {
                    leastRecentlyUsedBlockSet = blocksIterator.next().getValue();
                    CacheBlock<Integer, Integer> leastRecentlyUsedBlock = leastRecentlyUsedBlockSet.getBlocks().get(0);
                    blockSetLookup.remove(leastRecentlyUsedBlock.getKey());
                    leastRecentlyUsedBlockSet.remove(leastRecentlyUsedBlock);

                }
                return leastRecentlyUsedBlockSet;
            }
        };

        // MRU Algorithm
        // If unused block is available then write to it
        // Else purge the most recently used by discarding the tail block off of the insertion ordered list of blocks
        CacheBlockDiscarder<Map<Integer, CacheBlockSet<Integer, Integer>>,
                            CacheBlockSet<Integer, Integer>> mruCacheBlockDiscarder =
                new CacheBlockDiscarder<Map<Integer,CacheBlockSet<Integer, Integer>>,
                                        CacheBlockSet<Integer,Integer>>() {
            public synchronized CacheBlockSet<Integer, Integer> discardBlockIn(Map<Integer, CacheBlockSet<Integer, Integer>> blockSetLookup){
                final Iterator<Map.Entry<Integer, CacheBlockSet<Integer, Integer>>> blocksIterator = blockSetLookup.entrySet().iterator();
                CacheBlockSet<Integer, Integer> mostRecentlyUsedBlockSet = null;
                while (blocksIterator.hasNext()) {
                    mostRecentlyUsedBlockSet = blocksIterator.next().getValue();
                }
                if (mostRecentlyUsedBlockSet != null) {
                    List<CacheBlock<Integer, Integer>> blocks = mostRecentlyUsedBlockSet.getBlocks();
                    CacheBlock<Integer, Integer> mostRecentlyUsedBlock = blocks.get(blocks.size() - 1);
                    blockSetLookup.remove(mostRecentlyUsedBlock.getKey());
                    mostRecentlyUsedBlockSet.remove(mostRecentlyUsedBlock);
                }
                return mostRecentlyUsedBlockSet;
            }
        };

        SetAssociativeCache<Integer, Integer> lruSetAssociativeCache =
                new SetAssociativeCache<Integer, Integer>(2, 8, lruCacheBlockDiscarder);
        SetAssociativeCache<Integer, Integer> mruSetAssociativeCache =
                new SetAssociativeCache<Integer, Integer>(2, 8, mruCacheBlockDiscarder);

        System.out.println("// WRITING 10 ITEMS TO AN LRU CACHE WITH 8 BLOCKS AND 2 WAY");

        // LRU TEST (10 inserts with only 8 cacheblocks 2 ways so we have to discard two cacheblocks from the tail of the list)
        for (int i = 0; i < 10; i++) {
            System.out.println("lruSetAssociativeCache.put(" + i + ", " + i + "): ");
            lruSetAssociativeCache.put(new Integer(i), new Integer(i));
        }

        System.out.println();
        System.out.println("// READING 10 ITEMS FROM AN LRU CACHE WITH 8 BLOCKS AND 2 WAY");

        for (int i = 0; i < 10; i++) {
            System.out.println("lruSetAssociativeCache.get(" + i + "): " + lruSetAssociativeCache.get(new Integer(i)));
        }

        System.out.println();
        System.out.println("// WRITING 10 ITEMS TO A MRU CACHE WITH 8 BLOCKS AND 2 WAY");

        // MRU TEST (10 inserts with only 8 cacheblocks 2 ways so we have to discard two cacheblocks from the head of the list)
        for (int i = 0; i < 10; i++) {
            System.out.println("mruSetAssociativeCache.put(" + i + ", " + i + "): ");
            mruSetAssociativeCache.put(new Integer(i), new Integer(i));
        }

        System.out.println();
        System.out.println("// READING FROM 10 ITEMS FROM A MRU CACHE WITH 8 BLOCKS AND 2 WAY");

        for (int i = 0; i < 10; i++) {
            System.out.println("mruSetAssociativeCache.get(" + i + "): " + mruSetAssociativeCache.get(new Integer(i)));
        }

    }

}
