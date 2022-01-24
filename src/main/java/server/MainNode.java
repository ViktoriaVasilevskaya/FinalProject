package server;

import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

enum HandlerType {
    CLIENT,
    P2P
}

class NodeEndpoint {
    public String host;
    public String port;

    public NodeEndpoint(String host, String port) {
        this.host = host;
        this.port = port;
    }
}

public class MainNode {
    public static void main (String [] args){
        CacheNode node = new CacheNode();

        ArrayList<NodeEndpoint> endpoints = new ArrayList<NodeEndpoint>();
        if (args.length < 2) {
            System.out.println("Usage: 2 arguments: client port, p2p port");
            return;
        }
        int clientPort = 0;
        int p2pPort = 0;
        try {
            clientPort = Integer.parseInt(args[0]);
            p2pPort = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
           System.out.println("Failed to parse ports");
           return;
        }

        Charset charset = Charset.forName("US-ASCII");
        Path path = FileSystems.getDefault().getPath(".", "nodes.txt");

        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] splitted = line.split(":");
                if (splitted.length < 2)
                    continue;
                if (Integer.parseInt(splitted[1]) == p2pPort) { //исключает себя из списка
                    continue;
                }
                NodeEndpoint ep = new NodeEndpoint(splitted[0], splitted[1]);
                endpoints.add(ep);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
            return;
        }

        node.init(clientPort, p2pPort, endpoints);
        node.run();
    }
}
