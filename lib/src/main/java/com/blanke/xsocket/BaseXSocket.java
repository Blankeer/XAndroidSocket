package com.blanke.xsocket;

import android.os.Handler;
import android.os.Looper;

/**
 */
public abstract class BaseXSocket {
    private Handler mUIHandler;
    protected Object lock;

    public BaseXSocket() {
        mUIHandler = new Handler(Looper.getMainLooper());
        lock = new Object();
    }

    protected void runOnUiThread(Runnable runnable) {
        mUIHandler.post(runnable);
    }
}
