package com.wlt.tomcat.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author 王立腾
 * @date 2019-09-15 18:03
 */
public class FileUtils {

    /**
     * 获取文件的byte[]数组
     *
     * @param dataOrigin
     * @return
     */
    public static byte[] getContent(String dataOrigin) {

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(dataOrigin, "rw");
            FileChannel channel = randomAccessFile.getChannel();
            //如果数据超过int最大值，就需要用流来传递了
            ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
            channel.read(buffer);
            buffer.flip();
            return buffer.array();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;


        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;

    }
}
