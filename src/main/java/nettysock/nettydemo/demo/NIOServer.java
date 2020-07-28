package nettysock.nettydemo.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(8888));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            if (selector.select(1000)==0){
                System.out.println("无客户端连接");
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()){
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    System.out.println("有个客户接入");
                }
                if(selectionKey.isReadable()){
                    SocketChannel selectableChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer attachment =(ByteBuffer) selectionKey.attachment();
                    selectableChannel.read(attachment);
                    System.out.println("收到"+new String(attachment.array()));
                }
                iterator.remove();
            }
        }
        }
    }

