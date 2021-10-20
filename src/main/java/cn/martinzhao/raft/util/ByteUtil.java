package cn.martinzhao.raft.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 * Convert byte to other basic type or from other basic type.
 */
@Slf4j
public class ByteUtil {
    private ByteUtil() {

    }

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f'};

    public static long byte2Long(byte[] bb) {
        return (((long) bb[0] & 0xff) << 56) | (((long) bb[1] & 0xff) << 48) | (((long) bb[2] & 0xff) << 40)
                | (((long) bb[3] & 0xff) << 32) | (((long) bb[4] & 0xff) << 24) | (((long) bb[5] & 0xff) << 16)
                | (((long) bb[6] & 0xff) << 8) | ((long) bb[7] & 0xff);
    }

    public static float byte2Float(byte[] b, int index) {
        int l;
        l = b[index];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    public static byte[] float2Byte(float f) {

        // convert float to byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }

        // reverse the array
        int len = b.length;
        // set up a new array
        byte[] dest = new byte[len];
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // swap the i th item from head and i th item from tail
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }

        return dest;

    }

    /**
     * convert to small end。
     *
     * @param n the number of the Short value
     * @return bytes array for the short value
     */
    public static byte[] shortToBytes(short n) {
        byte[] b = new byte[2];
        b[1] = (byte) (n & 0xff);
        b[0] = (byte) ((n >> 8) & 0xff);
        return b;
    }

    public static short bytesToShort(byte[] b) {
        return (short) (b[1] & 0xff | (b[0] & 0xff) << 8);
    }

    public static byte[] longToBytes(long num) {
        byte[] b = new byte[8];
        for (int i = 0; i < 8; i++) {
            b[i] = (byte) (num >>> (56 - i * 8));
        }
        return b;
    }

    /*
     * 将int转为高字节在前，低字节在后的byte数组 b[0] = 11111111(0xff) & 01100001 b[1] =
     * 11111111(0xff) & (n >> 8)00000000 b[2] = 11111111(0xff) & (n >> 8)00000000
     * b[3] = 11111111(0xff) & (n >> 8)00000000
     */
    public static byte[] intToByteArray(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }


    public static int byteArrayToInt(byte[] bArr) {
        if (bArr.length != 4) {
            return -1;
        }
        return ((bArr[0] & 0xff) << 24) | ((bArr[1] & 0xff) << 16) | ((bArr[2] & 0xff) << 8)
                | (bArr[3] & 0xff);
    }

    public static byte getXor(byte[] data) {
        byte temp = 0x00;
        for (byte datum : data) {
            temp ^= datum;
        }
        return temp;
    }

