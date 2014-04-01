package com.p2p.spider;

import com.p2p.spider.net.CustomSite;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * 定制一个PageProcessor即可实现自己的爬虫逻辑。抓取osc博客的一段代码
 * User: haibo.lhb
 * Date: 14-3-22
 */
public class OschinaBlogPageProcesser implements PageProcessor {

    private Site site ;

    public OschinaBlogPageProcesser() {
        /*site = Site.me().setDomain("my.oschina.net")
                .addStartUrl("http://my.oschina.net/flashsword/blog");*/
        site = CustomSite.me().setDomain("my.oschina.net");
    }

    @Override
    public void process(Page page) {
        List<String> links = page.getHtml().links().regex("http://my\\.oschina\\.net/flashsword/blog/\\d+").all();
        page.addTargetRequests(links);
        page.putField("title", page.getHtml().xpath("//div[@class='BlogEntity']/div[@class='BlogTitle']/h1").toString());
        page.putField("content", page.getHtml().$("div.content").toString());
        page.putField("tags",page.getHtml().xpath("//div[@class='BlogTags']/a/text()").all());
    }

    @Override
    public Site getSite() {
        return site;

    }

    public static void main(String[] args) {
        /*Spider.create(new OschinaBlogPageProcesser())
                .addPipeline(new ConsolePipeline()).run();*/
        Spider.create(new OschinaBlogPageProcesser()).addUrl("http://my.oschina.net/flashsword/blog")
                .addPipeline(new ConsolePipeline()).run();
    }
}
