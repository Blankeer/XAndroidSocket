package com.blanke.xandroidsocket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blanke.xandroidsocket.layout.ConsoleLayout;
import com.blanke.xsocket.tcp.client.TcpConnConfig;
import com.blanke.xsocket.tcp.client.XTcpClient;
import com.blanke.xsocket.tcp.client.bean.TcpMsg;
import com.blanke.xsocket.tcp.server.TcpServerConfig;
import com.blanke.xsocket.tcp.server.XTcpServer;
import com.blanke.xsocket.tcp.server.listener.TcpServerListener;
import com.blanke.xsocket.utils.CharsetUtil;
import com.blanke.xsocket.utils.StringValidationUtils;

public class TcpserverActivity extends AppCompatActivity implements View.OnClickListener, TcpServerListener {
    private EditText tcpserverEditPort;
    private Button tcpserverBuConnect;
    private EditText tcpserverEdit;
    private Button tcpserverBuSend;
    private ConsoleLayout tcpserverConsole;
    private XTcpServer mXTcpServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpserver);
        tcpserverEditPort = (EditText) findViewById(R.id.tcpserver_edit_port);
        tcpserverBuConnect = (Button) findViewById(R.id.tcpserver_bu_connect);
        tcpserverEdit = (EditText) findViewById(R.id.tcpserver_edit);
        tcpserverBuSend = (Button) findViewById(R.id.tcpserver_bu_send);
        tcpserverConsole = (ConsoleLayout) findViewById(R.id.tcpserver_console);

        tcpserverBuSend.setOnClickListener(this);
        tcpserverBuConnect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tcpserver_bu_connect) {
            tcpserverConsole.clearConsole();
            if (mXTcpServer != null && mXTcpServer.isListening()) {
                mXTcpServer.stopServer();
            } else {
                String port = tcpserverEditPort.getText().toString().trim();
                if (StringValidationUtils.validateRegex(port, StringValidationUtils.RegexPort)) {
                    if (mXTcpServer == null) {
                        mXTcpServer = XTcpServer.getTcpServer(Integer.parseInt(port));
                        mXTcpServer.addTcpServerListener(this);
                        mXTcpServer.config(new TcpServerConfig.Builder()
                                .setTcpConnConfig(new TcpConnConfig.Builder().create()).create());
                    }
                    mXTcpServer.startServer();
                    addMsg("tcp 服务开启 " + port);
                } else {
                    addMsg("端口输入错误");
                }
            }
        } else {
            String text = tcpserverEdit.getText().toString().trim();
            if (mXTcpServer != null) {
                mXTcpServer.sendMsgToAll(text);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mXTcpServer != null) {
            mXTcpServer.removeTcpServerListener(this);
//            mXTcpServer.stopServer();
        }
    }

    private void addMsg(String msg) {
        tcpserverConsole.addLog(msg);
    }


    @Override
    public void onCreated(XTcpServer server) {
        addMsg("服务启动成功");
    }

    @Override
    public void onListened(XTcpServer server) {
        addMsg("服务listenling " + server.getPort());
    }

    @Override
    public void onAccept(XTcpServer server, XTcpClient tcpClient) {
        addMsg("收到客户端连接请求 " + tcpClient.getTargetInfo().getIp());
    }

    @Override
    public void onSended(XTcpServer server, XTcpClient tcpClient, TcpMsg tcpMsg) {
        addMsg("发送消息给 " + tcpClient.getTargetInfo().getIp() + " 成功 msg= " + tcpMsg.getSourceDataString());
    }

    @Override
    public void onReceive(XTcpServer server, XTcpClient tcpClient, TcpMsg tcpMsg) {
        addMsg("收到客户端消息 " + tcpClient.getTargetInfo().getIp() + "," + CharsetUtil.dataToString(tcpMsg.getSourceDataBytes(), CharsetUtil.UTF_8));
    }

    @Override
    public void onValidationFail(XTcpServer server, XTcpClient client, TcpMsg tcpMsg) {

    }

    @Override
    public void onClientClosed(XTcpServer server, XTcpClient tcpClient, String msg, Exception e) {
        addMsg("客户端连接断开 " + tcpClient.getTargetInfo().getIp() + msg + e);
    }

    @Override
    public void onServerClosed(XTcpServer server, String msg, Exception e) {
        addMsg("服务器关闭 " + server + msg + e);
    }
}

