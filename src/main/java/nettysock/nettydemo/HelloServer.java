package nettysock.nettydemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HelloServer {
    public static void main(String[] args) throws InterruptedException {
        //定义一对线程组，
        //定义一个主线程组，用于接受客户端的链接，但不做任何处理
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        //定义一个从线程组，主线程组会把任务丢给他，让从线程去做任务
        EventLoopGroup workGroup=new NioEventLoopGroup();

        try{
            //netty服务器的创建，ServerBootstrap是一个启动类
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            //设置主从线程组
            serverBootstrap.group(bossGroup,workGroup);
            //设置nio的双向通道
            serverBootstrap.channel(NioServerSocketChannel.class);
            //子处理器,用于处理workGroup
            serverBootstrap.childHandler(new HelloServerIntialier());
            //启动server，并且设置8080为启动的端口号，同时启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();

            //监听关闭channel.设置为同步
            channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
