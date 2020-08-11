package org.company.netty.chat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.company.netty.chat.ChatClientHandler;

import java.util.Scanner;


public class ChatClient1 {
    private final String host;
    private final int port;

    public ChatClient1(String host, int port) {
        this.host = host;
        this.port = port;
    }
    public void run() throws InterruptedException {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            //编码器
                            pipeline.addLast("encode", new StringEncoder());
                            pipeline.addLast(new ChatClientHandler());

                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            Channel channel = future.channel();
            System.out.println("---------------"+channel.remoteAddress()+"------------------");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String msg = scanner.nextLine();
                channel.writeAndFlush(msg);
            }
        }finally {
            eventExecutors.shutdownGracefully();
        }

    }    public static void main(String[] args) throws InterruptedException {
        new ChatClient1("127.0.0.1",8888).run();

    }
}
