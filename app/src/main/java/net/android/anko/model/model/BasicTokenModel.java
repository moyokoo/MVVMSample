package net.android.anko.model.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BasicTokenModel {

    private int id;
    private String url;
    private String token;
    @SerializedName("created_at") private Date createdAt;
    @SerializedName("updated_at") private Date updatedAt;
    private List<String> scopes;

    public BasicTokenModel() {

    }

    public static BasicTokenModel generateFromOauthToken(OauthTokenModel oauthToken){
        BasicTokenModel basicToken = new BasicTokenModel();
        basicToken.setToken(oauthToken.getAccessToken());
        basicToken.setScopes(Arrays.asList(oauthToken.getScope().split(",")));
        return basicToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }
}
