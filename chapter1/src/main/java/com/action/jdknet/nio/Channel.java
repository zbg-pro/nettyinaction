package com.action.jdknet.nio;

import org.junit.Test;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by hl on 2019/3/28.
 */
public class Channel {


    @Test
    public void fileChannelTest() throws Exception{
        RandomAccessFile aFile = new RandomAccessFile("/Users/hl/Documents/config-prod.txt", "rw");
        FileChannel channel = aFile.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(4888);
        int bytesRead = channel.read(byteBuffer);
        while (bytesRead != -1) {
            System.out.println("Read " + bytesRead);
            byteBuffer.flip();//从写模式换未读取模式
            while (byteBuffer.hasRemaining()) {
                System.out.print((char)byteBuffer.get());
            }

            byteBuffer.clear();
            bytesRead = channel.read(byteBuffer);
        }

        aFile.close();
    }

    //buffer的事例通过http://ifeve.com/buffers/

    public static void byteBufferTest(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(121);
        byteBuffer.mark();
        byteBuffer.reset();
        /**
         * equals()与compareTo()方法
         可以使用equals()和compareTo()方法两个Buffer。

         equals()
         当满足下列条件时，表示两个Buffer相等：

         有相同的类型（byte、char、int等）。
         Buffer中剩余的byte、char等的个数相等。
         Buffer中所有剩余的byte、char等都相同。
         如你所见，equals只是比较Buffer的一部分，不是每一个在它里面的元素都比较。实际上，它只比较Buffer中的剩余元素。

         compareTo()方法
         compareTo()方法比较两个Buffer的剩余元素(byte、char等)， 如果满足下列条件，则认为一个Buffer“小于”另一个Buffer：

         第一个不相等的元素小于另一个Buffer中对应的元素 。
         所有元素都相等，但第一个Buffer比另一个先耗尽(第一个Buffer的元素个数比另一个少)。
         */
    }
}
