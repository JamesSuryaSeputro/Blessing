package com.example.blessing;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
        private static final String PREFS_NAME = "user_pref";
        private static final String EMAIL = "email";
        private final SharedPreferences preferences;

        public Preference(Context context) {
            preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        public void setNama(String value) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(EMAIL, value);
            editor.apply();
        }

        public String getName(){
            return preferences.getString(EMAIL, "");
        }

    }