package com.p2p.spider.fazhan;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.p2p.spider.CustomSite;
import com.p2p.spider.common.Platform;
import com.p2p.spider.fazhan.module.Invest;
import com.p2p.spider.fazhan.module.Investor;
import com.p2p.spider.fazhan.pipeline.DbDetailPagePipeline;
import com.p2p.spider.net.HttpProxyFactory;
import com.sun.istack.internal.Nullable;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpHost;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

import static org.apache.commons.lang.StringUtils.*;

/**
 * 定制一个PageProcessor即可实现自己的爬虫逻辑。抓取fazhan标detail页面
 * User: haibo.lhb
 * Date: 14-3-22
 */
public class FazhanDetailPageProcessor implements PageProcessor {

    private Site site;

    public FazhanDetailPageProcessor() {
        /*site = Site.me().setDomain("my.oschina.net")
                .addStartUrl("http://my.oschina.net/flashsword/blog");*/

        HttpHost proxy = HttpProxyFactory.getProxy();
        site = CustomSite.me().setDomain("fazhan.com").setHttpProxy(proxy)
                .addHeader("Host", "fazhan.com")
                .addHeader("Cache-Control","0")
                .addCookie("CNZZDATA1000012626", "826743153-1394509633-%7C1394509633")
                .addCookie("dy_cookie_time", "604800")
                .addCookie("PHPSESSID", "iep68jml51rf5dvb646gcebb33")
                .addCookie("0f6c2a6bf71d5982091346175e19d8c7", "06fb1199FTRwe04ukEj45lt8BEn4MoBLIbHaF7ty5rGL0bu9SGaRISQ4eSo")
        ;
    }

    @Override
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
        invest.setPlatform(Platform.FaZhan.getCode());
//        invest.setDesc(trim(substringAfter(detailList.get(5), "：")));
        invest.setDesc(page.getHtml().$("#js_xiangqing > div:nth-child(2)").xpath("div/text()").toString());
        invest.setMinInvest(trim(substringAfter(detailList.get(6), "：")));
        invest.setMaxInvest(trim(substringAfter(detailList.get(7), "：")));

//        Html tableHtml = (Html) page.getHtml().$("div.hang2:nth-child(4) > table:nth-child(1)");
        page.putField("invest", invest);
        Html tableTrHtml = (Html) page.getHtml().$("div.hang2:nth-child(4) > table:nth-child(1) > tbody:nth-child(1) > tr");
        List<Investor> investorList = parseInvestor(tableTrHtml, invest);
        page.putField("investors", investorList);

    }

    private List<Investor> parseInvestor(Html tableTrHtml, Invest invest) {
        List<String> rows = tableTrHtml.all();
        List<Investor> investors = Lists.newLinkedList();
        if (CollectionUtils.isEmpty(rows)) {
            return investors;
        }
        rows.remove(0);//移除第一行 title
        for (String row : rows) {
            Html rowHtml = new Html(row);
            List<String> tdTexts = rowHtml.xpath("div/text()").all();
            Investor investor = new Investor();
            investor.setNickName(trim(tdTexts.get(0)));
            Double total = Double.valueOf(trim(substringAfter(tdTexts.get(1), "￥"))) * 100;
            investor.setPerInvest(total.intValue());
            investor.setInvestTime(trim(tdTexts.get(2)));
            investor.setInvestId(invest.getInvestId());
            investor.setPlatform(invest.getPlatform());
            investors.add(investor);
        }
        return investors;
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws Exception {
        /*Spider.create(new OschinaBlogPageProcesser())
                .addPipeline(new ConsolePipeline()).run();*/

        DbDetailPagePipeline detailPagePipeline = new DbDetailPagePipeline();
        List<Invest> investList = detailPagePipeline.getUncompleteInvest();
        List<String> urls = Lists.transform(investList,new Function<Invest, String>() {
            @Override
            public String apply(@Nullable com.p2p.spider.fazhan.module.Invest invest) {
                return invest.getUrl();
            }
        });
        Spider.create(new FazhanDetailPageProcessor()).addUrl(urls.toArray(new String[0]))
                .addPipeline(new ConsolePipeline()).addPipeline(detailPagePipeline)
                .run();

    }
}
