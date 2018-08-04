package com.weiwobang.paotui.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.weiwobang.paotui.bean.Userinfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PreferenceManager {
    public static final String PREFERENCE_NAME = "wwb_saveInfo";
    private static SharedPreferences mSharedPreferences;
    private static PreferenceManager mPreferencemManager;

    private static SharedPreferences.Editor editor;
    @SuppressLint("CommitPrefEdits")
    private PreferenceManager(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static synchronized void init(Context cxt){
        if(mPreferencemManager == null){
            mPreferencemManager = new PreferenceManager(cxt);
        }
    }
    public void setUserinfo(Userinfo userinfo) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(userinfo);
        String user = new String(Base64.encode(bos.toByteArray(), Base64.DEFAULT));
        editor.putString("userinfo", user);
        editor.apply();
    }
    public Userinfo getUserinfo() throws IOException, ClassNotFoundException {
        String user = mSharedPreferences.getString("userinfo", "");
        byte[] stringToBytes = Base64.decode(user, Base64.DEFAULT);
        ByteArrayInputStream bis=new ByteArrayInputStream(stringToBytes);
        ObjectInputStream is = new ObjectInputStream(bis);
        Userinfo userinfo = (Userinfo) is.readObject();
        return userinfo;
    }
    /**
     * get instance of PreferenceManager
     *
     * @param
     * @return
     */
    public synchronized static PreferenceManager getInstance() {
        if (mPreferencemManager == null) {
            throw new RuntimeException("please init first!");
        }

        return mPreferencemManager;
    }
}
