package nettysock.nettydemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;


/*
创建自定义助手类
 */
//SimpleChannelInboundHandler：对于请求来讲，其实相当于入站，入境
public class CustomHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject)
            throws Exception {
        //获取channel
        Channel channel=channelHandlerContext.channel();
        System.out.println(channel.remoteAddress());
        //定义发送数据信息
        ByteBuf content= Unpooled.copiedBuffer("Hello netty~", CharsetUtil.UTF_8);
        //构建一个http response
        FullHttpResponse response=new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                                                              HttpResponseStatus.OK,
                                                              content);
        //为响应增加数据类型和长度
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,content.readableBytes());
        //把响应刷到客户端
        channelHandlerContext.writeAndFlush(response);


    }
}
