package org.company.netty.chat;

import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
    //定义channel主，管理所有channel.GlobalEventExecutor.INSTANCE全局时间执行器
    private  static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf=new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    //handlerAdded表示连接建立，一旦连接，第一个执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户端连接服务器信息推送
        channelGroup.writeAndFlush("[伞兵]"+channel.remoteAddress()+"已就位"+sdf.format(new java.util.Date())+"\n");
        channelGroup.add(channel);


    }
    //将该客户端断开服务器信息推送
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[伞兵]"+channel.remoteAddress()+"坚守不住阵地，壮烈牺牲!"+sdf.format(new java.util.Date())+"\n");
        System.out.println("还剩余"+channelGroup.size()+"伞兵坚守阵地");
    }

    //channelActive表示channel处于活动状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"就位!!!"+sdf.format(new java.util.Date())+"\n");
    }
   //channelInactive表示channel处于非活动状态
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"坚守不住阵地，壮烈牺牲!!!"+sdf.format(new java.util.Date())+"\n");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel=ctx.channel();
        channelGroup.forEach(ch -> {
            if (channel !=ch){
                //不是当前channel,转发信息
                ch.writeAndFlush("[伞兵]"+channel.remoteAddress()+"发送消息"+msg+sdf.format(new java.util.Date())+"\n");
            }  else {
                ch.writeAndFlush("[自己]发送了消息"+msg+sdf.format(new java.util.Date())+"\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
