package net.android.anko.utils.douyin;

import net.android.anko.base.constant.AnkoConstant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DYDecode {

    public void ttt() {
        String url = "#在抖音，记录美好生活#这可能是@小甜芯最想让你看到的一条抖音了！http://v.douyin.com/7N577/";// 解析的网址，长短链接都可以
        String playAddr = getURI(NewUrlDecode(urlAnalysisMethod(url))).trim();// 有空白符
        String cover = getCover(urlAnalysisMethod(url)).trim();// 有空白符
    }


    public static String urlAnalysisMethod(String url) {
        try {
            if (isContainChinese(url)) {
                url = cuthttpschinese(url);
            }
            if (url.length() < 40) {
                url = getURI(url);
            }
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String NewUrlDecode(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).timeout(5000).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println(doc.data());
        Elements elem = doc.getElementsByTag("script");
        String url1 = elem.toString();
        int start = url1.indexOf("playAddr");
        url1 = url1.substring(start);
        int end = url1.indexOf("\",");
        return url1.substring(11, end).replaceAll("playwm", "play");
    }

    // 获取封面，项目要用到
    public static String getCover(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).timeout(5000).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Elements elem = doc.getElementsByTag("script");
        String url1 = elem.toString();
        int start = url1.indexOf("cover");
        String str = url1.substring(start);
        int end = str.indexOf("}");
        return str.substring(8, end).replaceAll("\"", "");
    }

    /**
     * @param url
     * @return
     */
    // 获取真实地址
    public static String getURI(String url) {

        Retrofit.Builder builder = new Retrofit.Builder();
        OkHttpClient.Builder builder2 = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = builder.baseUrl(AnkoConstant.GANK_BASE_URL)//域名
                .client(builder2.build()).build();
        IDou iDou = retrofit.create(IDou.class);
        Call<ResponseBody> responseBodyCall = iDou.douyin(url);
        Response response = null;
        try {
            response = responseBodyCall.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.raw().request().url().uri().toString();
    }

    /**
     * @param str
     * @return
     */
    // 检查是否有中文
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * @param str
     * @return
     */
    // 截取到http开始的字段
    public static String cuthttpschinese(String str) {
        int start = str.indexOf("http");
        return str.substring(start);
    }
}
