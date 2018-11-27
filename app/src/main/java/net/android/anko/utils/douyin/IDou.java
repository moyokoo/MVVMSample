package net.android.anko.utils.douyin;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IDou {
    @GET
    public Call<ResponseBody> douyin(@Url String url);
}
