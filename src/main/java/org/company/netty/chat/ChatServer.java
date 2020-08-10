package org.company.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ChatServer {
    private int port;
    public ChatServer(int port){
        this.port=port;
    }
    public void run() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {


        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        //解码器
                        pipeline.addLast("decoder",new StringDecoder());
                        //编码器
                        pipeline.addLast("encode",new StringEncoder());
                        pipeline.addLast(new ChatServerHandler());

                    }
                });
        System.out.println("服务器已经就绪");
        ChannelFuture future = serverBootstrap.bind(port).sync();
        future.channel().closeFuture().sync();
    }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws InterruptedException {

        new ChatServer(8888).run();

    }
}
