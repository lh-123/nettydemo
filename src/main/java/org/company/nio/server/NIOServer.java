package org.company.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置非堵塞
        serverSocketChannel.configureBlocking(false);
        //注册
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            //等待一秒钟
            if (selector.select(1000)==0){
                System.out.println("无连接，老子不等了");
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()){
                SelectionKey selectionKey = keyIterator.next();
                if (selectionKey.isAcceptable()){
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功");
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }
                if (selectionKey.isReadable()){
                    //通过selectionkey反向获取对应的channel
                    SocketChannel channel = (SocketChannel)selectionKey.channel();
                    //获取到该channel关联的Buffer
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    channel.read(byteBuffer);
                    System.out.println("客户端发送"+new String(byteBuffer.array()));
                }
                keyIterator.remove();
            }
        }
    }
}
