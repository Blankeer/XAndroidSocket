package com.blanke.xsocket.tcp.client.helper.decode;

import com.blanke.xsocket.tcp.client.TcpConnConfig;
import com.blanke.xsocket.tcp.client.bean.TargetInfo;

/**
 * 解析消息的处理
 */
public interface AbsDecodeHelper {
    byte[][] execute(byte[] data, TargetInfo targetInfo, TcpConnConfig tcpConnConfig);
}
