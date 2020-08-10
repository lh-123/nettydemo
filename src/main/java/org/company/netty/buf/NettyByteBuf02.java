package org.company.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class NettyByteBuf02 {
    public static void main(String[] args) {
        ByteBuf buf = Unpooled.copiedBuffer("hello,天津", StandardCharsets.UTF_8);
        if (buf.hasArray()){
            byte[] array = buf.array();
            System.out.println(new String(array, StandardCharsets.UTF_8));
            System.out.println("byteBuf"+buf);
            System.out.println(buf.arrayOffset());
            System.out.println(buf.readerIndex());
            System.out.println(buf.writerIndex());
            System.out.println(buf.capacity());
            int i = buf.readableBytes();
            System.out.println("可读取"+i);

        }
    }
}
