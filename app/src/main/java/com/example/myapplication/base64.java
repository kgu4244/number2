package com.example.myapplication;

import android.util.Base64;

public class base64 {
    public static byte[] decrytBASE64(String key) throws Exception{
        return Base64.decode(key, Base64.DEFAULT);
    }

    public static String encrytBASE64(byte[] key) throws Exception{
        return Base64.encodeToString(key, Base64.DEFAULT);
    }
}
