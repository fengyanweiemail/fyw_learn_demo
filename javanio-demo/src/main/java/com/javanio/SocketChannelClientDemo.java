package com.javanio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * Created by fyw on 2020/6/17.
 */
public class SocketChannelClientDemo {

    public static void main(String[] args) {
        client();
    }


    public static void client(){
        String info = "I'm-th information from client information from clientinformation from client" +
                "information from client information from client" +
                "information from client information from client information from clientinformation from client" +
                "information from client information from client information from client information from client" +
                "information from client information from client information from client information from client" +
                "information from client information from client information from client information from client" +
                "information from client information from client information from client" +
                "information from client information from client information from client information from client" +
                "information from client information from client information from client information from client" +
                "information from client information from client information from client information from client " +
                "information from client information from client information from client information from client" +
                "information from client information from client information from client information from client" +
                "1111111111";
        ByteBuffer buffer = ByteBuffer.allocate(info.length());
        SocketChannel socketChannel = null;
        try
        {

            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("localhost",8080));
            if(socketChannel.finishConnect())
            {
                int i=0;
                while(true) {
                    TimeUnit.SECONDS.sleep(1);
                    buffer.clear();
                    buffer.put(info.getBytes());
                    buffer.flip();
                    while(buffer.hasRemaining()){
                        System.out.println(buffer);
                        socketChannel.write(buffer);

                    }
                }




            }
        }







        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
        finally{
            try{
                if(socketChannel!=null){
                    socketChannel.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
