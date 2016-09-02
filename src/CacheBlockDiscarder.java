
/**
 * Created by Matthew Blake Hunter
 */
public abstract class CacheBlockDiscarder<BlockSetLookup, InsertableBlockSet> {

    abstract InsertableBlockSet discardBlockIn(BlockSetLookup lookup);

}
