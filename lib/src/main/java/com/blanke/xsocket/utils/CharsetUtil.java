package com.blanke.xsocket.utils;

import java.io.UnsupportedEncodingException;

public class CharsetUtil {
    public static final String UTF_8 = "UTF-8";
    public static final String GBK = "GBK";


    public static byte[] stringToData(String string, String charsetName) {
        if (string != null) {
            try {
                return string.getBytes(charsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String dataToString(byte[] data, String charsetName) {
        if (data != null) {
            try {
                return new String(data, charsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}