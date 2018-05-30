package com.android.yangke.vo;

import com.android.yangke.base.BaseResponse;

/**
 * author: yangke on 5/30/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 磁力
 */
public class MagnetVo extends BaseResponse{
    //标题
    public String mTitle;
    //磁力
    public String mMagnet;
    //迅雷
    public String mThunder;

    public MagnetVo(String title, String magnet, String thunder) {
        this.mTitle = title;
        this.mMagnet = magnet;
        this.mThunder = thunder;
    }
}
