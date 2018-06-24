package com.android.yangke.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPostTask extends HttpTask {
    protected HttpPostTask(BaseParam param, ServiceMap serviceMap, String message, Serializable ext, int[] features) {
        super(param, serviceMap, message, ext, features);
    }

    @Override
    protected HttpURLConnection makeConnection(BaseParam param) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(param.getUrl()).openConnection();
        if (!isSlowAPI()) {
            conn.setConnectTimeout(HttpPar.TIMEOUT_MILLIS);
            conn.setReadTimeout(HttpPar.TIMEOUT_MILLIS);
        } else {
            conn.setConnectTimeout(HttpPar.LONG_TIMEOUT_MILLIS);
            conn.setReadTimeout(HttpPar.LONG_TIMEOUT_MILLIS);
        }
        conn.setRequestMethod(param.getRequestMethod());
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        String entity = map2Entity(param.toGetParamMap());
        conn.setRequestProperty("Content-Length", String.valueOf(entity.getBytes().length));

        conn.setDoOutput(true);
        conn.setDoInput(true);

        return conn;
    }
}
