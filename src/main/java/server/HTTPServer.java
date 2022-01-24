package server;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

public class HTTPServer {
    private final HttpServer server;
    private final int port;

    public HTTPServer(int port, ThreadPoolExecutor threadPoolExecutor, HandlerType type, LRUCache cache, ArrayList<NodeEndpoint> endpoints) throws IOException {
        this.port = port;
        server = HttpServer.create(new InetSocketAddress("localhost", port), 0);

        switch (type) {
            case CLIENT ->
                    server.createContext("/endpoint", new ClientHandler(cache, endpoints));
            case P2P ->
                    server.createContext("/endpoint", new p2pHandler(cache));
        }

        server.setExecutor(threadPoolExecutor);
    }
    public void start(){
        server.start();
    }


}
