package com.zl.netty.protocol.codeProcessor;



import com.zl.netty.protocol.entity.Header;
import com.zl.netty.protocol.entity.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hl on 2018/6/4.
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    MarshallingDecoder marshallingDecoder;

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws Exception {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        this.marshallingDecoder = new MarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if(frame == null)
            return null;

        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionID(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());

        int size = frame.readInt();
        if (size > 0) {
            Map<String, Object> attch = new HashMap<String, Object>(size);
            int keySize = 0;
            byte[] keyArray = null;
            String key = null;
            for (int i = 0; i < size; i++) {
                keySize = frame.readInt();
                keyArray = new byte[keySize];
                frame.readBytes(keyArray);
                key = new String(keyArray, "UTF-8");
                attch.put(key, marshallingDecoder.decode(frame));
            }
            keyArray = null;
            key = null;
            header.setAttachment(attch);

        }
        if (frame.readableBytes() > 4) {
            message.setBody(marshallingDecoder.decode(frame));
        }
        message.setHeader(header);
        return message;
    }


    public static void main(String[] args) throws UnsupportedEncodingException {
        ByteBuf buffer = Unpooled.compositeBuffer();
        String str1 = "\rzhang涨等多个风格zztrrterwqef";
        int a = 12;
        long l = 13l;
        boolean b = true;
        buffer.writeBoolean(b);
        buffer.writeInt(a);
        buffer.writeLong(l);
        buffer.writeBytes(str1.getBytes("UTF-8"));


        byte[] bs = buffer.array();
        System.out.println(new String(bs, "UTF-8"));//zhang涨等多个风格zz                          

        int readerIndex = buffer.readerIndex();
        System.out.println(readerIndex);//0

        int readable = buffer.readableBytes();
        System.out.println(readable);

        System.out.println(buffer.isReadable());


        System.out.println(buffer.capacity());

        System.out.println(buffer.hasMemoryAddress());

        System.out.println(buffer.readerIndex());
        System.out.println(buffer.writerIndex());

//        a = 0;
//        while(buffer.isReadable()) {a++;
//            System.out.print(new String(new byte[]{buffer.readByte()}, "UTF-8"));
//        }
//        System.out.println("\n@@@"+a);

//        Random random = new Random();
//        while (buffer.writableBytes() > 4) {
//            int ii = random.nextInt(100);
//            buffer.writeInt(ii);
//        }

        int index = buffer.forEachByte(ByteBufProcessor.FIND_CR);
        System.out.println("aa"+index);

        /**
         * slice()
         * duplicate()
         * slice(int, int)
         * readSlice(int)
         * Unpooled.unmodifiableBuffer()
         *
         */

        Charset utf8 = Charset.forName("utf-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in action rocks!", utf8);

        ByteBuf sliced = buf.slice(0, 14);
        System.out.println(sliced.toString(utf8));
        buf.setByte(0 ,(byte)'J');
        System.out.println(buf.getByte(0) == sliced.getByte(0));

        ByteBuf copyed = buf.copy(0, 14);
        System.out.println(copyed.toString(utf8));
        buf.setByte(0 ,(byte)'J');
        System.out.println(buf.getByte(0) == copyed.getByte(0));


    }
}
