

package net.android.anko.model.model;

import com.google.gson.annotations.SerializedName;

import net.android.anko.BuildConfig;
import net.android.anko.helper.GithubConfigHelper;

import java.util.Arrays;
import java.util.List;


public class AuthRequestModel {

    private List<String> scopes;
    private String note;
    private String noteUrl;
    @SerializedName("client_id")
    private String clientId;
    @SerializedName("client_secret")
    private String clientSecret;

    public static AuthRequestModel generate() {
        AuthRequestModel model = new AuthRequestModel();
        model.scopes = Arrays.asList("user", "repo", "gist", "notifications", "read:org");
        model.note = BuildConfig.APPLICATION_ID;
        model.clientId = GithubConfigHelper.getClientId();
        model.clientSecret = GithubConfigHelper.getSecret();
        model.noteUrl = GithubConfigHelper.getRedirectUrl();
        return model;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public String getNote() {
        return note;
    }

    public String getNoteUrl() {
        return noteUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
