package com.blanke.xsocket.tcp.server;

import com.blanke.xsocket.BaseXSocket;
import com.blanke.xsocket.tcp.client.XTcpClient;
import com.blanke.xsocket.tcp.client.bean.TargetInfo;
import com.blanke.xsocket.tcp.client.bean.TcpMsg;
import com.blanke.xsocket.tcp.client.listener.TcpClientListener;
import com.blanke.xsocket.tcp.server.listener.TcpServerListener;
import com.blanke.xsocket.tcp.server.manager.TcpServerManager;
import com.blanke.xsocket.tcp.server.state.ServerState;
import com.blanke.xsocket.utils.XSocketLog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * tcp服务端
 */
public class XTcpServer extends BaseXSocket implements TcpClientListener {
    private static final String TAG = "XTcpServer";
    protected int port;
    protected ServerState mServerState;
    protected ServerSocket mServerSocket;
    protected Map<TargetInfo, XTcpClient> mXTcpClients;
    protected ListenThread mListenThread;
    protected TcpServerConfig mTcpServerConfig;
    protected List<TcpServerListener> mTcpServerListeners;

    private XTcpServer() {
        super();
    }

    public static XTcpServer getTcpServer(int port) {
        XTcpServer xTcpServer = TcpServerManager.getTcpServer(port);
        if (xTcpServer == null) {
            xTcpServer = new XTcpServer();
            xTcpServer.init(port);
            TcpServerManager.putTcpServer(xTcpServer);
        }
        return xTcpServer;
    }

    private void init(int port) {
        this.port = port;
        setServerState(ServerState.Closed);
        mXTcpClients = new LinkedHashMap<>();
        mTcpServerListeners = new ArrayList<>();
        if (mTcpServerConfig == null) {
            mTcpServerConfig = new TcpServerConfig.Builder().create();
        }
    }

    //开启tcpserver
    public void startServer() {
        if (!getListenThread().isAlive()) {
            XSocketLog.d(TAG, "tcp server启动ing ");
            getListenThread().start();
        }
    }

    public void stopServer() {
        stopServer("手动关闭tcpServer", null);
    }

    protected void stopServer(String msg, Exception e) {
        getListenThread().interrupt();//关闭listen
        setServerState(ServerState.Closed);
        if (closeSocket()) {
            for (XTcpClient client : mXTcpClients.values()) {
                if (client != null) {
                    client.disconnect();
                }
            }
            notifyTcpServerClosed(msg, e);
        }
        XSocketLog.d(TAG, "tcp server closed");
    }

    private boolean closeSocket() {
        if (mServerSocket != null && !mServerSocket.isClosed()) {
            try {
                mServerSocket.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean sendMsgToAll(TcpMsg msg) {
        boolean re = true;
        for (XTcpClient client : mXTcpClients.values()) {
            if (client.sendMsg(msg) == null) {
                re = false;
            }
        }
        return re;
    }

    public boolean sendMsgToAll(String msg) {
        boolean re = true;
        for (XTcpClient client : mXTcpClients.values()) {
            if (client.sendMsg(msg) == null) {
                re = false;
            }
        }
        return re;
    }

    public boolean sendMsgToAll(byte[] msg) {
        boolean re = true;
        for (XTcpClient client : mXTcpClients.values()) {
            if (client.sendMsg(msg) == null) {
                re = false;
            }
        }
        return re;
    }

    public boolean sendMsg(TcpMsg msg, XTcpClient client) {
        return client.sendMsg(msg) != null;
    }

    public boolean sendMsg(String msg, XTcpClient client) {
        return client.sendMsg(msg) != null;
    }

    public boolean sendMsg(byte[] msg, XTcpClient client) {
        return client.sendMsg(msg) != null;
    }

    public boolean sendMsg(TcpMsg msg, String ip) {
        XTcpClient client = mXTcpClients.get(ip);
        if (client != null) {
            return client.sendMsg(msg) != null;
        }
        return false;
    }

    public boolean sendMsg(String msg, String ip) {
        XTcpClient client = mXTcpClients.get(ip);
        if (client != null) {
            return client.sendMsg(msg) != null;
        }
        return false;
    }

    public boolean sendMsg(byte[] msg, String ip) {
        XTcpClient client = mXTcpClients.get(ip);
        if (client != null) {
            return client.sendMsg(msg) != null;
        }
        return false;
    }

    @Override
    public void onConnected(XTcpClient client) {
        //no callback,ignore
    }

    @Override
    public void onSended(XTcpClient client, TcpMsg tcpMsg) {
        notifyTcpServerSended(client, tcpMsg);
    }

    @Override
    public void onDisconnected(XTcpClient client, String msg, Exception e) {
        notifyTcpClientClosed(client, msg, e);
    }

    @Override
    public void onReceive(XTcpClient client, TcpMsg tcpMsg) {
        notifyTcpServerReceive(client, tcpMsg);
    }

    @Override
    public void onValidationFail(XTcpClient client, TcpMsg tcpMsg) {
        notifyTcpServerValidationFail(client, tcpMsg);
    }

    class ListenThread extends Thread {
        @Override
        public void run() {
            Socket socket;
            while (!Thread.interrupted()) {
                try {
                    XSocketLog.d(TAG, "tcp server listening");
                    socket = getServerSocket().accept();
                    TargetInfo targetInfo = new TargetInfo(socket.getInetAddress().getHostAddress(), socket.getPort());
                    XTcpClient xTcpClient = XTcpClient.getTcpClient(socket, targetInfo,
                            mTcpServerConfig.getTcpConnConfig());//创建一个client，接受和发送消息
                    notifyTcpServerAccept(xTcpClient);
                    xTcpClient.addTcpClientListener(XTcpServer.this);
                    mXTcpClients.put(targetInfo, xTcpClient);
                } catch (IOException e) {
                    XSocketLog.d(TAG, "tcp server listening error:" + e);
//                    e.printStackTrace();
                    stopServer("监听失败", e);
//                    return;
                }
            }
        }
    }

    protected ListenThread getListenThread() {
        if (mListenThread == null || !mListenThread.isAlive()) {
            mListenThread = new ListenThread();
        }
        return mListenThread;
    }

    protected ServerSocket getServerSocket() {
        if (mServerSocket == null || mServerSocket.isClosed()) {
            try {
                mServerSocket = new ServerSocket(port);
                setServerState(ServerState.Created);
                notifyTcpServerCreate();
                setServerState(ServerState.Listening);
                notifyTcpServerLinten();
            } catch (IOException e) {
//                e.printStackTrace();
                stopServer("创建失败", e);
            }
        }
        return mServerSocket;
    }

    public void addTcpServerListener(TcpServerListener listener) {
        if (mTcpServerListeners.contains(listener)) {
            return;
        }
        this.mTcpServerListeners.add(listener);
    }

    public void removeTcpServerListener(TcpServerListener listener) {
        this.mTcpServerListeners.remove(listener);
    }

    private void notifyTcpServerCreate() {
        for (TcpServerListener wr : mTcpServerListeners) {
            final TcpServerListener l = wr;
            if (l != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l.onCreated(XTcpServer.this);
                    }
                });
            }
        }
    }

