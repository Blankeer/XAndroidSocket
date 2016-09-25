package com.blanke.xsocket.utils;

import android.util.Log;

public class XSocketLog {

    public static boolean debug = false;
    public static final String TAG = "XSocket";

    public static void debug(boolean debug1) {
        debug = debug1;
    }

    public static void d(String tag, Object o) {
        if (debug) {
            Log.d(TAG, tag + " " + o.toString());
        }
    }

    public static void e(String tag, Object o) {
        if (debug) {
            Log.e(TAG, tag + " " + o.toString());
        }
    }
}
