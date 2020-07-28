package org.company.nio.chat;

import io.netty.handler.codec.spdy.SpdyWindowUpdateFrame;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ChatClient {
    private final String host="127.0.0.1";
    private final int port=8888;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;
    public ChatClient() throws IOException {
        selector=Selector.open();
        socketChannel=SocketChannel.open(new InetSocketAddress(host,port));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ);
        username = socketChannel.getRemoteAddress().toString().substring(1);


    }
}