    private void notifyTcpServerLinten() {
        for (TcpServerListener wr : mTcpServerListeners) {
            final TcpServerListener l = wr;
            if (l != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l.onListened(XTcpServer.this);
                    }
                });
            }
        }
    }

    private void notifyTcpServerAccept(final XTcpClient client) {
        for (TcpServerListener wr : mTcpServerListeners) {
            final TcpServerListener l = wr;
            if (l != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l.onAccept(XTcpServer.this, client);
                    }
                });
            }
        }
    }

    private void notifyTcpServerReceive(final XTcpClient client, final TcpMsg tcpMsg) {
        for (TcpServerListener wr : mTcpServerListeners) {
            final TcpServerListener l = wr;
            if (l != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l.onReceive(XTcpServer.this, client, tcpMsg);
                    }
                });
            }
        }
    }

    private void notifyTcpServerSended(final XTcpClient client, final TcpMsg tcpMsg) {
        for (TcpServerListener wr : mTcpServerListeners) {
            final TcpServerListener l = wr;
            if (l != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l.onSended(XTcpServer.this, client, tcpMsg);
                    }
                });
            }
        }
    }

    private void notifyTcpServerValidationFail(final XTcpClient client, final TcpMsg tcpMsg) {
        for (TcpServerListener wr : mTcpServerListeners) {
            final TcpServerListener l = wr;
            if (l != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l.onValidationFail(XTcpServer.this, client, tcpMsg);
                    }
                });
            }
        }
    }

    private void notifyTcpClientClosed(final XTcpClient client, final String msg, final Exception e) {
        for (TcpServerListener wr : mTcpServerListeners) {
            final TcpServerListener l = wr;
            if (l != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l.onClientClosed(XTcpServer.this, client, msg, e);
                    }
                });
            }
        }
    }

    private void notifyTcpServerClosed(final String msg, final Exception e) {
        for (TcpServerListener wr : mTcpServerListeners) {
            final TcpServerListener l = wr;
            if (l != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l.onServerClosed(XTcpServer.this, msg, e);
                    }
                });
            }
        }
    }


    public int getPort() {
        return port;
    }

    private void setServerState(ServerState state) {
        this.mServerState = state;
    }

    public boolean isClosed() {
        return mServerState == ServerState.Closed;
    }

    public boolean isListening() {
        return mServerState == ServerState.Listening;
    }

    public void config(TcpServerConfig tcpServerConfig) {
        mTcpServerConfig = tcpServerConfig;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Xtcpserver port=" + port + ",state=" + mServerState);
        sb.append(" client size=" + mXTcpClients.size());
        return sb.toString();
    }
}
