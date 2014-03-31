package com.p2p.spider.fazhan;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.p2p.spider.CustomSite;
import com.p2p.spider.common.EnvConstant;
import com.p2p.spider.common.Platform;
import com.p2p.spider.fazhan.module.Invest;
import com.p2p.spider.fazhan.module.InvestSet;
import com.p2p.spider.fazhan.pipeline.DbListPagePipeline;
import com.p2p.spider.fazhan.pipeline.FazhanXmlPipeline;
import com.p2p.spider.utils.RandomUtil;
import com.sun.istack.internal.Nullable;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang.StringUtils.*;

/**
 * 定制一个PageProcessor即可实现自己的爬虫逻辑。抓取fazhan标list页面
 * User: haibo.lhb
 * Date: 14-3-22
 */
public class FazhanListPageProcessor implements PageProcessor {

    private Site site ;

    public FazhanListPageProcessor() {
        /*site = Site.me().setDomain("my.oschina.net")
                .addStartUrl("http://my.oschina.net/flashsword/blog");*/
        site = CustomSite.me().setDomain("fazhan.com");
    }

    @Override
    public void process(Page page) {
        /*List<String> links = page.getHtml().links().regex("http://my\\.oschina\\.net/flashsword/blog/\\d+").all();
        page.addTargetRequests(links);*/

        /*page.putField("titles", page.getHtml().xpath("//div[@class='roundleft']/div[@class='roundtitle']/div[@class='hang  bluecolor fontsize16']").toString());
        page.putField("content", page.getHtml().$("div.content").toString());
        page.putField("tags", page.getHtml().xpath("//div[@class='BlogTags']/a/text()").all());*/

//        Html titleHtml = (Html) page.getHtml().xpath("//div[@class='roundleft']/div[@class='roundtitle']/div[@class='hang  bluecolor fontsize16']/a[@title]");

        String progress = trim(page.getHtml().$("div.rounddiv:nth-child(2) > div:nth-child(1) > div:nth-child(3) > div:nth-child(2) > div:nth-child(3)").xpath("div/text()").toString());
        if (!progress.contains("100")) {
            return;
        }
        Html pageTenderHtml = (Html) page.getHtml().xpath("//div[@class='mycontentleft']/div[@class='rounddiv']");
        List<String> tenderHtmlList = pageTenderHtml.all();
        if (tenderHtmlList.size() > 0) {//移除搜索div
            tenderHtmlList.remove(0);
        }
        List<Invest> investList = Lists.transform(tenderHtmlList, new Function<String, Invest>() {
            @Override
            public Invest apply(@Nullable java.lang.String htmlText) {
                Html tenderHtml = new Html(htmlText);
//                String titleHrefXpath = "div[@class='roundleft']/div[@class='roundtitle']/div[@class='hang  bluecolor fontsize16']/a";
                String titleHrefXpath = "div[@class='rounddiv']/div[@class='roundleft']/div[@class='roundtitle']//a";
                String title = tenderHtml.xpath(titleHrefXpath).$("a", "title").toString();
                String detailUrl = tenderHtml.xpath(titleHrefXpath).$("a", "href").toString();
                String rewardXpath = "div[@class='rounddiv']/div[@class='roundleft']/div[@class='hang']/text()";
                String rewardText = tenderHtml.xpath(rewardXpath).all().get(0).toString();//年利率：21.6%   投标奖：1.40%
                String typeHrefSelector = "div[@class='rounddiv']/div[@class='roundleft']/div[@class='roundtitle']/div[@class='hang  bluecolor fontsize16']/a/img";
                String type = tenderHtml.xpath(typeHrefSelector).$("img", "title").toString();//<img align="absmiddle" title="信用标" src="/themes/blue/yunp2p/borrow_type_credit.gif"></img>

                String detailSelector = "div[@class='rounddiv']/div[@class='roundright']/div[@class='hangdeshed']/text()";
                List<String> detailList = tenderHtml.xpath(detailSelector).all();//[ 借款金额：￥200000.00 ,  借款期限：1个月 ,  信用等级：  ,  还款方式：到期还本还息 ]
                Invest invest = new Invest();
                invest.setInvestId(substringBetween(detailUrl, "/a", ".html"));
                invest.setUrl(detailUrl);
                invest.setTitle(title);

                invest.setDueTime(substringAfter(detailList.get(1), "："));
                invest.setPaymentType(substringAfter(detailList.get(3), "："));
                invest.setRate(substringBetween(rewardText, "：", "%") + "%");

                Double total = Double.valueOf(trim(substringAfter(detailList.get(0), "：￥"))) * 100;
                invest.setTotal(total.intValue());
                /*
                double rate = Double.valueOf(substringBetween(rewardText, "：", "%"));
                Double interest = total*rate/12;
                invest.setTotalInterest(interest.intValue());
                */
                invest.setReward(trim(substringAfterLast(rewardText, "：")));
                invest.setType(type);
                invest.setStep(EnvConstant.STEP_FAZHAN_INIT);
                invest.setPlatform(Platform.FaZhan.getCode());
                return invest;
            }
        });
        InvestSet investSet = new InvestSet();
        investSet.setInvests(investList);
        page.putField("investSet", investSet);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws Exception {
        /*Spider.create(new OschinaBlogPageProcesser())
                .addPipeline(new ConsolePipeline()).run();*/

//        FazhanXmlPipeline xmlPipeline = new FazhanXmlPipeline("d:/tmp/fazhan.xml");
        String urlPrefix = "http://www.fazhan.com/tenders/index";
        for (int i = 0; i < 30; i++) {
            String url;
            if (i == 0) {
                url = urlPrefix + ".html";
            } else {
                url = urlPrefix +i+ ".html";
            }
            DbListPagePipeline dbListPagePipeline = new DbListPagePipeline();
            Spider.create(new FazhanListPageProcessor()).addUrl(url)
                    .addPipeline(new ConsolePipeline()).addPipeline(dbListPagePipeline)/*.addPipeline(xmlPipeline)*/
                    .run();
            TimeUnit.MILLISECONDS.sleep(RandomUtil.getInt(1000, 3000));
            FazhanDetailPageProcessor.main(null);
            TimeUnit.MILLISECONDS.sleep(RandomUtil.getInt(1000, 5000));
        }
    }
}
