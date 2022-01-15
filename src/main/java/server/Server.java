package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storage.Cache;

public class Server implements AutoCloseable{
    private final static Logger log = LoggerFactory.getLogger(Server.class);

    private final Cache cache;
    private final int port;

    public Server(Cache cache, int port) {
        this.cache = cache;
        this.port = port;
    }


    @Override
    public void close() throws Exception {

    }
}
