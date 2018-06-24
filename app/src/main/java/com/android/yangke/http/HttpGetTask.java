package com.android.yangke.http;

import com.android.yangke.util.Constant;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 处理get请求
 */
public class HttpGetTask extends HttpTask {

    protected HttpGetTask(BaseParam param, ServiceMap serviceMap, String message, Serializable ext, int[] features) {
        super(param, serviceMap, message, ext, features);
    }

    @Override
    protected HttpURLConnection makeConnection(BaseParam param) throws IOException {
        String baseUrl = param.getUrl();
        String urlWithParams = baseUrl + "?" + map2Entity(param.toGetParamMap());
        HttpURLConnection conn = (HttpURLConnection) new URL(urlWithParams).openConnection();
        if(!isSlowAPI()){
            conn.setConnectTimeout(HttpPar.TIMEOUT_MILLIS);
            conn.setReadTimeout(HttpPar.TIMEOUT_MILLIS);
        }else {
            conn.setConnectTimeout(HttpPar.LONG_TIMEOUT_MILLIS);
            conn.setReadTimeout(HttpPar.LONG_TIMEOUT_MILLIS);
        }
        conn.setRequestMethod(param.getRequestMethod());
        conn.setDoInput(true);

        if (!param.shouldNotAddCookie()) {
//            CookieUtils.addCookieProperty(conn);
        }
        return conn;
    }
}
