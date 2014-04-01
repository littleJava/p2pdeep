package com.p2p.spider.fazhan;

import com.p2p.spider.net.CustomSite;
import com.p2p.spider.fazhan.module.Invest;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.selector.Html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.*;

/**
 * 定制一个PageProcessor即可实现自己的爬虫逻辑。抓取fazhan标detail页面
 * User: haibo.lhb
 * Date: 14-3-22
 */
@Deprecated
public class FazhanDetailGetter  {
    public void getContent() throws IOException {
//        CloseableHttpClient httpclient = HttpClients.createDefault();

        CookieStore cookieStore = new BasicCookieStore();
        if (site.getCookies() != null) {
            for (Map.Entry<String, String> cookieEntry : site.getCookies().entrySet()) {
                BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
//                cookie.setDomain(site.getDomain());//错误，服务器无法解析cookie
//                cookie.setDomain("fazhan.com");正确
                cookie.setDomain(".fazhan.com");
                cookieStore.addCookie(cookie);
            }
        }
        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
        HttpGet httpGet = new HttpGet("http://www.fazhan.com/tenders/a20140300100.html");
        CloseableHttpResponse response1 = httpclient.execute(httpGet);

        try {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
//            EntityUtils.consume(entity1);
            InputStreamReader reader = new InputStreamReader(entity1.getContent(), "gbk");
            BufferedReader lineReader = new BufferedReader(reader);
            String line = null;
            while ((line = lineReader.readLine()) != null) {
                System.out.println(line);
            }
        } finally {
            response1.close();
        }
    }

    private Site site;

    public FazhanDetailGetter() {
        /*site = Site.me().setDomain("my.oschina.net")
                .addStartUrl("http://my.oschina.net/flashsword/blog");*/

        site = CustomSite.me().setDomain("www.fazhan.com")
                .addHeader("Host","fazhan.com")
                .addHeader("Cache-Control", "0")
                .addCookie("CNZZDATA1000012626", "826743153-1394509633-%7C1394509633")
                .addCookie("dy_cookie_time", "604800")
                .addCookie("PHPSESSID", "iep68jml51rf5dvb646gcebb33")
                .addCookie("0f6c2a6bf71d5982091346175e19d8c7", "06fb1199FTRwe04ukEj45lt8BEn4MoBLIbHaF7ty5rGL0bu9SGaRISQ4eSo")
        ;
    }

    public void process(Page page) {
        /*List<String> links = page.getHtml().links().regex("http://my\\.oschina\\.net/flashsword/blog/\\d+").all();
        page.addTargetRequests(links);*/

        /*page.putField("titles", page.getHtml().xpath("//div[@class='roundleft']/div[@class='roundtitle']/div[@class='hang  bluecolor fontsize16']").toString());
        page.putField("content", page.getHtml().$("div.content").toString());
        page.putField("tags", page.getHtml().xpath("//div[@class='BlogTags']/a/text()").all());*/

//        Html titleHtml = (Html) page.getHtml().xpath("//div[@class='roundleft']/div[@class='roundtitle']/div[@class='hang  bluecolor fontsize16']/a[@title]");

        Html tenderHtml = (Html) page.getHtml().xpath("//div[@class='mycontentleft']/div[@class='rounddiv']/div[@class='hangdeshed']/div[@class='roundcenter']/div[@class='hang']/text()");
        List<String> detailList = tenderHtml.all();
        String detailUrl = page.getUrl().toString();

        Invest invest = new Invest();
        invest.setInvestId(substringBetween(detailUrl, "/a", ".html"));
        invest.setUrl(detailUrl);
        invest.setDesc(trim(substringAfter(detailList.get(5), "：")));
        invest.setMinInvest(trim(substringAfter(detailList.get(6), "：")));
        invest.setMaxInvest(trim(substringAfter(detailList.get(7), "：")));

        Html tableHtml = (Html) page.getHtml().$("div.hang2:nth-child(4) > table:nth-child(1)");
        page.putField("invest", invest);
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws Exception {
        /*Spider.create(new OschinaBlogPageProcesser())
                .addPipeline(new ConsolePipeline()).run();*/

       /* FazhanXmlPipeline xmlPipeline = new FazhanXmlPipeline("d:/tmp/fazhan.xml", FileMode.UPDATE);
        Spider.create(new FazhanDetailGetter()).addUrl("http://fazhan.com/tenders/a20140300100.html")
                .addPipeline(new ConsolePipeline()).addPipeline(xmlPipeline)
                .run();*/
        FazhanDetailGetter getter = new FazhanDetailGetter();
        getter.getContent();


    }
}
