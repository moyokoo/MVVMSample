package net.android.anko.helper;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.android.anko.AnkkoApp;

import java.util.Map;

public class PrefHelper {

    /**
     * @param key
     *         ( the Key to used to retrieve this data later  )
     * @param value
     *         ( any kind of primitive values  )
     *         <p/>
     *         non can be null!!!
     */
    @SuppressLint("ApplySharedPref") public static <T> void set(@NonNull String key, @Nullable T value) {
        if (InputHelper.isEmpty(key)) {
            throw new NullPointerException("Key must not be null! (key = " + key + "), (value = " + value + ")");
        }
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(AnkkoApp.getInstance()).edit();
        if (InputHelper.isEmpty(value)) {
            clearKey(key);
            return;
        }
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        } else {
            edit.putString(key, value.toString());
        }
        edit.commit();//apply on UI
    }

    @Nullable
    public static String getString(@NonNull String key) {
        return PreferenceManager.getDefaultSharedPreferences(AnkkoApp.getInstance()).getString(key, null);
    }

    public static String getString(@NonNull String key,String defaultKey) {
        return PreferenceManager.getDefaultSharedPreferences(AnkkoApp.getInstance()).getString(key, defaultKey);
    }

    public static boolean getBoolean(@NonNull String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AnkkoApp.getInstance());
        return preferences.getAll().get(key) instanceof Boolean && preferences.getBoolean(key, false);
    }

    public static int getInt(@NonNull String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AnkkoApp.getInstance());
        return preferences.getAll().get(key) instanceof Integer ? preferences.getInt(key, 0) : -1;
    }

    public static long getLong(@NonNull String key) {
        return PreferenceManager.getDefaultSharedPreferences(AnkkoApp.getInstance()).getLong(key, 0);
    }

    public static float getFloat(@NonNull String key) {
        return PreferenceManager.getDefaultSharedPreferences(AnkkoApp.getInstance()).getFloat(key, 0);
    }

    public static void clearKey(@NonNull String key) {
        PreferenceManager.getDefaultSharedPreferences(AnkkoApp.getInstance()).edit().remove(key).apply();
    }

    public static boolean isExist(@NonNull String key) {
        return PreferenceManager.getDefaultSharedPreferences(AnkkoApp.getInstance()).contains(key);
    }

    public static void clearPrefs() {
        PreferenceManager.getDefaultSharedPreferences(AnkkoApp.getInstance()).edit().clear().apply();
    }

    public static Map<String, ?> getAll() {
        return PreferenceManager.getDefaultSharedPreferences(AnkkoApp.getInstance()).getAll();
    }

}
