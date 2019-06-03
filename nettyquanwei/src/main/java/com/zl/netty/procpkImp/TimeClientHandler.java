package com.zl.netty.procpkImp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * Created by hl on 2018/5/7.
 *
 * 连接上服务端后，立即循环100次发送同样的消息，每条消息以换行符号结束
 *
 * 读取服务端发送来的消息：打印预期的时间消息，打印读取次数
 *
 * 服务端最开始每次收到1024个字符，到第16次开始接收16383  15Kb的数据，直到接收完成
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

    private static final Logger loggger = Logger.getLogger(TimeClientHandler.class.getName());

    private int counter;

    private byte[] req;

    public TimeClientHandler() {
        req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message = null;
        for (int i = 0; i < 3; i++) {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.write(message);
        }
        ctx.flush();
    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        byte[] req = new byte[buf.readableBytes()];

        buf.readBytes(req);

        String body = new String(req, "UTF-8");

        System.out.println("NOW is : "+body + "; the counter is: "+ ++counter);


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        loggger.warning("unexcept exception from downStream: " + cause.getMessage());
        ctx.close();
    }
}
