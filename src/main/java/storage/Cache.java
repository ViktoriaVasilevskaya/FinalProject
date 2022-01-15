package storage;

import java.io.Serializable;

public interface Cache extends Serializable {
    public void add(String key, Object value, long periodInMillis);
    public Object get (String key);
    public void remove (String key);
    public void clear();
}
