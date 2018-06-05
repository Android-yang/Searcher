package com.android.yangke.vo;

import com.android.yangke.base.BaseResponse;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author: yangke on 6/5/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 搜索历史
 */
@Entity
public class SearchHistoryBeen extends BaseResponse {
    @Id(autoincrement = true)
    private Long id;

    //搜索关键字
    private String keyword;

    //存入时间
    private long time;

    @Generated(hash = 321498034)
    public SearchHistoryBeen(Long id, String keyword, long time) {
        this.id = id;
        this.keyword = keyword;
        this.time = time;
    }

    @Generated(hash = 181149912)
    public SearchHistoryBeen() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
