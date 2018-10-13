package com.wwb.paotui.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.wwb.paotui.bean.Account;
import com.wwb.paotui.bean.Userinfo;

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

    /**
     * 保存用户信息
     * @param userinfo
     */
    public void setUserinfo(Userinfo userinfo)  {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(bos);
            os.writeObject(userinfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String user = new String(Base64.encode(bos.toByteArray(), Base64.DEFAULT));
        editor.putString("userinfo", user);
        editor.apply();
    }
    public Userinfo getUserinfo()  {
        String user = mSharedPreferences.getString("userinfo", "");
        byte[] stringToBytes = Base64.decode(user, Base64.DEFAULT);
        ByteArrayInputStream bis=new ByteArrayInputStream(stringToBytes);
        ObjectInputStream is = null;
        Userinfo userinfo = null;
        try {
            is = new ObjectInputStream(bis);
            userinfo = (Userinfo) is.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userinfo;
    }

    /**
     * 保存账户，用于自动登录
     * @param account
     */
    public void setAccount(Account account){
        editor.putString("password", account.getPassword());
        editor.putString("username",account.getUsername());
        editor.putString("openid",account.getOpenid());
        editor.apply();

    }
    public Account getAccount(){
        String password=mSharedPreferences.getString("password", "");
        String username=mSharedPreferences.getString("username", "");
        String openid=mSharedPreferences.getString("openid", "");
        Account account=new Account(username,password);
        account.setOpenid(openid);
        return account;
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
