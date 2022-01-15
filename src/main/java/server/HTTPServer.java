package server;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class HTTPServer {
    private final HttpServer server;
    private final ThreadPoolExecutor threadPoolExecutor;

    public HTTPServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        server.createContext("/test", new Handler());

        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        server.setExecutor(threadPoolExecutor);
    }
    public void start(){
        server.start();
    }

}
