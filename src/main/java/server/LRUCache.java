package server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.*;

class Node {
    String key;
    String value;

    public Node(String key, String value) {

        this.key = key;
        this.value = value;
    }
}
public class LRUCache{


    private Map<String, Node> searchMap;
    private int capacity;
    private LinkedList<Node> list;

    public LRUCache(int capacity) {
        this.capacity = capacity;

        searchMap = new HashMap ();
        list = new LinkedList<>();
    }

        public synchronized String get(String key) {

            if(searchMap.containsKey(key)){
                Node element = searchMap.get(key);
                list.remove(element);
                list.addFirst(element);
                return element.value;
            }else{
                return "null";
            }

    }

    public synchronized void put(String key, String value) {

            if(searchMap.containsKey(key)){
                Node element = searchMap.get(key);
                element.value = value;
                list.remove(element);
                list.addFirst(element);

            } else {
                if(list.size() < capacity){
                    Node element = new Node(key, value);
                    searchMap.put(key, element);
                    list.addFirst(element);
                } else {
                    searchMap.remove(list.getLast().key);
                    list.removeLast();
                    Node element = new Node(key, value);
                    searchMap.put(key, element);
                    list.addFirst(element);
                }
            }
        }
    public Boolean contains(String key) {
        return searchMap.containsKey(key);
    }
}
