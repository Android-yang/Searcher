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
    //日期
    public String mDate;
    //大小
    public String mSize;

    public MagnetVo(String title, String magnet, String thunder, String date, String size) {
        this.mTitle = title;
        this.mMagnet = magnet;
        this.mThunder = thunder;
        this.mDate = date;
        this.mSize = size;
    }
}
