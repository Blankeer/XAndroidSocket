package com.blanke.xandroidsocket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blanke.xandroidsocket.layout.ConsoleLayout;
import com.blanke.xsocket.tcp.client.bean.TcpMsg;
import com.blanke.xsocket.udp.client.UdpClientConfig;
import com.blanke.xsocket.udp.client.XUdp;
import com.blanke.xsocket.udp.client.bean.UdpMsg;
import com.blanke.xsocket.udp.client.listener.UdpClientListener;
import com.blanke.xsocket.utils.StringValidationUtils;

public class UdpserverActivity extends AppCompatActivity implements View.OnClickListener, UdpClientListener {
    private EditText udpserverEditPort;
    private Button udpserverBuStart;
    private EditText udpserverEditAutoreply;
    private ConsoleLayout udpserverConsole;
    private XUdp mXUdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udpserver);
        udpserverEditPort = (EditText) findViewById(R.id.udpserver_edit_port);
        udpserverBuStart = (Button) findViewById(R.id.udpserver_bu_start);
        udpserverEditAutoreply = (EditText) findViewById(R.id.udpserver_edit_autoreply);
        udpserverConsole = (ConsoleLayout) findViewById(R.id.udpserver_console);
        udpserverBuStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.udpserver_bu_start) {
            if (mXUdp != null && mXUdp.isUdpServerRuning()) {
                mXUdp.stopUdpServer();
            } else {
                if (mXUdp == null) {
                    String port = udpserverEditPort.getText().toString().trim();
                    mXUdp = XUdp.getUdpClient();
                    if (StringValidationUtils.validateRegex(port, StringValidationUtils.RegexPort)) {
                        mXUdp.config(new UdpClientConfig.Builder().setLocalPort(Integer.parseInt(port)).create());
                    }
                    mXUdp.addUdpClientListener(this);
                }
                mXUdp.startUdpServer();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mXUdp != null) {
            mXUdp.removeUdpClientListener(this);
            mXUdp.stopUdpServer();
        }
    }

    private void addMsg(String msg) {
        udpserverConsole.addLog(msg);
    }

    @Override
    public void onStarted(XUdp XUdp) {
        addMsg("udp服务开启");
    }

    @Override
    public void onStoped(XUdp XUdp) {
        addMsg("udp服务关闭");
    }

    @Override
    public void onSended(XUdp XUdp, UdpMsg udpMsg) {
        addMsg("我：" + udpMsg.getSourceDataString());
    }

    @Override
    public void onReceive(XUdp client, UdpMsg msg) {
        addMsg("收到消息：" + msg.getSourceDataString());
        String autoReply = udpserverEditAutoreply.getText().toString().trim();
        client.sendMsg(new UdpMsg(autoReply, msg.getTarget(), TcpMsg.MsgType.Send));
    }

    @Override
    public void onError(XUdp client, String msg, Exception e) {
        addMsg("onError：" + client + msg + e);
    }
}

