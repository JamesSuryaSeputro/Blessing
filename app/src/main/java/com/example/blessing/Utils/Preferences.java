package com.example.blessing.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

 public class Preferences {
    static final String KEY_ID = "id";
    static final String KEY_NAMA = "nama";
    static final String KEY_EMAIL = "email";
    static final String KEY_ROLE = "role";
    static final String KEY_NAMAROLE = "namarole";
    static final String KEY_USER_STATUS = "statuslogin";
    static final String KEY_IDSOAL = "idsoal";
    static final String KEY_NAMASOAL = "namasoal";
    static final String KEY_IDTRYOUT = "idtryout";
    static final String KEY_JUDULTRYOUT = "judultryout";
     static final String KEY_IDNILAISOAL = "idnilaisoal";
    static final String KEY_IDNILAITRYOUT = "idnilaitryout";

    // Deklarasi sharedpreferences berdasarkan parameter context
    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setKeyId(Context context, String id) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_ID, id);
        editor.apply();
    }

    // Mengembalikan nilai String dari KEY_ID
    public static String getKeyId(Context context) {
        return getSharedPreferences(context).getString(KEY_ID, "");
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

    public static void setKeyRole(Context context, String idrole) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_ROLE, idrole);
        editor.apply();
    }

    // Mengembalikan nilai string dari KEY_USER
    public static String getKeyUser(Context context) {
        return getSharedPreferences(context).getString(KEY_ROLE, "No Data");
    }

    public static void setKeyRolename(Context context, String namarole) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_NAMAROLE, namarole);
        editor.apply();
    }

    // Mengembalikan nilai string dari KEY_USER
    public static String getKeyRolename(Context context) {
        return getSharedPreferences(context).getString(KEY_NAMAROLE, "No Data");
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

    public static void setKeyIdSoal(Context context, String idsoal) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_IDSOAL, idsoal);
        editor.apply();
    }

    // Mengembalikan nilai String dari KEY_IDSOAL
    public static String getKeyIdSoal(Context context) {
        return getSharedPreferences(context).getString(KEY_IDSOAL, "");
    }

    public static void setKeyIdTryout(Context context, String idtryout) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_IDTRYOUT, idtryout);
        editor.apply();
    }

    // Mengembalikan nilai String dari KEY_IDTRYOUT
    public static String getKeyIdTryout(Context context) {
        return getSharedPreferences(context).getString(KEY_IDTRYOUT, "");
    }

    public static void setKeyNamaSoal(Context context, String namasoal) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_NAMASOAL, namasoal);
        editor.apply();
    }

    // Mengembalikan nilai String dari KEY NAMASOAL
    public static String getKeyNamaSoal(Context context) {
        return getSharedPreferences(context).getString(KEY_NAMASOAL, "");
    }

    public static void setKeyJudulTryout(Context context, String judultryout) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_JUDULTRYOUT, judultryout);
        editor.apply();
    }

    // Mengembalikan nilai String dari KEY_JUDULTRYOUT
    public static String getKeyJudulTryout(Context context) {
        return getSharedPreferences(context).getString(KEY_JUDULTRYOUT, "");
    }

     public static void setKeyIdNilaiSoal(Context context, String idnilaisoal) {
         SharedPreferences.Editor editor = getSharedPreferences(context).edit();
         editor.putString(KEY_IDNILAISOAL, idnilaisoal);
         editor.apply();
     }

     // Mengembalikan nilai String dari KEY_ID
     public static String getKeyIdNilaiSoal(Context context) {
         return getSharedPreferences(context).getString(KEY_IDNILAISOAL, "");
     }

     public static void setKeyIdNilaiTryout(Context context, String idnilaitryout) {
         SharedPreferences.Editor editor = getSharedPreferences(context).edit();
         editor.putString(KEY_IDNILAITRYOUT, idnilaitryout);
         editor.apply();
     }

     // Mengembalikan nilai String dari KEY_ID
     public static String getKeyIdNilaiTryout(Context context) {
         return getSharedPreferences(context).getString(KEY_IDNILAITRYOUT, "");
     }
    public static void clearLoggedinUser(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(KEY_USER_STATUS);
        editor.apply();
    }
}