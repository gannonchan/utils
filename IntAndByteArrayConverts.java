package org.gannon;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.util.stream.IntStream;

/**
 * 本类是用于整型和byte[]之间的相互转换
 *
 * @author chenzhiming
 * @date 2023/07/13
 */
public class IntAndByteArrayConverts {

    /**
     * 主机为小端字节序的int类型转byte[]
     *
     * @param value 需要转换的数
     * @return 转换后的byte[]
     */
    public static byte[] intToByteArrayLE(int value) {
        final byte[] bytes = new byte[4];
        IntStream.range(0, 4).forEach(i -> {
            int offset = Integer.SIZE - (i + 1) * Byte.SIZE;
            bytes[i] = (byte) ((value >> offset) & 0xff);
        });
        return bytes;
    }

    /**
     * 主机为大端字节序的int类型转byte[]
     *
     * @param value 需要转换的数
     * @return 转换后的byte[]
     */
    public static byte[] intToByteArrayBE(int value) {
        final byte[] bytes = new byte[4];
        IntStream.range(0, 4).forEach(i -> {
            int offset = Integer.SIZE - (i + 1) * Byte.SIZE;
            int index = offset / Byte.SIZE;
            bytes[index] = (byte) ((value >> offset) & 0xff);
        });
        return bytes;
    }

    /**
     * 主机为小端字节序的byte[]类型转int，高地址位存储高数位，低地址位存储低数位
     *
     * @param bytes byte[]
     * @return 转换后的整型数
     */
    public static int byteArrayToIntLE(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int offset = Integer.SIZE - (i + 1) * Byte.SIZE;
            value |= (bytes[i] & 0xff) << offset;
        }
        return value;
    }

    /**
     * 主机为大端字节序的byte[]类型转int，低地址位存储高数位，高地址存储低数位
     *
     * @param bytes byte[]
     * @return 转换后的整型数
     */
    public static int byteArrayToIntBE(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int offset = Integer.SIZE - (i + 1) * Byte.SIZE;
            int index = offset / Byte.SIZE;
            value |= (bytes[index] & 0xff) << offset;
        }
        return value;
    }

    /**
     * 基于平台的自适应字节序的int类型转byte[]
     *
     * @param value 需要转换的整型数
     * @return 转换后的byte[]
     */
    public static byte[] intToByteArrayWithPlatform(int value) {
        final byte[] bytes = new byte[4];
        ByteOrder byteOrder = ByteOrder.nativeOrder();
        if ("LITTLE_ENDIAN".equals(byteOrder.toString())) {
            // 高位在前低位在后
            IntStream.range(0, 4).forEach(i -> {
                int offset = Integer.SIZE - (i + 1) * Byte.SIZE;
                bytes[i] = (byte) ((value >> offset) & 0xff);
            });
        } else {
            // 大端字节序
            // 低位在前高位在后
            IntStream.range(0, 4).forEach(i -> {
                int offset = Integer.SIZE - (i + 1) * Byte.SIZE;
                int index = offset / Byte.SIZE;
                bytes[index] = (byte) ((value >> offset) & 0xff);
            });
        }
        return bytes;
    }

    /**
     * 基于平台的自适应字节序的byte[]类型转int
     *
     * @param bytes 需要转换的byte[]
     * @return 转换后的整型数
     */
    public static int byteArrayToIntWithPlatform(byte[] bytes) {
        int value = 0;
        ByteOrder byteOrder = ByteOrder.nativeOrder();
        if ("LITTLE_ENDIAN".equals(byteOrder.toString())) {
            // 小端字节序
            // 高位在前低位在后
            for (int i = 0; i < 4; i++) {
                int offset = Integer.SIZE - (i + 1) * Byte.SIZE;
                value |= (bytes[i] & 0xff) << offset;
            }
        } else {
            // 大端字节序
            // 低位在前高位在后
            for (int i = 0; i < 4; i++) {
                int offset = Integer.SIZE - (i + 1) * Byte.SIZE;
                int index = offset / Byte.SIZE;
                value |= (bytes[index] & 0xff) << offset;
            }
        }
        return value;
    }

    // 获取主机字节序，也可以使用java内置的ByteOrder.netiveOrder()直接获取
    public static void byteOrder() throws NoSuchFieldException, IllegalAccessException {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        long a = unsafe.allocateMemory(8);
        try {
            unsafe.putLong(a, 0x0102030405060708L);
            byte b = unsafe.getByte(a);
            switch (b) {
                case 0x01:
                    System.out.println("大端序");
                    break;
                case 0x08:
                    System.out.println("小端序");
                    break;
                default:
                    assert false;
            }
        } finally {
            unsafe.freeMemory(a);
        }
    }
}
