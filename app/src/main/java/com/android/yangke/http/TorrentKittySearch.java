package com.android.yangke.http;

import com.android.yangke.vo.MagnetVo;
import com.vondear.rxtools.RxLogUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * author: yangke on 5/30/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : TorrentKitty search data
 */
public class TorrentKittySearch implements MagnetRequest {

    private static int TIME_OUT = 10000;
    private int mMaxPagerNum = -1;
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:47.0) Gecko/20100101 Firefox/47.0";

    @Override
    public String baseUrl() {
        return HttpPar.BASE_URL_TORRENT_KITTY;
    }

    @Override
    public List<MagnetVo> searchResult(String key) {
        return null;
    }

    public int getPageNum(String key) {
        int numMax = 0;
        try {
            String pageUrl = baseUrl() + URLEncoder.encode(key, "UTF-8");
            RxLogUtils.d(pageUrl);
            Document document = Jsoup.connect(pageUrl)
                    .userAgent(USER_AGENT)
                    .timeout(TIME_OUT)
                    .get();

            Elements pageLinks = document.select("div.pagination a");

            for (Element element : pageLinks) {
                String numStr = element.attr("href");
                int temp = Integer.parseInt(numStr);
                if (temp > numMax) {
                    numMax = temp;
                }
                RxLogUtils.d("max num is --->" + numMax);
                this.mMaxPagerNum = numMax;
                return numMax;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public List<MagnetVo> getResult(String key) {
        ArrayList<MagnetVo> mResultList = new ArrayList<>();
        ArrayList<MagnetVo> mTempList = null;

        int num = getPageNum(key);
        if (num < 0) {
            return null;
        }

        if (num == 0) {
            mTempList = getResultFromPageNum(key, 1);
            if (mTempList != null) {
                for (MagnetVo vo : mTempList) {
                    mResultList.add(vo);
                }
            }
            return mResultList;
        }

        int pageNum = 5;
        if (num == pageNum) {
            for (int i = 1; i <= num; i++) {
                mTempList = getResultFromPageNum(key, i);
                if (mTempList != null) {
                    for (MagnetVo vo : mTempList) {
                        mResultList.add(vo);
                    }
                }
            }
        } else {
            for (int i = 1; i < pageNum; i++) {
                mTempList = getResultFromPageNum(key, i);
                for (MagnetVo vo : mTempList) {
                    mResultList.add(vo);
                }
            }
        }
        return mResultList;
    }

    public ArrayList<MagnetVo> getResultFromPageNum(String key, int page) {
        try {
            String pageUrl = baseUrl() + URLEncoder.encode(key, "UTF-8") + "/" + page;
            Document document = Jsoup.connect(pageUrl)
                    .userAgent(USER_AGENT)
                    .timeout(TIME_OUT)
                    .get();
            String titles = document.title();
            RxLogUtils.d("titles--->" + titles);
            Elements table = document.select("table#archiveResult");
            Elements urls = table.select("a[href^=magnet]");
            if (urls.isEmpty() == true) {
                RxLogUtils.d("searchResult null");
                ;
            }

            ArrayList<MagnetVo> mResultList = new ArrayList<>();
            for (Element url : urls) {
                String title = url.attr("title");
                String magnet = url.attr("href");
                String thunder = url.select("div.item-bar")
                        .select("a.download[href^=thunder]")
                        .attr("href");
                MagnetVo vo = new MagnetVo(title, magnet, thunder);
                mResultList.add(vo);
            }
            return mResultList;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
