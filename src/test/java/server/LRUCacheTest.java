package server;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {
    private LRUCache lru;

    @BeforeEach
    void setUp() {
        lru = new LRUCache(2);
    }

    @org.junit.jupiter.api.Test
    void put() {
        lru.put("first_key","first_value");
        lru.put ("second_key", "second_value");
        lru.put ("third_key", "third_value");
        if("null" != lru.get("first_key")){
            System.out.println("error");
        }

    }

}