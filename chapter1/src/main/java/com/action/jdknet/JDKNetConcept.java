package com.action.jdknet;

import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by hl on 2019/3/28.
 */
public class JDKNetConcept {

    @Test
    public void inetAddress() throws UnknownHostException {
        //获取本机的InetAddress实例
        InetAddress address =InetAddress.getLocalHost();
        System.out.println(address.getHostName());//获取计算机名
        System.out.println(address.getHostAddress());//获取IP地址
        byte[] bytes = address.getAddress();//获取字节数组形式的IP地址,以点分隔的四部分
        System.out.println(bytes[0]);

        //获取其他主机的InetAddress实例
        InetAddress address2 =InetAddress.getByName("其他主机名");
        InetAddress address3 =InetAddress.getByName("IP地址");
    }

    @Test
    public void url() throws MalformedURLException {
        //创建一个URL的实例
        URL baidu =new URL("http://www.baidu.com");
        URL url =new URL(baidu,"/index.html?username=tom#test");//？表示参数，#表示锚点
        System.out.println(url.getProtocol());//获取协议
        System.out.println(url.getHost());//获取主机
        System.out.println(url.getPort());//如果没有指定端口号，根据协议不同使用默认端口。此时getPort()方法的返回值为 -1
        System.out.println(url.getPath());//获取文件路径
        System.out.println(url.getFile());//文件名，包括文件路径+参数
        System.out.println(url.getRef());//相对路径，就是锚点，即#号后面的内容
        System.out.println(url.getQuery());//查询字符串，即参数
    }

    @Test
    public void readUrlHtmlPage() throws IOException {
        URL url = new URL("https://www.baidu.com");
        InputStream in = url.openStream();//通过openStream获取网页的字节输入流

        //将字节流转换为字符输入流，如果不指定编码，汉字可能出现乱码
        InputStreamReader inReader = new InputStreamReader(in, CharsetUtil.UTF_8);

        //为字符输入流提供缓存读取，提高读取效率
        BufferedReader bfReader = new BufferedReader(inReader);

        String data;

        while((data = bfReader.readLine()) != null) {
            System.out.println(data);
        }

        bfReader.close();
        inReader.close();
        in.close();
    }

}
