package com.blanke.xandroidsocket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.blanke.xsocket.tcp.client.TcpConnConfig;
import com.blanke.xsocket.tcp.client.XTcpClient;
import com.blanke.xsocket.tcp.client.bean.TargetInfo;
import com.blanke.xsocket.tcp.client.bean.TcpMsg;
import com.blanke.xsocket.tcp.client.helper.stickpackage.StaticLenStickPackageHelper;
import com.blanke.xsocket.tcp.server.TcpServerConfig;
import com.blanke.xsocket.tcp.server.XTcpServer;
import com.blanke.xsocket.udp.client.XUdp;
import com.blanke.xsocket.utils.XSocketLog;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    byte[] testGetToken = {(byte) -95, (byte) 15, (byte) 37, (byte) 0, (byte) 52,
            (byte) 52, (byte) 57, (byte) 49, (byte) 68, (byte) 66, (byte) 54, (byte) 48, (byte) 50,
            (byte) 67, (byte) 54, (byte) 53, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
            (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
            (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 76, (byte) 65, (byte) 50, (byte) 45,
            (byte) 83, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};

    private View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XSocketLog.debug(true);
//        tcpClientTest();
    }

    private void start(Class activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    public void tcpclient(View v) {
        start(TcpclientActivity.class);
//        start(TestActivity.class);
    }

    public void tcpserver(View v) {
        start(TcpserverActivity.class);
    }

    public void udpclient(View v) {
        start(UdpclientActivity.class);
    }

    public void udpserver(View v) {
        start(UdpserverActivity.class);
    }

    private void tcpClientTest() {
//        UdpTargetInfo targetInfo = new UdpTargetInfo("192.168.88.1", 7682);
        TargetInfo targetInfo = new TargetInfo("10.155.1.221", 2222);
        XTcpClient xTcpClient = XTcpClient.getTcpClient(targetInfo);
        xTcpClient.config(new TcpConnConfig.Builder()
                .setConnTimeout(4000)
//                .setStickPackageHelper(new VariableLenStickPackageHelper(ByteOrder.LITTLE_ENDIAN, 2, 2, 4 + 8))
//                .setStickPackageHelper(new SpecifiedStickPackageHelper("888".getBytes(), "999".getBytes()))
                .setStickPackageHelper(new StaticLenStickPackageHelper(3))
                .setLocalPort(22223)
                .create());
//        xTcpClient.addTcpClientListener(this);
        xTcpClient.connect();
    }

//    private void udpClientTest() {
////        UdpTargetInfo target = new UdpTargetInfo("10.155.1.221", 3333);
//        UdpTargetInfo target = new UdpTargetInfo("255.255.255.255", 9982);
//        XUdp xUdpClient = XUdp.getUdpClient(target);
//        xUdpClient.addUdpClientListener(this);
//        xUdpClient.setUdpClientConfig(new UdpClientConfig.Builder().setLocalPorrt(1234).create());
//        byte[] suikongsearch = {(byte) 105, (byte) 112, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1};
//        xUdpClient.sendMsg(suikongsearch, true);
//    }

    private void tcpServerTest() {
        XTcpServer xTcpServer = XTcpServer.getTcpServer(4567);
//        xTcpServer.addTcpServerListener(this);
        TcpConnConfig clientConfig = new TcpConnConfig.Builder().setStickPackageHelper(new StaticLenStickPackageHelper(3)).create();
        xTcpServer.config(new TcpServerConfig.Builder().setTcpConnConfig(clientConfig).create());
        xTcpServer.startServer();
    }

    //tcp client
    public void onConnected(XTcpClient client) {
        Log.e("xxx onConnected", "onConnected");
        client.sendMsg(testGetToken);
    }

    public void onSended(XTcpClient client, TcpMsg tcpMsg) {

    }

    public void onDisconnected(XTcpClient client, String msg, Exception e) {
        Log.e("xxx onDisconnected", "onDisconnected " + msg + "," + e);
    }

    public void onReceive(XTcpClient client, TcpMsg tcpMsg) {

    }


    //udp

    public void onResponse(XUdp client, String msgs, byte[] sourceBytes) {
        Log.e("xxx onResponse", msgs + " " + Arrays.toString(sourceBytes));

    }

    public void onReceive(XUdp client, String msg, Exception e) {

    }


    //tcp server
    public void onCreated(XTcpServer server) {
        Log.e("xxx XTcpServer", server + " created");
    }

    public void onListened(XTcpServer server) {
        Log.e("xxx XTcpServer", server + " onListened");
    }

    public void onAccept(XTcpServer server, XTcpClient tcpClient) {
        Log.e("xxx XTcpServer", " 收到客户端连接请求" + tcpClient);
    }

    public void onSended(XTcpServer server, XTcpClient tcpClient, TcpMsg tcpMsg) {

    }

    public void onReceive(XTcpServer server, XTcpClient tcpClient, TcpMsg tcpMsg) {

    }

//    @Override
//    public void onReceive(XTcpServer server, XTcpClient tcpClient, Object[] msgs, byte[] sourceBytes) {
//        Log.e("xxx XTcpServer", " 收到客户端的消息 " + tcpClient + " : " + msgs + "," + Arrays.toString(sourceBytes));
//        tcpClient.sendMsg("hello " + tcpClient.getTargetInfo().getIp() + ",you msg=" + Arrays.toString(msgs));
//    }

    public void onClientClosed(XTcpServer server, XTcpClient tcpClient, String msg, Exception e) {
        Log.e("xxx XTcpServer", " 客户端连接断开" + tcpClient + msg + e);
    }

    public void onServerClosed(XTcpServer server, String msg, Exception e) {
        Log.e("xxx XTcpServer", " tcpserver 关闭" + msg + e);
    }

}
