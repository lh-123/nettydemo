package org.company.nio.chat;

import io.netty.handler.codec.spdy.SpdyWindowUpdateFrame;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class ChatClient {
    private final String host="127.0.0.1";
    private final int port=8888;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;
    public ChatClient() throws IOException {
        selector=Selector.open();
        socketChannel= SocketChannel.open(new InetSocketAddress(host,port));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username+"已就位");
    }
    public void sendInfo(String info){
    info =username +"说"+info;
    try{
        socketChannel.write(ByteBuffer.wrap(info.getBytes()));
    } catch (IOException e) {
        e.printStackTrace();
    }

    }
 public void reafInfo(){
        try{
            int read=selector.select();
            if(read>0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if (key.isReadable()){
                        SocketChannel sc =(SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        sc.read(byteBuffer);
                        String s = new String(byteBuffer.array());
                        System.out.println(s.trim());
                    }
                }
                iterator.remove();
            }else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

 }

    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient();
        new Thread(() -> {
            chatClient.reafInfo();
            try{
                Thread.currentThread().sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String s=scanner.nextLine();
            chatClient.sendInfo(s);
        }

    }
}
