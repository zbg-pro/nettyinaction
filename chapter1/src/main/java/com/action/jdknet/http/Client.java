package com.action.jdknet.http;

import java.io.*;
import java.net.Socket;

/**
 * Created by hl on 2019/3/28.
 */
public class Client {

    public static void connect(String msg, String host, int port) throws IOException {
        //1 创建客户端socket，指定服务器信息
        Socket socket = new Socket(host, port);

        //2 获取输出流，向服务端发送消息
        OutputStream out = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(out);
        pw.write(msg);pw.flush();
        socket.shutdownOutput();

        //3 获取输入流并读取服务端消息
        InputStreamReader inReader = new InputStreamReader(socket.getInputStream());
        BufferedReader br = new BufferedReader(inReader);
        String info = null;
        while((info = br.readLine()) != null) {
            System.out.println("[client]--, server:" + info);
        }

        //4 关闭资源
        br.close();pw.close();out.close();inReader.close();socket.close();
    }

    public static void main(String[] args) throws IOException {
        connect("hello server ~！", "localhost", 1213);
    }

}
