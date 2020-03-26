package com.example.blessing.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    static final String KEY_NAMA = "nama";
    static final String KEY_EMAIL = "email";
    static final String KEY_LOGGEDIN_USER = "loggedinuser";
    static final String KEY_USER_STATUS = "statuslogin";

    // Deklarasi sharedpreferences berdasarkan parameter context
    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setKeyNama(Context context, String nama) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_NAMA, nama);
        editor.apply();
    }

    // Mengembalikan nilai String dari KEY_NAMA
    public static String getKeyNama(Context context) {
        return getSharedPreferences(context).getString(KEY_NAMA, "");
    }

    public static void setKeyEmail(Context context, String email) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    // Mengembalikan nilai string dari KEY_EMAIL
    public static String getKeyEmail(Context context) {
        return getSharedPreferences(context).getString(KEY_EMAIL, "No Data");
    }

    public static void setKeyLoggedinUser(Context context, String username){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_LOGGEDIN_USER, username);
        editor.apply();
    }
    // Mengembalikan nilai string dari KEY_LOGGEDIN_USER
    public static String getKeyLoggedinUser(Context context){
        return getSharedPreferences(context).getString(KEY_LOGGEDIN_USER,"No Data");
    }

    public static void setStatusLogin(Context context, boolean status) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(KEY_USER_STATUS, status);
        editor.apply();
    }

    // Mengembalikan nilai boolean dari key KEY_USER_STATUS
    public static boolean getStatusLogin(Context context) {
        return getSharedPreferences(context).getBoolean(KEY_USER_STATUS, false);
    }

    public static void clearLoggedinUser(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(KEY_USER_STATUS);
        editor.remove(KEY_LOGGEDIN_USER);
        editor.apply();
    }
}
