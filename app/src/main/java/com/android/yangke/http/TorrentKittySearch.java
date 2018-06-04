package com.android.yangke.http;

import com.android.yangke.vo.MagnetVo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
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

    private static int TIME_OUT = 20000;
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:47.0) Gecko/20100101 Firefox/47.0";

    @Override
    public String baseUrl() {
        return HttpPar.BASE_URL_TORRENT_KITTY;
    }

    /**
     * @param key 关键字
     * @param page 当前请求页数
     * @return 服务器请求回来的数据
     */
    public List<MagnetVo> getRequestResult(String key, int page) {
        if (page < 0) {
            return new ArrayList<>();
        }
        try {
            ArrayList<MagnetVo> data = getIndexResultFromPageNum(key, page);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * @param key  关键字
     * @param page 页数
     * @return 获取某一页的数据
     */
    public ArrayList<MagnetVo> getIndexResultFromPageNum(String key, int page) throws IOException {
        String pageUrl = baseUrl() + URLEncoder.encode(key, "UTF-8") + "/" + page;
        Document document = getDocument(pageUrl);

        Elements table = document.select("table#archiveResult");
//            RxLogUtils.d("table--->" + table);
        Elements urls = table.select("a[href^=magnet]");
//            RxLogUtils.d("urls--->" + urls);

        String size = "size\">", date = "date\">", endCase = "</td>";
        //一页的结果数据，约20条
        ArrayList<MagnetVo> onePagerResultList = new ArrayList<>();
        for (Element url : urls) {
            String title = url.attr("title");
            String magnet = url.attr("href");
            Node urlParentNode = url.parentNode();
            /**
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

            MagnetVo vo = new MagnetVo(title, magnet, "", resourceDate, resourceSize);
            onePagerResultList.add(vo);
//                RxLogUtils.d("data size---" + onePagerResultList.size());
        }
        return onePagerResultList;
    }

    /**
     * 通过 url 获取 Document 对象
     *
     * @param pageUrl url
     * @return document 对象
     * @throws IOException IOException
     */
    public static Document getDocument(String pageUrl) throws IOException {
        return Jsoup.connect(pageUrl)
                .userAgent(USER_AGENT)
                .timeout(TIME_OUT)
                .get();
    }

}
