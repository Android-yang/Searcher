
package com.android.yangke.http;

import android.text.TextUtils;

import com.android.yangke.base.BaseResponse;
import com.android.yangke.util.GsonTools;
import com.orhanobut.logger.Logger;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

abstract public class NetworkTask implements Serializable, ITask {

    public static final class RequestFeature {

        private static final int BASE = 0x01;
        private static final int FLAG_SHOW_PROGRESS_DIALOG = BASE << 1;
        private static final int FLAG_CANCELABLE = BASE << 2;
        private static final int FLAG_RETURN_LAST_PAGE_ON_CANCEL = BASE << 3;
        private static final int FLAG_SHOW_ERROR_MESSAGE = BASE << 4;
        private static final int FLAG_SLOW_API = BASE << 5;

        /**
         * 阻塞显示网络等待对话框
         */
        public static final int BLOCK = FLAG_SHOW_PROGRESS_DIALOG | FLAG_SHOW_ERROR_MESSAGE;
        /**
         * 可以取消网络等待对话框
         */
        public static final int CANCELABLE = FLAG_CANCELABLE;
        /**
         * 显示网络异常对话框
         */
        public static final int SHOWERROR = FLAG_SHOW_ERROR_MESSAGE;
        /**
         * 取消时返回上一页面
         */
        public static final int RETURN_ON_CANCEL = FLAG_SHOW_ERROR_MESSAGE | FLAG_CANCELABLE | FLAG_RETURN_LAST_PAGE_ON_CANCEL;
        /**
         * 慢接口
         */
        public static final int SLOW_API = FLAG_SLOW_API;

    }

    private static final long serialVersionUID = 1L;

