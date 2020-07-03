package com.javanio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by fyw on 2020/6/4.
 */
public class BufferDemo {
    /*public static void main(String[] args) {
        //创建一个多大长度的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);



    }*/

    public static void main(String[] args) {

        InputStream in = null;
        try{
            long start = System.currentTimeMillis();
            in = new BufferedInputStream(new FileInputStream("javanio-demo/src/aa.txt"));
            byte [] buf = new byte[1024];
            int bytesRead = in.read(buf);
            while(bytesRead != -1)
            {
                for(int i=0;i<bytesRead;i++)
                    System.out.print((char)buf[i]);
                bytesRead = in.read(buf);
            }

            long end = System.currentTimeMillis();

            System.out.println("耗时"+(end-start));
        }catch (IOException e)
        {
            e.printStackTrace();
        }finally{
            try{
                if(in != null){
                    in.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}
