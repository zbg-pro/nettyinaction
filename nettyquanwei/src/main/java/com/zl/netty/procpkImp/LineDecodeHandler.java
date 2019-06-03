package com.zl.netty.procpkImp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.TooLongFrameException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hl on 2018/6/25.
 */
public class LineDecodeHandler extends ChannelHandlerAdapter {

    private int maxLength;

    ByteBuf cumulation;

    public LineDecodeHandler(int maxLength){
        this.maxLength = maxLength;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        List out = new ArrayList();
        try {
            if(msg instanceof ByteBuf) {
                cumulation = (ByteBuf) msg;
                callDecode(ctx, cumulation, out);
            } else {
                ctx.fireChannelRead(msg);
            }
        } catch (Exception e) {

        } finally {
            if (cumulation != null && !cumulation.isReadable()) {
                cumulation.release();
                cumulation = null;
            }
            int size = out.size();
            for (int i = 0; i < size; i ++) {
                ctx.fireChannelRead(out.get(i));
            }
            out = null;
        }
    }

    private void callDecode(ChannelHandlerContext ctx, ByteBuf in, List out) {
        while (in.isReadable()) {
            int outSize = out.size();
            int oldInputSize = in.readableBytes();
            decode(ctx, in, out);


            if (ctx.isRemoved()) {
                break;
            }

            if(outSize == out.size()) {
                if(oldInputSize == in.readableBytes()){
                    break;
                } else {
                    continue;
                }
            }

        }

    }

    private void decode(ChannelHandlerContext ctx, ByteBuf in, List out) {
        Object decoded = decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }

    private void fail(final ChannelHandlerContext ctx, int length) {
        ctx.fireExceptionCaught(
                new TooLongFrameException(
                        "frame length (" + length + ") exceeds the allowed maximum (" + maxLength + ')'));
    }

    private Object decode(ChannelHandlerContext ctx, ByteBuf buffer) {
        final int eol = findEndOfLine(buffer);

        if(eol >=0){
            final ByteBuf frame;
            final int length = eol - buffer.readerIndex();
            final int delimLength = buffer.getByte(eol) == '\r'? 2 : 1;

            if (length > maxLength) {
                buffer.readerIndex(eol + delimLength);
                fail(ctx, length);
                return null;
            }

            //跳过1个或者2个字符，因为除了每一行的内容输出外，还有个分隔符，这个分隔符如果是\r,跳2个index读取，如果是\n跳一个index继续读取
            frame = buffer.readBytes(length);
            buffer.skipBytes(delimLength);

            return frame;
        } else {
            //没找到换行符号，那么就以1024个字节为一组数据进行发送?
            final int length = buffer.readableBytes();

            return null;
        }

    }

    /**
     * Returns the index in the buffer of the end of line found.
     * Returns -1 if no end of line was found in the buffer.
     */
    private static int findEndOfLine(final ByteBuf buffer) {
        final int n = buffer.writerIndex();
        for (int i = buffer.readerIndex(); i < n; i ++) {
            final byte b = buffer.getByte(i);
            if (b == '\n') {
                return i;
            } else if (b == '\r' && i < n - 1 && buffer.getByte(i + 1) == '\n') {
                return i;  // \r\n
            }
        }
        return -1;  // Not found.
    }

}
