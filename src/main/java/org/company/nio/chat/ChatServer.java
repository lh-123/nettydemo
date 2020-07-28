package org.company.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class ChatServer {
    private Selector selector;
    private ServerSocketChannel listenchannel;
    private final static int port=8888;
    public ChatServer(){
        try{
             selector = Selector.open();
             listenchannel=ServerSocketChannel.open();
             listenchannel.socket().bind(new InetSocketAddress(port));
             listenchannel.configureBlocking(false);
             listenchannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //监听
    public void listen(){
        try{
            while (true){
                int count = selector.select(2000);
                if (count>0){
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                        if(selectionKey.isAcceptable()){
                            SocketChannel socketChannel = listenchannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress()+"上线了");
                        }
                        if (selectionKey.isReadable()){
                            readData(selectionKey);
                        }
                        iterator.remove();
                    }
                }
            }
        } catch (Exception e) {

        }
    }
    //读取客户端数据
    private void readData(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel=null;
        try{
            socketChannel=(SocketChannel)selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int con=socketChannel.read(byteBuffer);
            if (con>0){
                String msg = new String(byteBuffer.array());
                System.out.println("来自客户端"+msg);
                sendInforOtherClient(msg,socketChannel);
            }

        }catch (IOException e){

            System.out.println(socketChannel.getRemoteAddress()+"下线了");
            //取消注册
            selectionKey.cancel();
            //关闭通道
            socketChannel.close();
        }
    }
    //转发消息
    private void sendInforOtherClient(String msg,SocketChannel self) throws IOException {
        System.out.println("转发信息....");
        for(SelectionKey selectionKey:selector.keys()){
            Channel channel = selectionKey.channel();
            if (channel instanceof SocketChannel && channel !=self){
                SocketChannel dest=(SocketChannel)channel;
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(byteBuffer);
            }

        }

    }
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.listen();

    }
}
