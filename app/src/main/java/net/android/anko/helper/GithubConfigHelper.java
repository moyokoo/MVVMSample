package net.android.anko.helper;


/**
 * Created by miaoyongjun on 18.04.17.
 */

public class GithubConfigHelper {
    private static final String REDIRECT_URL = "https://ankko.github.io/";

    public static String getRedirectUrl() {
        return REDIRECT_URL;
    }

    public static String getClientId() {
        return "5804b047191240849afd";
    }

    public static String getSecret() {
        return "2833033183b22bb886a0da142a5b6c222dda6720";
    }
}