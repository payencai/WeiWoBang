package com.wwb.paotui.jnitools;

public class JniTools {
    private JniTools() {
    }

    private static JniTools instance = null;

    public static JniTools getInstance() {
        if (instance == null) {
            System.loadLibrary("clib");
            instance = new JniTools();
        }
        return instance;
    }
    public static native int  Add(int x,int y);
}
