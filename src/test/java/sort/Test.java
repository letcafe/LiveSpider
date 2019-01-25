package sort;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class Test {


    public static void main(String[] args) {
        String str = "你上大号A";
        char x = '中';
        byte[] bytes = null;
        byte[] bytes1 = null;
        bytes = str.getBytes(Charset.forName("UTF-8"));
        bytes1 = charToByte(x);
        System.out.println("bytes 大小：" + bytes.length);
        System.out.println("bytes1大小：" + bytes1.length);
    }

    public static byte[] charToByte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }
}

