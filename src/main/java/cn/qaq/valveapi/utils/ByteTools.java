package cn.qaq.valveapi.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;

/**
 * @program: proto
 * @description: Byte操作工具类
 * @author: QAQ
 * @create: 2020-10-07 15:47
 **/
@Slf4j
public class ByteTools {
    public static final float byteToFloat(byte[] b) {
        int accum = 0;
        accum = accum | (b[0] & 0xff) << 0;
        accum = accum | (b[1] & 0xff) << 8;
        accum = accum | (b[2] & 0xff) << 16;
        accum = accum | (b[3] & 0xff) << 24;
        return Float.intBitsToFloat(accum);
    }
    public static final float byteToFloatBig(byte[] b) {
        int accum = 0;
        accum = accum | (b[3] & 0xff) << 0;
        accum = accum | (b[2] & 0xff) << 8;
        accum = accum | (b[1] & 0xff) << 16;
        accum = accum | (b[0] & 0xff) << 24;
        return Float.intBitsToFloat(accum);
    }
    public static final double byteToDoubleBig(Byte[] b) {
        long value = 0;
        for(int i=0;i<8;i++)
            value |= ((long) (b[i] & 0xff)) << (8*7-8 * i);

        return Double.longBitsToDouble(value);
    }
    public static final double byteToDouble(byte[] b) {
        long value = 0;
        for(int i=0;i<8;i++)
            value |= ((long) (b[i] & 0xff)) << (8 * i);

        return Double.longBitsToDouble(value);
    }
    public  static  final int hexToint(Byte[] a,int s,int len)
    {
        // 0x 12 34 56 78
        //time|=(long)(src[i+11]&0xff)<<(24-8*i);
        // 2:8 4:24 8:56 8*(len-1)-8*i
        int b=0;
        for(int i=0;i<len;i++)
            b|=(int)(a[i+s]&0xff)<<(8*(len-1)-8*i);
        return  b;

    }
    public  static  final int getBodyId(Byte[] a,int s)  {
        return (a[s]<<8&0x0f00)|(a[s+1]&0xff);
    }
    public static final Byte[] getBody(int type,Integer id)
    {
        //0x 12 34
        // 1 为type,234为id
        Byte[] a=new Byte[2];
        a[0]=(byte)(((type<<12|id)>>8)&0xff);
        a[1]=(byte)(((type<<12|id))&0xff);
        return a;
    }
    public static Long getLongFromSE(Integer s,Integer e,Long src)
    {
        return (src>>(s))&((0x01L<<(e-s+1L))-1L);
    }
    public static int getInt(Byte[] arr, int index) {
        return (0xff000000 & (arr[index + 0] << 24)) |
                (0x00ff0000 & (arr[index + 1] << 16)) |
                (0x0000ff00 & (arr[index + 2] << 8)) |
                (0x000000ff & arr[index + 3]);
    }
    public static Long getLong(byte[] arr, int index,int length) {
        Long a=0L;
        for(int i=0;i<length;i++)
        {
            a|=((arr[i+index]&0xffL)<<(8*(length-1)-8*i));
        }
        return a;
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

    public static  final Byte[] TimeToBytes(Long time)
    {
        Byte[] a=new Byte[4];
        for (int i = 0; i < 4; i++) {
            a[i]=(byte)((time>>(24 - 8 * i))&0xff);
        }
        return  a;
    }
    public  static final Byte[] getBytes(double d) {
        long value = Double.doubleToRawLongBits(d);
        Byte[] byteRet = new Byte[8];
        for (int i = 0; i < 8; i++) {
            byteRet[i] = (byte) ((value >> 8 * i) & 0xff);
        }
        return byteRet;
    }
    public  static final Byte[] getBytes(Long c)
    {
        Byte[] b=new Byte[8];
        for (int i = 0; i < 8; i++) {
            b[i] = (byte) (c >> (56-8*i));
        }
        return  b;
    }

    public static final Byte[] getBytes(Float a)
    {
        Byte[] b = new Byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (Float.floatToIntBits(a) >> (24-8*i));
        }
        return b;
    }
    public static final String hexToStr(byte[] src) throws UnsupportedEncodingException {
            int i ;
            for (i = 0; i < src.length; ++i) {
                if (src[i] == 0) {
                    break;
                }
            }
            return new String(src, 0, i, "UTF-8");
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
    /**
     * toHexString 反转
     */
    public static final Byte[] HexStringToIntByte(String a)
    {
        Byte[] t=new Byte[4];
        for(int i=0;i<3;i++)
        {

        }
        return  t;
    }
    public static final String hexTohexStr(byte[] data){
        try {
            char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            StringBuffer stringBuffer=new StringBuffer();
            for(int i=0;i<data.length;i++)
            {
                stringBuffer.append(HEX_CHAR[(data[i]<0?data[i]+256:data[i])/16]);
                stringBuffer.append(HEX_CHAR[(data[i]<0?data[i]+256:data[i])%16]);
                stringBuffer.append(" ");
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
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
    public static String getCRC(byte[] bytes) {
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
    /*
    * 定制的，减少最后2位
    *
    * */
    public static final String getCRCR(Byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length-2; i++) {
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
    public static final String getCRCR(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length-2; i++) {
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
