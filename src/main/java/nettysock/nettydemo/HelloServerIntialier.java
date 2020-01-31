package nettysock.nettydemo;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/*
初始化器，channel注册之后，执行里面相应的初始化方法
 */
public class HelloServerIntialier extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
     //通过SocketChannel去获取对应的管道
        //每一个channel是由handle
        ChannelPipeline pipeline = channel.pipeline();
        //通过管道添加　handler
        //HttpServerCodec()是由netty自己提供的助手类，可以理解微拦截器
        //当请求到服务端，我们需要做解码，响应到客户端做编码
        pipeline.addLast("HttpServerCodec",new HttpServerCodec());
        //添加自定义的助手类，返回“hello netty~"
        pipeline.addLast("customHandler",new CustomHandler());
    }
}
