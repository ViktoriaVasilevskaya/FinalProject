package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpServer extends Thread {
    @Override
    public void run(){
        try (ServerSocket serverSocket = new ServerSocket(12345)){
            try (Socket socket = serverSocket.accept()){
                InputStream in = socket.getInputStream();
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                byte [] buf = new byte [1024];
                int len;
                while ((len = in.read(buf))>0)
                    bout.write(buf,0,len);
                System.out.println(new String(bout.toByteArray(), StandardCharsets.UTF_8));

            }

        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
