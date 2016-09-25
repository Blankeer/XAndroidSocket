package com.blanke.xsocket.udp.client.bean;

import com.blanke.xsocket.tcp.client.bean.TargetInfo;
import com.blanke.xsocket.tcp.client.bean.TcpMsg;

/**
 */
public class UdpMsg extends TcpMsg {

    public UdpMsg(byte[] data, TargetInfo target, MsgType type) {
        super(data, target, type);
    }

    public UdpMsg(String data, TargetInfo target, MsgType type) {
        super(data, target, type);
    }

    public UdpMsg(int id) {
        super(id);
    }
}
