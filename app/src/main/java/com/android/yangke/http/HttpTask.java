
package com.android.yangke.http;

import com.android.yangke.base.BaseApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

abstract public class HttpTask extends NetworkTask {

    protected HttpTask(BaseParam param, ServiceMap serviceMap, String message, Serializable ext,
                       int[] features) {
        super(param, serviceMap, message, ext, features);
    }

    private static final long serialVersionUID = -6458626667901622265L;

    protected String doRequest() {
        String result = "";
        if (APKConfig.isTailgateOn) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                        MessageWarehouse.getResponseContent(
                                BaseApplication.instance(), ""/*Global.getUserMobile()*/, key.name()), "UTF-8"));
                String str = "";
                StringBuilder sb = new StringBuilder();
                while ((str = bufferedReader.readLine()) != null) {
                    sb.append(str);
                }
                bufferedReader.close();
                result = sb.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        try {

            HttpURLConnection conn = makeConnection(param);
            setSecurityConfigIfNeed(conn);
            // 判断是否会重定向
            int status = conn.getResponseCode();
            boolean redirect = false;
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
            }
            // 如果需要重定向，手动处理
            if (redirect) {
                String newUrl = conn.getHeaderField("Location");
                HttpURLConnection newConn = (HttpURLConnection) new URL(newUrl).openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(newConn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null)
                    sb.append(line);

                result = sb.toString();
                return result;
            }
//            CookieUtils.saveCookies(conn);
            result = getResponseString(conn);
//            保存接口返回内容到本地文件
//            if (BuildConfig.DEBUG) {
//                MessageWarehouse.writeResponseContent(
//                        ""/*Global.getUserMobile()*/,
//                        key.name(), new ByteArrayInputStream(result.getBytes("UTF-8")));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    protected abstract HttpURLConnection makeConnection(BaseParam param) throws IOException;

}