    public static String getStringByBCDCode(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            byte temp = (byte) ((b & 0xf0) >> 4);
            sb.append(byteBCDToString(temp));
            temp = (byte) (b & 0x0f);
            sb.append(byteBCDToString(temp));
        }
        return sb.toString();
    }

    private static String byteBCDToString(byte number) {
        switch (number) {
            case 0x00:
                return "0";
            case 0x01:
                return "1";
            case 0x02:
                return "2";
            case 0x03:
                return "3";
            case 0x04:
                return "4";
            case 0x05:
                return "5";
            case 0x06:
                return "6";
            case 0x07:
                return "7";
            case 0x08:
                return "8";
            case 0x09:
                return "9";
            default:
                throw new NumberFormatException();
        }
    }

    public static byte[] getBCDCodeByString(String number) {
        int length = (number.length() + 1) / 2;
        byte[] result = new byte[length];
        for (int i = number.length() - 1; i >= 0; ) {
            byte temp1 = getBCDCodeByChar(number.charAt(i));
            i--;
            if (i < 0) {
                result[length - 1] = temp1;
            } else {
                byte temp2 = getBCDCodeByChar(number.charAt(i));
                i--;
                result[length - 1] = (byte) ((byte) ((byte) (temp2 << 4) & (byte) 0xf0) | temp1);

            }
            length--;
        }
        return result;
    }

    private static byte getBCDCodeByChar(char number) {

        switch (number) {
            case '1':
                return 0x01;
            case '2':
                return 0x02;
            case '3':
                return 0x03;
            case '4':
                return 0x04;
            case '5':
                return 0x05;
            case '6':
                return 0x06;
            case '7':
                return 0x07;
            case '8':
                return 0x08;
            case '9':
                return 0x09;
            case '0':
                return 0x00;
            default:
                throw new NumberFormatException();
        }

    }

    public static Byte[] replaceByteArray(Byte[] source, ReplaceByte[] replacePair) {
        int i, j, k, l, m;
        List<Byte> result = new ArrayList<>();
        for (i = 0; i < source.length; ) {
            boolean changed = false;
            // check every change pair.
            for (j = 0; j < replacePair.length; j++) {
                for (k = 0; k < replacePair[j].getTarget().length; k++) {
                    l = i + k;
                    if (l >= source.length)
                        break;
                    if (!source[l].equals(replacePair[j].getTarget()[k])) {
                        break;
                    }
                }
                if (k == replacePair[j].getTarget().length) {
                    for (m = 0; m < replacePair[j].getReplacement().length; m++) {
                        result.add(replacePair[j].getReplacement()[m]);
                        changed = true;
                    }
                    i += replacePair[j].getTarget().length;
                    break;
                }
            }
            if (!changed) {
                result.add(source[i]);
                i++;
            }
        }
        Byte[] a = new Byte[result.size()];
        return result.toArray(a);
    }

    public static Byte[] toObjects(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];
        int i = 0;
        for (byte b : bytesPrim)
            bytes[i++] = b;
        return bytes;
    }

    public static byte[] toPrimitives(Byte[] oBytes) {
        byte[] bytes = new byte[oBytes.length];
        for (int i = 0; i < oBytes.length; i++) {
            bytes[i] = oBytes[i];
        }
        return bytes;
    }

    public static byte setByteByBit(byte source, int position) {
        byte temp1 = source;
        temp1 = (byte) (((byte) (temp1 >>> position)) << position);
        byte temp2 = 0x00;
        for (int i = 8; i > 0; i--) {
            if (i >= position) {
                temp2 <<= 1;
            } else {
                temp2 <<= 1;
                temp2 |= ((byte) 0x01);
            }
        }
        temp2 &= source;
        byte temp3 = (byte) ((byte) (0x01) << (position - 1));
        return (byte) (temp1 | temp2 | temp3);
    }

    public static Date getDateByBCDCode(byte[] bcdCode) throws ParseException {
        if (bcdCode.length != 6) {
            log.error("BCD code length is not 6 as expected!");
            return null;
        }
        String date = getStringByBCDCode(bcdCode);
        SimpleDateFormat myFmt = new SimpleDateFormat("yyMMddHHmmss");
        return myFmt.parse(date);

    }

    public static byte[] replaceKeyWords(byte[] array) {
        ReplaceByte replace1 = new ReplaceByte();
        Byte[] replacement = {0x7d, 0x02};
        replace1.setReplacement(replacement);
        Byte[] target = {0x7e};
        replace1.setTarget(target);
        ReplaceByte replace2 = new ReplaceByte();
        Byte[] replacement2 = {0x7d, 0x01};
        replace2.setReplacement(replacement2);
        Byte[] target2 = {0x7d};
        replace2.setTarget(target2);
        ReplaceByte[] replaces = new ReplaceByte[]{replace1, replace2};
        Byte[] result = ByteUtil.replaceByteArray(ByteUtil.toObjects(array), replaces);
        return ByteUtil.toPrimitives(result);
    }

    public static String bytesToHex(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int a;
        int index = 0;
        for (byte b : bytes) {
            if (b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }

            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }

        return new String(buf);
    }

    public static byte[] concatBytes(byte[]... args) {
        byte[] result = new byte[0];
        for (byte[] arg : args) {
            byte[] temp = new byte[result.length + arg.length];
            System.arraycopy(result, 0, temp, 0, result.length);
            System.arraycopy(arg, 0, temp, result.length, arg.length);
            result = temp;
        }

        return result;
    }
}
