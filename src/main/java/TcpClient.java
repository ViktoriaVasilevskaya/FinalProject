import server.TcpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpClient extends Thread {

    @Override
    public void run(){
        try(Socket socket = new Socket()){
            socket.connect(new InetSocketAddress("localhost",12345));

            OutputStream out = socket.getOutputStream();

            out.write("read me".getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public static void main (String [] args) throws Exception{
        TcpServer server = new TcpServer();
        TcpClient client = new TcpClient();

        server.start();
        client.start();
    }

}