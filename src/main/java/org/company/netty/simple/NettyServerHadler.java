package org.company.netty.simple;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class NettyServerHadler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//异步执行
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("lihang1 你是废物",CharsetUtil.UTF_8));
                }catch (Exception e){
                    System.out.println("发生异常"+e.getMessage());
                }

            }
        });

            ctx.channel().eventLoop().schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10*1000);
                        ctx.writeAndFlush(Unpooled.copiedBuffer("lihang2 你是废物",CharsetUtil.UTF_8));
                    }catch (Exception e){
                        System.out.println("发生异常"+e.getMessage());
                    }

                }
            },1, TimeUnit.SECONDS);


        System.out.println("go on......");
//        System.out.println("server" +ctx);
//        ByteBuf buffer=(ByteBuf) msg;
//        System.out.println("客户端发送:"+buffer.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址"+ctx.channel().remoteAddress());

    }
//数据读取完毕之后
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       ctx.close();
    }
}
