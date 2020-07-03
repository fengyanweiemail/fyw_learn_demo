package com.javanio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by fyw on 2020/6/5.
 */
public class FileChannelDemo {
    public static void main(String[] args) {
        RandomAccessFile randomAccessFile = null;
        try{
            long start = System.currentTimeMillis();
            randomAccessFile = new RandomAccessFile("javanio-demo/src/aa.txt","rw");
            //获取一个文件通道  FileChannel不能切换到非阻塞模式
            FileChannel fileChannel = randomAccessFile.getChannel();
            //设置一个缓冲区的大小
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //将文件信息读进缓冲区，返回读取的长度
            int b = fileChannel.read(buffer);
            System.out.println(b);
            while(b!=-1){
                //设置缓冲区从头开始读取
                buffer.flip();
                //判断是否已经达到缓冲区的上界
                while (buffer.hasRemaining()){
                    //获取文本信息并打印
                    System.out.print(buffer.get());
                }
                //不覆盖缓冲区未读数据的情况下，从头开始
                buffer.compact();
                b = fileChannel.read(buffer);
            }
            long end = System.currentTimeMillis();
            System.out.println("耗时"+(end-start));
        }catch (Exception e){

        }finally {
            try {
                randomAccessFile.close();
            }catch (Exception e){

            }

        }
    }
}
