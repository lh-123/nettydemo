package org.company.bio;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import jdk.nashorn.internal.ir.CallNode;
import org.jcp.xml.dsig.internal.dom.DOMUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOservice {
    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("服务器启动。。。。");
        while (true){
            Socket socket = serverSocket.accept();
            System.out.println("连接到客户端");
            System.out.println("线程id" +Thread.currentThread().getId()+"名字"+Thread.currentThread().getName());
            //启动线程
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    handler(socket);

                }
            });

        }

    }
    public static void handler(Socket socket)  {
        try {
            System.out.println("线程id" +Thread.currentThread().getId()+"名字"+Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            while (true){
                int read = inputStream.read(bytes);
                if (read!=-1){
                    System.out.println(new String(bytes,0,read));
                }else{}
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭连接");
            try{
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
