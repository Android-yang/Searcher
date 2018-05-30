package com.android.yangke.http;

import com.android.yangke.vo.MagnetVo;

import java.util.List;

/**
 * author: yangke on 5/30/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : TorrentKitty、BT樱桃、XXX的抽象
 */
public interface MagnetRequest {

    /**
     *
     * @return Site base url
     */
    String baseUrl();

    /***
     *
     * @param key 搜索关键字
     * @return 网站数据抓取结果
     */
    List<MagnetVo> searchResult(String key);
}
