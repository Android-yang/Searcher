package com.android.yangke.http;

import com.android.yangke.vo.MagnetVo;
import com.vondear.rxtools.RxLogUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
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

            Elements table = document.select("table#archiveResult");
//            RxLogUtils.d("table--->" + table);

            Elements urls = table.select("a[href^=magnet]");
//            RxLogUtils.d("urls--->" + urls);

            if (urls.isEmpty() == true) {
                RxLogUtils.d("searchResult null");
            }
            String size = "size\">";
            String date = "date\">";
            String endCase = "</td>";
            ArrayList<MagnetVo> mResultList = new ArrayList<>();
            for (Element url : urls) {
                String title = url.attr("title");
                String magnet = url.attr("href");
                Node urlParentNode = url.parentNode();
                /**
                 * <tr>
                 * <td class="name">[蚂蚁仔www.mayizai.com][功夫瑜伽.Kung.Fu.Yoga][WEB-MKV-2.44GB][国语中英字幕][1080P][成龙贺岁大片]</td>
                 * <td class="size">524.29 kb</td>
                 * <td class="date">2017-03-26</td>
                 * <td class="action"><a href="/information/6E09F5EEEE6DC4EEFA3D4C0CE965A40CC5CE7F10" title="[蚂蚁仔www.mayizai.com][功夫瑜伽.Kung.Fu.Yoga][WEB-MKV-2.44GB][国语中英字幕][1080P][成龙贺岁大片]" rel="information">Detail</a><a href="magnet:?xt=urn:btih:6E09F5EEEE6DC4EEFA3D4C0CE965A40CC5CE7F10&amp;dn=%5B%E8%9A%82%E8%9A%81%E4%BB%94www.mayizai.com%5D%5B%E5%8A%9F%E5%A4%AB%E7%91%9C%E4%BC%BD.Kung.Fu.Yoga%5D%5BWEB-MKV-2.44GB%5D%5B%E5%9B%BD%E8%AF%AD%E4%B8%AD%E8%8B%B1%E5%AD%97%E5%B9%95%5D%5B1080P%5D%5B%E6%88%90%E9%BE%99%E8%B4%BA%E5%B2%81%E5%A4%A7%E7%89%87%5D&amp;tr=http%3A%2F%2Ftracker.ktxp.com%3A6868%2Fannounce&amp;tr=http%3A%2F%2Ftracker.ktxp.com%3A7070%2Fannounce&amp;tr=udp%3A%2F%2Ftracker.ktxp.com%3A6868%2Fannounce&amp;tr=udp%3A%2F%2Ftracker.ktxp.com%3A7070%2Fannounce&amp;tr=http%3A%2F%2Fbtfans.3322.org%3A8000%2Fannounce&amp;tr=http%3A%2F%2Fbtfans.3322.org%3A8080%2Fannounce&amp;tr=http%3A%2F%2Fbtfans.3322.org%3A6969%2Fannounce&amp;tr=http%3A%2F%2Ftracker.bittorrent.am%2Fannounce&amp;tr=udp%3A%2F%2Ftracker.bitcomet.net%3A8080%2Fannounce&amp;tr=http%3A%2F%2Ftk3.5qzone.net%3A8080%2F&amp;tr=http%3A%2F%2Ftracker.btzero.net%3A8080%2Fannounce&amp;tr=http%3A%2F%2Fscubt.wjl.cn%3A8080%2Fannounce&amp;tr=http%3A%2F%2Fbt.popgo.net%3A7456%2Fannounce&amp;tr=http%3A%2F%2Fthetracker.org%2Fannounce&amp;tr=http%3A%2F%2Ftracker.prq.to%2Fannounce&amp;tr=http%3A%2F%2Ftracker.publicbt.com%2Fannounce&amp;tr=http%3A%2F%2Ftracker.dmhy.org%3A8000%2Fannounce&amp;tr=http%3A%2F%2Fbt.titapark.com%3A2710%2Fannounce&amp;tr=http%3A%2F%2Ftracker.tjgame.enorth.com.cn%3A8000%2Fannounce&amp;" title="[蚂蚁仔www.mayizai.com][功夫瑜伽.Kung.Fu.Yoga][WEB-MKV-2.44GB][国语中英字幕][1080P][成龙贺岁大片]" rel="magnet">Open</a>
                 * <!--a href="//www.torrentkitty.tv/torrent/2926456/[蚂蚁仔www.mayizai.com][功夫瑜伽.Kung.Fu.Yoga][WEB-MKV-2.44GB][国语中英字幕][1080P][成龙贺岁大片]" style="display:hidden;"></a--><a href="https://av28.com/" target="_blank">Download</a></td>
                 * </tr>
                 */
                String currentResourceHtml = urlParentNode.parentNode().outerHtml();
                int startIndex = currentResourceHtml.indexOf(size) + size.length();
                int endIndex = currentResourceHtml.indexOf(endCase, startIndex);
                String resourceSize = currentResourceHtml.substring(startIndex, endIndex);

                int dateStartIndex = currentResourceHtml.indexOf(date) + date.length();
                int dateEndIndex = currentResourceHtml.indexOf(endCase, dateStartIndex);
                String resourceDate = currentResourceHtml.substring(dateStartIndex, dateEndIndex);

//                String thunder = url.select("div.item-bar")
//                        .select("a.download[href^=thunder]")
//                        .attr("href");
                MagnetVo vo = new MagnetVo(title, magnet, "", resourceDate, resourceSize);
                mResultList.add(vo);
                RxLogUtils.d("data size---" + mResultList.size());
            }
            return mResultList;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
