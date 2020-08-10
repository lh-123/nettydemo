package org.company.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
            NioEventLoopGroup bossGroup = new NioEventLoopGroup();
            NioEventLoopGroup workGroup = new NioEventLoopGroup();
            try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //链式编程
            serverBootstrap.group(bossGroup, workGroup)//设置俩个线程组
                    .channel(NioServerSocketChannel.class)//使用NioServerSocketChannel　
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//是指保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道测试对象
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("客户"+ch.hashCode());
                            ch.pipeline().addLast(new NettyServerHadler());

                        }
                    });
            System.out.println("服务器准备好了");
            ChannelFuture future = serverBootstrap.bind(6666).sync();
            //对关闭通道进行监听
                future.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()){
                            System.out.println("监听端口　成功");
                        }else {
                            System.out.println("监听端口失败");
                        }
                    }
                });
            future.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
