package com.action.jdknet.http;

import io.netty.util.CharsetUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hl on 2019/3/28.
 */
public class Server {

    public static void start(int port) throws IOException {
        //1 创建服务端，绑定端口，并监听端口
        ServerSocket serverSocket = new ServerSocket(port);

        //2 调用accept方法监听，等待客服端连接
        Socket socket = serverSocket.accept();

        //3 获取输入流，读取客户端信息
        InputStream in = socket.getInputStream();//通过openStream获取网页的字节输入流

        //将字节流转换为字符输入流，如果不指定编码，汉字可能出现乱码
        InputStreamReader inReader = new InputStreamReader(in, CharsetUtil.UTF_8);

        //为字符输入流提供缓存读取，提高读取效率
        BufferedReader bfReader = new BufferedReader(inReader);

        String data;

        while((data = bfReader.readLine()) != null) {
            System.out.println("[server]--- clientInfo:" + data);
        }

        socket.shutdownInput();//关闭输入流

        //4 获取输出流。响应给到客户端
        OutputStream out = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(out);
        pw.write("[server]---welcome！");

        //5 关闭资源
        pw.close();
        out.close();
        bfReader.close();
        inReader.close();
        in.close();
        socket.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws Exception {
        //start(1213);
        serverStart(1213);
    }

    /**
     * 支持多客户端连接
     * @param port
     * @throws Exception
     */
    public static void serverStart(int port) throws Exception{
        ServerSocket serverSocket =new ServerSocket(port);
        ExecutorService threadService = Executors.newCachedThreadPool();
        Socket socket = null;
        int count = 0;
        while (true) {
            socket = serverSocket.accept();
            threadService.submit(new ServerBusinessProcess(socket));
            count++;
            System.out.println("client count：" + count);
        }
    }

    public  static class ServerBusinessProcess implements Runnable {
        Socket socket = null;
        public ServerBusinessProcess (Socket socket){
            this.socket = socket;
        }

        public void run() {
            //服务端处理的业务代码
            try {
                //3 获取输入流，读取客户端信息
                InputStream in = socket.getInputStream();//通过openStream获取网页的字节输入流

                //将字节流转换为字符输入流，如果不指定编码，汉字可能出现乱码
                InputStreamReader inReader = new InputStreamReader(in, CharsetUtil.UTF_8);

                //为字符输入流提供缓存读取，提高读取效率
                BufferedReader bfReader = new BufferedReader(inReader);

                String data;

                while((data = bfReader.readLine()) != null) {
                    System.out.println("[server]--- clientInfo:" + data);
                }

                socket.shutdownInput();//关闭输入流

                //4 获取输出流。响应给到客户端
                OutputStream out = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(out);
                pw.write("[server]---welcome！");

                //5 关闭资源
                pw.close();
                out.close();
                bfReader.close();
                inReader.close();
                in.close();
                socket.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