    public ServiceMap key = ServiceMap.EMPTY;
    private int mFeature;
    private String loadingMessage = "";
    public final BaseParam param;
    public BaseResponse response = BaseResponse.NULL;
    /**
     * 本地用的参数
     */
    public Serializable ext;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (ext == null ? 0 : ext.hashCode());
        result = prime * result + (mTaskWatcher == null ? 0 : mTaskWatcher.hashCode());
        result = prime * result + (this.key == null ? 0 : this.key.hashCode());
        result = prime * result + (this.param == null ? 0 : this.param.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && obj.getClass() == getClass()) {
            NetworkTask other = (NetworkTask) obj;
            if (isEquals(ext, other.ext)
                    && this.key == other.key
                    && isEquals(param, other.param)
                    && isEquals(mTaskWatcher, other.getWatcher())) {
                return true;
            }
        }
        return false;
    }

    private boolean isEquals(Object first, Object second) {
        if (first == second) {
            return true;
        }

        return first != null && second != null
                && first.getClass() == second.getClass()
                && first.equals(second);
    }

    @Override
    public String toString() {
        return String.format("NetworkParam [key=%s, param=%s]", this.key, this.param);
    }

    protected static String byte2String(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        String str = "";
        try {
            str = new String(data, 0, data.length, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return str;
    }

    public static final String map2Entity(Map<String, String> parmas) {
        Iterator<Entry<String, String>> iter = parmas.entrySet().iterator();
        StringBuilder builder = new StringBuilder();
        while (iter.hasNext()) {
            Entry<String, String> entry = iter.next();
            String key = entry.getKey();
            String val = entry.getValue();
            if (val == null) continue;
            try {
                val = TextUtils.isEmpty(val) ? val : URLEncoder.encode(entry.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            builder.append(key);
            builder.append("=");
            builder.append(val);
            builder.append("&");
        }

        return builder.toString();
    }

    protected static String getResponseString(HttpURLConnection conn) {
        String result = null;
        try {
            int responseCode = conn.getResponseCode();
            Logger.v("response", "http status code : %d", responseCode);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            if (responseCode == HttpURLConnection.HTTP_OK) {
                result = sb.toString();
            } else {
                Logger.e("response error", sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void printRequest() {
        Map<String, String> parmas = param.toGetParamMap();
        Iterator<Entry<String, String>> iter = parmas.entrySet().iterator();
        StringBuilder builder = new StringBuilder("{");
        builder.append('\n');
        while (iter.hasNext()) {
            Entry<String, String> entry = iter.next();
            String key = entry.getKey();
            String val = entry.getValue();
            if (val == null) continue;
            builder.append("  ");
            builder.append(key);
            builder.append("=");
            builder.append(val);
            builder.append('\n');
        }
        builder.append("}");
        String url = param.getUrl() + builder.toString();
        Logger.i("request", "API=" + key.name());
        Logger.i("request", url);
    }

    @Override
    public boolean doWork() {
        printRequest();
        String strResponse = doRequest();
        if (TextUtils.isEmpty(strResponse)) {
            return false;
        }
        try {
            response = GsonTools.Gson2Bean(strResponse, key.getClazz());

            printResponse(strResponse);
        } catch (Exception e) {
            response = ServerErrorResponse.INSTANCE;
            e.printStackTrace();
            Logger.e("response", strResponse);
        }
        return true;
    }

    protected abstract String doRequest();

    private void printResponse(String str) {
        Logger.i("response", "API=" + key.name());
        String[] formattedJsons = GsonTools.GsonString(str).split("\n");
        StringBuilder builder = new StringBuilder();
        for (String line : formattedJsons) {
            builder.append(line + '\n');
        }
        String s = builder.toString();
        for (int i = 0; i < s.length(); i += 4000) {
            if (i + 4000 < s.length())
                Logger.i("response" + i, s.substring(i, i + 4000));
            else
                Logger.i("response" + i, s.substring(i, s.length()));
        }
    }

    @Override
    public boolean isCancelled() {
        return mRunner.isCancelled();
    }

    @Override
    public void cancel() {
        mRunner.cancel();
    }

    protected NetworkTask(BaseParam param, ServiceMap serviceMap, String message,
                          Serializable ext, int[] features) {
        this.key = serviceMap == null ? ServiceMap.EMPTY : serviceMap;
        if (param == null) {
            throw new IllegalArgumentException("param must not be null");
        }
        this.param = param;
        if (features == null || features.length == 0) {
            features = DEFAULT_FEATURE;
        }
        for (int flag : features) {
            mFeature |= flag;
        }
        this.loadingMessage = TextUtils.isEmpty(message) ? "正在加载中……" : message;
        this.ext = ext;
    }

    public String getLoadingMessage() {
        return loadingMessage;
    }

    private ITaskWatcher mTaskWatcher;

    @Override
    public void setWatcher(ITaskWatcher watcher) {
        mTaskWatcher = watcher;
    }

    @Override
    public ITaskWatcher getWatcher() {
        return mTaskWatcher;
    }

    private ITaskRunner mRunner = ITaskRunner.EMPTY_RUNNER;

    private static final int[] DEFAULT_FEATURE = {
            RequestFeature.CANCELABLE,
    };

    @Override
    public void setRunner(ITaskRunner runner) {
        mRunner = runner;
    }

    public boolean isBlock() {
        return RequestFeature.FLAG_SHOW_PROGRESS_DIALOG == (mFeature & RequestFeature.FLAG_SHOW_PROGRESS_DIALOG);
    }

    public boolean isCancelable() {
        return RequestFeature.FLAG_CANCELABLE == (mFeature & RequestFeature.FLAG_CANCELABLE);
    }

    public boolean isReturnOnCancel() {
        return RequestFeature.FLAG_RETURN_LAST_PAGE_ON_CANCEL == (mFeature & RequestFeature.FLAG_RETURN_LAST_PAGE_ON_CANCEL);
    }

    public boolean isShowError() {
        return RequestFeature.FLAG_SHOW_ERROR_MESSAGE == (mFeature & RequestFeature.FLAG_SHOW_ERROR_MESSAGE);
    }

    public boolean isSlowAPI() {
        return RequestFeature.FLAG_SLOW_API == (mFeature & RequestFeature.FLAG_SLOW_API);
    }

    private static SSLContext sslContext;

    static {
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{
//                    new PubKeyPinningTrustManager()
            }, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置https相关配置（不同环境配置不同）
     */
    protected static void setSecurityConfigIfNeed(HttpURLConnection conn) {
        if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection safeConn = (HttpsURLConnection) conn;
            safeConn.setSSLSocketFactory(sslContext.getSocketFactory());
            safeConn.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        }
    }
}
