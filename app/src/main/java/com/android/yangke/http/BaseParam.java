
package com.android.yangke.http;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

abstract public class BaseParam implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Map<String, String> mParams = new HashMap<>();

    protected BaseParam() {
        mParams.putAll(HttpPar.getBasicParams());
    }

    public abstract Map<String, String> toGetParamMap();

    public String getUrl() {
        return getProtocolSchema() + getBaseUrl() + url();
    }

    /**
     * @return 协议的前缀   e.g. "http://" ,"https://"
     */
    private String getProtocolSchema() {
        return goWithHttps() ? HttpPar.HTTPS : HttpPar.HTTP;
    }

    /**
     * @return host+firstLevelPath
     */
    private String getBaseUrl() {
        return getServerType().getBaseUrl();
    }

    /**
     * @return the string representing the method to be used.
     */
    public String getRequestMethod() {
        return HttpPar.HTTP_GET;
    }

    abstract protected String url();

    public boolean goWithHttps() {
        return APKConfig.isOpenHttps && isRisky();
    }

    protected boolean isRisky() {
        return false;
    }

    @Override
    public int hashCode() {
        int result = 23;
        result = 31 * result + getUrl().hashCode();
        int paramsHashCode = toGetParamMap() == null ? 0 : toGetParamMap().hashCode();
        result = 31 * result + paramsHashCode;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && obj instanceof BaseParam) {
            BaseParam other = (BaseParam) obj;
            if (getUrl() != null && getUrl().equalsIgnoreCase(other.getUrl())) {
                Map<String, String> paramMap = toGetParamMap();
                if (paramMap != null && paramMap.equals(other.toGetParamMap())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否应带上cookie，登录和注册不应带cookie
     *
     * @return true if should not add cookie, such as login and register
     */
    public boolean shouldNotAddCookie() {
        return false;
    }

    public enum ServerType {
        /**
         * java server
         */
        Default {
            @Override
            public String getBaseUrl() {
                return HttpPar.COMMON_SERVER_HOST;
            }

            @Override
            public String getBaseImageUrl() {
                return HttpPar.COMMON_SERVER_HOST_IMAGE;
            }
        };

        public abstract String getBaseUrl();

        public abstract String getBaseImageUrl();
    }

    protected ServerType getServerType() {
        return ServerType.Default;
    }
}
