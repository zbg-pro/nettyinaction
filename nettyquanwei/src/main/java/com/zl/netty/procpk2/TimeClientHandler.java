package com.zl.netty.procpk2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * Created by hl on 2018/5/7.
 * 未考虑tcp粘包的情况下处理接收消息
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
            ctx.writeAndFlush(message);
        }

    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;

        System.out.println("NOW is : "+body + "; the counter is: "+ ++counter);


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        loggger.warning("unexcept exception from downStream: " + cause.getMessage());
        ctx.close();
    }
}
