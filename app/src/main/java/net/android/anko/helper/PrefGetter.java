package net.android.anko.helper;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import net.android.anko.AnkkoApp;
import net.android.anko.model.model.UserGithubModel;

public class PrefGetter {
    private static final String TOKEN = "token";
    private static final String OTP_CODE = "otp_code";
    private static final String USER_GITHUB = "USER_GITHUB";
    public final static String LANGUAGE = "language";
    public final static String WHITE_THEME = "whiteTheme";
    public final static String CODE_WRAP = "codeWrap";
    public final static String GANk_TYPE = "gankType";

    public static Gson getGson() {
        return new Gson();
    }


    public static String getToken() {
        return PrefHelper.getString(TOKEN);
//        return "89576ba64069bc58b8a067196f5f240aa84a10e1";
    }

    public static void setToken(@Nullable String token) {
        PrefHelper.set(TOKEN, token);
    }

    public static void cleanToken() {
        PrefHelper.set(TOKEN, "");
    }

    public static void cleanUserGithub() {
        PrefHelper.set(USER_GITHUB, "");
    }

    public static String getOtpCode() {
        return PrefHelper.getString(OTP_CODE);
    }

    public static void setOtpCode(@Nullable String otp) {
        PrefHelper.set(OTP_CODE, otp);
    }

    public static UserGithubModel getUserGithub() {
        return getGson().fromJson(PrefHelper.getString(USER_GITHUB), UserGithubModel.class);
    }

    public static String getUserGithubName() {
        if (getUserGithub() == null) {
            return "";
        } else {
            return getUserGithub().getLogin();
        }
    }

    public static void setUserGithub(@Nullable UserGithubModel model) {
        PrefHelper.set(USER_GITHUB, getGson().toJson(model));
    }

    public static String getLanguage() {
        return PrefHelper.getString(LANGUAGE, "en");
    }

    public static Boolean isDarkTheme() {
        return false;
//        return PrefHelper.getBoolean(WHITE_THEME);
    }

    public static void setCodeWrap(boolean b) {
        PrefHelper.set(CODE_WRAP, b);
    }

    public static Boolean isCodeWrap() {
        return false;
    }

    public static boolean isAppAnimationDisabled() {
        return PrefHelper.getBoolean("app_animation");
    }

    public static String gankTypeName() {
        return PrefHelper.getString(GANk_TYPE, "Android");
    }
}
