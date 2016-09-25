package com.blanke.xsocket.tcp.client.helper.decode;

import com.blanke.xsocket.tcp.client.TcpConnConfig;
import com.blanke.xsocket.tcp.client.bean.TargetInfo;

public class BaseDecodeHelper implements AbsDecodeHelper {
    @Override
    public byte[][] execute(byte[] data, TargetInfo targetInfo, TcpConnConfig tcpConnConfig) {
        return new byte[][]{data};
    }
}
