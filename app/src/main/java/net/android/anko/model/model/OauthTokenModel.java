package net.android.anko.model.model;

import com.google.gson.annotations.SerializedName;

public class OauthTokenModel {

    @SerializedName("access_token")
    private String accessToken;

    private String scope;

    public OauthTokenModel() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
