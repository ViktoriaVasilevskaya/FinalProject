package storage;

import java.util.Map;
import java.util.LinkedHashMap;

public final class SimpleLRUCache extends LinkedHashMap{
    private int maxSize;
    public SimpleLRUCache(final int maxSize) {
        super(1, 0.75f, true);
        this.maxSize = maxSize;
    }
    protected boolean removeEldestEntry(final Map.Entry eldest) {
        return size() > maxSize;
    }
}
