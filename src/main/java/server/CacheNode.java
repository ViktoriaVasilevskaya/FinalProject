package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

public class CacheNode {
    private LRUCache lru;
    HTTPServer clientServer;
    HTTPServer p2pServer;
    private ThreadPoolExecutor threadPoolExecutor;

    int clientPort;
    int p2pPort;

    public void init(int clientPort, int p2pPort, ArrayList<NodeEndpoint> endpoints) {
        lru = new LRUCache(20);
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        try {
            clientServer = new HTTPServer(clientPort, threadPoolExecutor, HandlerType.CLIENT, lru, endpoints);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            p2pServer = new HTTPServer(p2pPort, threadPoolExecutor, HandlerType.P2P, lru, endpoints);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        clientServer.start();
        p2pServer.start();
    }
}
