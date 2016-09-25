package com.blanke.xsocket.udp.client.manager;

import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class UdpSocketManager {
    private static Map<Integer, DatagramSocket> sDatagramSockets = new HashMap();

    public static void putUdpSocket(DatagramSocket socket) {
        sDatagramSockets.put(socket.getLocalPort(), socket);
    }

    public static DatagramSocket getUdpSocket(int port) {
        return sDatagramSockets.get(port);
    }
}
