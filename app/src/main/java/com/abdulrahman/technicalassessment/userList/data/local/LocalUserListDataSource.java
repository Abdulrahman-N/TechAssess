package com.abdulrahman.technicalassessment.userList.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class LocalUserListDataSource {

    private final Context context;

    @Inject
    LocalUserListDataSource(@ApplicationContext Context context) {
        this.context = context;
    }

    private static final String PREFS_FILE = "prefs";
    private static final String KEY_JSON = "encrypted_json";

    public void storeEncryptedJson(String string) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_JSON, string);
        editor.apply();
    }

    public String getEncryptedJson() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_JSON, null);
    }
}