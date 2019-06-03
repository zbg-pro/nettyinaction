package com.zl.netty.procpkImp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;



import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Created by hl on 2018/6/25.
 */
public class ByteBufTest {



    public void test1(){
        String s = "abcdefg1234567";
        byte[] b = s.getBytes();
        ByteBuf srcBuf = Unpooled.wrappedBuffer(b);

        //创建完成后，写的指针已经指向的index结尾
        //读的指针在index开头
        byte[] b2 = new byte[20];
        ByteBuf targetBuf = Unpooled.wrappedBuffer(b2);

        srcBuf.writerIndex();
        srcBuf.readableBytes();
        srcBuf.readerIndex();

        System.out.println(srcBuf.writerIndex());
        System.out.println(srcBuf.readerIndex());
        System.out.println(srcBuf.readableBytes());
        System.out.println(srcBuf.capacity());

        System.out.println("--------");

        /**
         * 带参数可以指定当前读写指针的位置，不带参数返回当前读写指针位置
         */
        System.out.println(targetBuf.writerIndex());
        System.out.println(targetBuf.readerIndex());
//        System.out.println(targetBuf.writerIndex(2));
//        System.out.println(targetBuf.readerIndex(2));
        System.out.println(targetBuf.writerIndex());

        //targetBuf.writeByte(1);
        System.out.println(targetBuf.writerIndex());


        System.out.println(targetBuf.isReadable());
        System.out.println(targetBuf.isWritable());
        System.out.println("----------");
        for (int i=0; i<srcBuf.capacity(); i++) {
            byte bb = srcBuf.getByte(i);
            System.out.println((char)bb);
        }
    }


    /**
     * readBytes方法作用：读取指定长度内容，返回byteBuf对象，同时源bytebuf读取指针相应前移
     */

    public void test2(){
        //创建完成后，写的指针已经指向的index结尾
        //读的指针在index开头
        byte[] b2 = new byte[20];
        ByteBuf targetBuf = Unpooled.wrappedBuffer(b2);
        System.out.println(targetBuf.writerIndex());
        System.out.println(targetBuf.readerIndex());

        System.out.println("-----1------");
        ByteBuf b3 = targetBuf.readBytes(5);

        System.out.println(b3.writerIndex());
        System.out.println(b3.readerIndex());

        System.out.println(targetBuf.writerIndex());
        System.out.println(targetBuf.readerIndex());

    }


    public void testReadBytes(){
        String s = "123456789";
        ByteBuf srcByteBuf = Unpooled.wrappedBuffer(s.getBytes());

        ByteBuf byteBuf = srcByteBuf.readBytes(5);
        for (int i = 0; i < byteBuf.capacity(); i++) {
            System.out.print((char)byteBuf.getByte(i));
        }


    }


    public void testWriteBytes(){
//        String s = "123456789123456789123456789";
//        ByteBuf srcByteBuf = Unpooled.wrappedBuffer(s.getBytes());

        byte[] bytes = new byte[27];
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);

        System.out.println(byteBuf.writerIndex());
        byteBuf.writerIndex(0);
        System.out.println(byteBuf.writerIndex());

        //此行代码：src的字节被写入byteBuf
        byteBuf.writeBytes(srcByteBuf);

        for (int i = 0; i < byteBuf.capacity(); i++) {
            System.out.print((char)byteBuf.getByte(i));
        }

    }

    String s = "123456789123456789123456789";
    ByteBuf srcByteBuf = Unpooled.wrappedBuffer(s.getBytes());


    public void testReadBytes2(){
        byte[] bytes = new byte[22];
        srcByteBuf.readBytes(bytes);

        for (int i = 0; i < bytes.length; i++) {
            System.out.println((char)bytes[i]);
        }
    }


    public void testReadBytes3(){
        byte[] bytes = new byte[22];
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        byteBuf.writerIndex(0);

        srcByteBuf.readBytes(byteBuf);

        for (int i = 0; i < bytes.length; i++) {
            System.out.print((char)bytes[i]);
        }
    }


    public void testWriteBytes4(){
        byte[] bytes = new byte[27];
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        byteBuf.writerIndex(0);

        byteBuf.writeBytes(srcByteBuf.array());

        for (int i = 0; i < byteBuf.capacity(); i++) {
            System.out.print((char)byteBuf.getByte(i));
        }
    }

    public void testWriteBytes5(){
        byte[] bytes = new byte[27];
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        byteBuf.writerIndex(0);

        //bytebuf的readbytes和writesbytes的效率，参考Unpooled零拷贝(Zero Copy) 的理解
        //https://www.cnblogs.com/xys1228/p/6088805.html

        for (int i = 0; i < byteBuf.capacity(); i++) {
            System.out.print((char)byteBuf.getByte(i));
        }
    }


    public void zeroCopy1(){
        //1 通过compositByteBuf实现，逻辑上的buffer，真实的buffer还是在内部对象中
        ByteBuf header = Unpooled.wrappedBuffer("123456".getBytes());
        ByteBuf body = Unpooled.wrappedBuffer("abcdefghijk".getBytes());

        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        compositeByteBuf.addComponents(header, body);

        //2 通过wrappedBuffer，对于header,body的修改会直接反映到b2上
        ByteBuf b2 = Unpooled.wrappedBuffer(header, body);


        //3 通过slice实现零拷贝   不带参数的相当于全部读取
        ByteBuf b3 = b2.slice(1,4);

        //4 通过fileRegion实现zero-copy
        //基于java nio的fileChannel实现，见copyFileWithFileChannel()方法
        //实际上是拷贝内存中起始地址和结束地址的位置

    }


    /**
     * 可以直接通过它将文件的内容直接写入 Channel 中,
     * 而不需要像传统的做法: 拷贝文件内容到临时 buffer,
     * 然后再将 buffer 写入 Channel. 通过这样的零拷贝操作, 无疑对传输大文件很有帮助.
     *
     * @param srcFileName
     * @param targetFileName
     * @throws IOException
     */
    public void copyFileWithFileChannel(String srcFileName, String targetFileName) throws IOException {
        RandomAccessFile srcFile = new RandomAccessFile(srcFileName, "r");
        RandomAccessFile targetFile = new RandomAccessFile(targetFileName, "r");

        FileChannel srcFileChannel = srcFile.getChannel();
        FileChannel targetFileChannel = targetFile.getChannel();

        long position = 0;
        long count = srcFileChannel.size();

        srcFileChannel.transferTo(position, count, targetFileChannel);

    }

}
