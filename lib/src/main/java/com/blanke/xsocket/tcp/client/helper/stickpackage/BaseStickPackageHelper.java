package com.blanke.xsocket.tcp.client.helper.stickpackage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 最简单的做法，不处理粘包，直接读取返回，最大长度256
 */
public class BaseStickPackageHelper implements AbsStickPackageHelper {
    private int maxLen = 256;//最大长度256

    public BaseStickPackageHelper() {
    }

    public BaseStickPackageHelper(int maxLen) {
        if (maxLen > 0) {
            this.maxLen = maxLen;
        }
    }

    @Override
    public byte[] execute(InputStream is) {
        byte[] bytes = new byte[maxLen];
        int len;
        try {
            if ((len = is.read(bytes)) != -1) {
                return Arrays.copyOf(bytes, len);
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
        return null;
    }
}
