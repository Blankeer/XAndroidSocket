package com.blanke.xandroidsocket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blanke.xandroidsocket.layout.ConsoleLayout;
import com.blanke.xandroidsocket.layout.StaticPackageLayout;
import com.blanke.xsocket.tcp.client.TcpConnConfig;
import com.blanke.xsocket.tcp.client.XTcpClient;
import com.blanke.xsocket.tcp.client.bean.TargetInfo;
import com.blanke.xsocket.tcp.client.bean.TcpMsg;
import com.blanke.xsocket.tcp.client.helper.stickpackage.AbsStickPackageHelper;
import com.blanke.xsocket.tcp.client.listener.TcpClientListener;
import com.blanke.xsocket.utils.StringValidationUtils;

import java.util.Arrays;

public class TcpclientActivity extends AppCompatActivity implements View.OnClickListener, TcpClientListener {

    private Button tcpclientBuConnect;
    private EditText tcpclientEdit;
    private EditText tcpclientEditIp;
    private Button tcpclientBuSend;
    private StaticPackageLayout tcpclientStaticpackagelayout;
    private ConsoleLayout tcpclientConsole;
    private SwitchCompat tcpclientSwitchReconnect;
    private XTcpClient xTcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpclient);
        tcpclientBuConnect = (Button) findViewById(R.id.tcpclient_bu_connect);
        tcpclientEdit = (EditText) findViewById(R.id.tcpclient_edit);
        tcpclientBuSend = (Button) findViewById(R.id.tcpclient_bu_send);
        tcpclientStaticpackagelayout = (StaticPackageLayout) findViewById(R.id.tcpclient_staticpackagelayout);
        tcpclientEditIp = (EditText) findViewById(R.id.tcpclient_edit_ip);
        tcpclientConsole = (ConsoleLayout) findViewById(R.id.tcpclient_console);
        tcpclientSwitchReconnect = (SwitchCompat) findViewById(R.id.tcpclient_switch_reconnect);
        tcpclientBuConnect.setOnClickListener(this);
        tcpclientBuSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tcpclient_bu_connect) {
            tcpclientConsole.clearConsole();
            if (xTcpClient != null && xTcpClient.isConnected()) {
                xTcpClient.disconnect();
            } else {
                AbsStickPackageHelper stickHelper = tcpclientStaticpackagelayout.getStickPackageHelper();
                if (stickHelper == null) {
                    addMsg("粘包参数设置错误");
                    return;
                }
                String temp = tcpclientEditIp.getText().toString().trim();
                String[] temp2 = temp.split(":");
                if (temp2.length == 2 && StringValidationUtils.validateRegex(temp2[0], StringValidationUtils.RegexIP)
                        && StringValidationUtils.validateRegex(temp2[1], StringValidationUtils.RegexPort)) {
                    TargetInfo targetInfo = new TargetInfo(temp2[0], Integer.parseInt(temp2[1]));
                    xTcpClient = XTcpClient.getTcpClient(targetInfo);
                    xTcpClient.addTcpClientListener(this);
                    xTcpClient.config(new TcpConnConfig.Builder()
                            .setStickPackageHelper(stickHelper)//粘包
                            .setIsReconnect(tcpclientSwitchReconnect.isChecked())
                            .create());
                    if (xTcpClient.isDisconnected()) {
                        xTcpClient.connect();
                    } else {
                        addMsg("已经存在该连接");
                    }
                } else {
                    addMsg("服务器地址必须是 ip:port 形式");
                }
            }
        } else {//send msg
            String text = tcpclientEdit.getText().toString().trim();
            if (xTcpClient != null) {
                xTcpClient.sendMsg(text);
            } else {
                addMsg("还没有连接到服务器");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (xTcpClient != null) {
            xTcpClient.removeTcpClientListener(this);
//            xTcpClient.disconnect();//activity销毁时断开tcp连接
        }
    }

    private void addMsg(String msg) {
        this.tcpclientConsole.addLog(msg);
    }

    @Override
    public void onConnected(XTcpClient client) {
        addMsg(client.getTargetInfo().getIp() + "连接成功");
    }

    @Override
    public void onSended(XTcpClient client, TcpMsg tcpMsg) {
        addMsg("我:" + tcpMsg.getSourceDataString());
    }

    @Override
    public void onDisconnected(XTcpClient client, String msg, Exception e) {
        addMsg(client.getTargetInfo().getIp() + "断开连接 " + msg + e);
    }

    @Override
    public void onReceive(XTcpClient client, TcpMsg msg) {
        byte[][] res = msg.getEndDecodeData();
        byte[] bytes = new byte[0];
        for (byte[] i : res) {
            bytes = i;
            break;
        }
        addMsg(client.getTargetInfo().getIp() + ":" + " len= " + bytes.length + ", "
                + msg.getSourceDataString() + " bytes=" + Arrays.toString(bytes));
    }

    @Override
    public void onValidationFail(XTcpClient client, TcpMsg tcpMsg) {

    }
}

