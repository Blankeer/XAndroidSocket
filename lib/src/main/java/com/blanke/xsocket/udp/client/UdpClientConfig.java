package com.blanke.xsocket.udp.client;

import com.blanke.xsocket.utils.CharsetUtil;

/**
 * udp配置
 */
public class UdpClientConfig {
    private String charsetName = CharsetUtil.UTF_8;//默认编码
    private long receiveTimeout = 10000;//接受消息的超时时间,0为无限大
    private int localPort = -1;

    private UdpClientConfig() {
    }

    public String getCharsetName() {
        return charsetName;
    }


    public long getReceiveTimeout() {
        return receiveTimeout;
    }

    public int getLocalPort() {
        return localPort;
    }

    public static class Builder {
        private UdpClientConfig mTcpConnConfig;

        public Builder() {
            mTcpConnConfig = new UdpClientConfig();
        }

        public UdpClientConfig create() {
            return mTcpConnConfig;
        }

        public Builder setCharsetName(String charsetName) {
            mTcpConnConfig.charsetName = charsetName;
            return this;
        }

        public Builder setReceiveTimeout(long timeout) {
            mTcpConnConfig.receiveTimeout = timeout;
            return this;
        }

        public Builder setLocalPort(int localPort) {
            mTcpConnConfig.localPort = localPort;
            return this;
        }
    }
}
