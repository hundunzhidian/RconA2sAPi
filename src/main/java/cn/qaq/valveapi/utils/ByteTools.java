package cn.qaq.valveapi.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: proto
 * @description: Byte操作工具类
 * @author: QAQ
 * @create: 2020-10-07 15:47
 **/
@Slf4j
public class ByteTools {


    @SneakyThrows
    public static final String getString(byte[] src, AtomicInteger index)
    {
        int i=0;
        for(;i<src.length-index.get();i++)
        {
            if(src[i+index.get()]==(byte)0x00)
            {
                break;
            }
        }
        return new String(src, index.getAndAdd(i+1), i, "UTF-8");
    }
    public static Long getLongSmall(byte[] arr, int index,int length) {
        Long a=0L;
        for(int i=0;i<length;i++)
        {
            a|=((arr[i+index]&0xffL)<<(8*i));
        }
        return a;
    }
    public  static final Byte[] getBytesSmall(Long c)
    {
        Byte[] b=new Byte[8];
        for (int i = 0; i < 8; i++) {
            b[i] = (byte) (c >> (8*i));
        }
        return  b;
    }
    public  static final Byte[] getBytesSmall(Integer c)
    {
        Byte[] b=new Byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (c >> (8*i));
        }
        return  b;
    }
    public static final Integer getIntegerSmall(byte[] src, AtomicInteger index,Integer length)
    {
        Integer tar=0;
        for(int i=0;i<length;i++)
        {
            tar|=(0xffffffff & (src[index.get() + (length-1-i)] << 8*(length-1-i)));
        }
        index.getAndAdd(length);
        if(length==1)
        {
            if(tar<0)tar+=256;//消除单字节问题
        }
        return tar;
    }
    public static final boolean getBoolean(byte[] src, AtomicInteger index)
    {
        return src[index.getAndAdd(1)]==(byte)0x01;
    }
    public static Long getLongBig(byte[] arr, int index,int length) {
        Long a=0L;
        for(int i=0;i<length;i++)
        {
            a|=((arr[i+index]&0x000000ffL)<<(8*(length-1)-8*i));
        }
        return a;
    }
    public static int getInt(byte[] arr, int index) {
        return (0xff000000 & (arr[index + 0] << 24)) |
                (0x00ff0000 & (arr[index + 1] << 16)) |
                (0x0000ff00 & (arr[index + 2] << 8)) |
                (0x000000ff & arr[index + 3]);
    }
    public  static final Byte[] getBytes(Integer c)
    {
        Byte[] b=new Byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (c >> (24-8*i));
        }
        return  b;
    }

    public static final Float getFloat(byte[] b) {
        int accum = 0;
        accum = accum|(b[0] & 0xff) << 0;
        accum = accum|(b[1] & 0xff) << 8;
        accum = accum|(b[2] & 0xff) << 16;
        accum = accum|(b[3] & 0xff) << 24;
        return Float.intBitsToFloat(accum);
    }
    public static final byte[] hexStrToBinaryStr(String hexString) {
        if(hexString==null) return null;
        String[] tmp = hexString.split(" ");
        byte[] tmpBytes = new byte[tmp.length];
        int i = 0;
        for (String b : tmp) {
            if (b.equals("FF")) {
                tmpBytes[i++] = -1;
            } else {
                tmpBytes[i++] = Integer.valueOf(b, 16).byteValue();
            }
        }
        return tmpBytes;
    }
    public static final byte[] hexStrToBinaryStrbyte(String hexString) {
        if(hexString==null) return null;
        String[] tmp = hexString.split(" ");
        byte[] tmpBytes = new byte[tmp.length];
        int i = 0;
        for (String b : tmp) {
            if (b.equals("FF")) {
                tmpBytes[i++] = -1;
            } else {
                tmpBytes[i++] = Integer.valueOf(b, 16).byteValue();
            }
        }
        return tmpBytes;
    }
    public static  final String intToByteString(int src)
    {
        char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        return String.valueOf(HEX_CHAR[(src<0?src+256:src)/16])+String.valueOf(HEX_CHAR[(src<0?src+256:src)/16]);
    }
    public static final String hexTohexStr(Byte[] data) {

            char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            StringBuffer stringBuffer=new StringBuffer();
            for(int i=0;i<data.length;i++)
            {
                if(data[i]==null) break;
                stringBuffer.append(HEX_CHAR[(data[i]<0?data[i]+256:data[i])/16]);
                stringBuffer.append(HEX_CHAR[(data[i]<0?data[i]+256:data[i])%16]);
                stringBuffer.append(" ");
            }
            return stringBuffer.toString();
    }
    public static String getCRC(Byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        return Integer.toHexString(CRC);
    }
    public static final void arraycopy(byte[] src,int srcPos,Byte[] dest,int destPos,int length)
    {
        for(int i=0;i<length;i++)
        {
            dest[destPos+i]=src[srcPos+i];
        }
    }
    public static final void arraycopy(byte[] src,int srcPos,byte[] dest,int destPos,int length)
    {
        for(int i=0;i<length;i++)
        {
            dest[destPos+i]=src[srcPos+i];
        }
    }
    public static final void arraycopy(Byte[] src,int srcPos,byte[] dest,int destPos,int length)
    {
        for(int i=0;i<length;i++)
        {
            dest[destPos+i]=src[srcPos+i];
        }
    }
    public static final void arraycopy(Byte[] src,int srcPos,Byte[] dest,int destPos,int length)
    {
        for(int i=0;i<length;i++)
        {
            dest[destPos+i]=src[srcPos+i];
        }
    }
}
