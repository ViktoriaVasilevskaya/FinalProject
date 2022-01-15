package server;

import java.io.IOException;

public class CacheNode {


    public static void main (String [] args){

        try{
          HTTPServer server = new HTTPServer();
          server.start();
      } catch (IOException e){
          System.out.println(e.getMessage());
      }

    }


}
