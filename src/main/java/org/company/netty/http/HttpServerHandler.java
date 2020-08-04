package org.company.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import javax.sound.midi.MidiFileFormat;
import java.net.URI;

//指HttpObject客户端客户端相互通讯的数据被封装为HttpObject
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
           if (msg instanceof HttpRequest){
               System.out.println("msg 类型" +msg.getClass());
               System.out.println("客户端地址"+ctx.channel().remoteAddress());
               HttpRequest httpRequest=(HttpRequest)msg;
               URI uri = new URI(httpRequest.uri());
               if ("/favicon.ico".equals(uri.getPath())){
                   System.out.println("不搭理你");
                   return;
               }
               //回复信息http协议
               ByteBuf content = Unpooled.copiedBuffer("hello ,Echo", CharsetUtil.UTF_8);
               //构造http的响应，httpresponse
               FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,content);
               httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
               httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
               ctx.writeAndFlush(httpResponse);


           }

    }
}
