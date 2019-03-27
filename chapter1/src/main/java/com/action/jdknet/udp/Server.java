package com.action.jdknet.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by hl on 2019/3/28.
 */
public class Server {

    public static void start(int port) throws IOException {
        //1、创建服务器端DatagramSocket，指定端口
        DatagramSocket socket =new DatagramSocket(port);

        //2 创建数据包，用来接收客户端发送的数据
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);

        //3接收客户端发送的数据
        socket.receive(packet);

        //4 读取数据
        String reqMsg = new String(data);
        System.out.println("[server]-- clientInfo:" + reqMsg);

        //5 向客户端发送数据
        //(1)定义客服端的地址，端口号，数据
        InetAddress address = packet.getAddress();
        int clientPort = packet.getPort();
        byte[] data2 = "欢迎您~！".getBytes();

        //(2)创建数据包，包含地址，端口，数据
        DatagramPacket packet2 = new DatagramPacket(data2, data2.length, address, clientPort);

        //响应资源
        socket.send(packet2);

        //（3）关闭资源
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        start(1212);
    }


}
