package org.company.nio.channel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//创建字符串
public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String string="李航，加油啊";
        FileOutputStream fileOutputStream = new FileOutputStream("/home/lih/Desktop/001.text");
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();
        ByteBuffer bytebuffer = ByteBuffer.allocate(1024);
        bytebuffer.put(string.getBytes());
        bytebuffer.flip();
        fileOutputStreamChannel.write(bytebuffer);
        fileOutputStream.close();


    }
}
