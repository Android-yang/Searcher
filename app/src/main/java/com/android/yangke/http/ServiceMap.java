package com.android.yangke.http;

import com.android.yangke.base.BaseResponse;
import com.android.yangke.vo.AppVersionVo;

public enum ServiceMap implements Enums.IType {
    // common
    EMPTY("", BaseResponse.class)
    //检查 APP 版本
    ,CHECK_APP_VERSION("check_app_version",AppVersionVo.class)

    ;
    private final String mType;
    private final Class<? extends BaseResponse> mClazz;


    ServiceMap(String type, Class<? extends BaseResponse> clazz) {
        this.mType = type;
        this.mClazz = clazz;
    }

    /**
     * 创建接口对应的resp的Result的对象
     *
     * @return AbsResult或其子类的对象
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseResponse> T newResult() throws IllegalAccessException, InstantiationException {
        return (T) getClazz().newInstance();
    }

    @Override
    public String getDesc() {
        return mType;
    }

    public Class<? extends BaseResponse> getClazz() {
        return mClazz;
    }

    @Override
    public int getCode() {
        return -1;
    }
}
