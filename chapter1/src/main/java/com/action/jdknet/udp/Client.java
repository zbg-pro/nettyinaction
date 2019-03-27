package com.action.jdknet.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by hl on 2019/3/28.
 */
public class Client {

    public static void connect(String host, int port) throws Exception{
        //客户端
        //1、定义服务器的地址、端口号、数据
        InetAddress address =InetAddress.getByName(host);
        byte[] data ="用户名：admin;密码：123".getBytes();
        //2、创建数据报，包含发送的数据信息
        DatagramPacket packet = new DatagramPacket(data,data.length,address,port);
        //3、创建DatagramSocket对象
        DatagramSocket socket =new DatagramSocket();
        //4、向服务器发送数据
        socket.send(packet);


        //接受服务器端响应数据
        //======================================
        //1、创建数据报，用于接受服务器端响应数据
        byte[] data2 = new byte[1024];
        DatagramPacket packet2 = new DatagramPacket(data2,data2.length);
        //2、接受服务器响应的数据
        socket.receive(packet2);
        String reply = new String(data2, 0,packet2.getLength());
        System.out.println("我是客户端，服务器说："+reply);
        //4、关闭资源
        socket.close();
    }

    public static void main(String[] args) throws Exception {
        connect("localhost", 1212);
    }

}
