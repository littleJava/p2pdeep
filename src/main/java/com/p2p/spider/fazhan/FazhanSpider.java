package com.p2p.spider.fazhan;

import com.p2p.spider.fazhan.module.InvestSet;
import com.p2p.spider.fazhan.pipeline.FazhanXmlPipeline;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

/**
 * 抓取fazhan标list页面和detail页面
 * User: haibo.lhb
 * Date: 14-3-22
 */
@Component
public class FazhanSpider {



    public static void main(String[] args) throws Exception {
        String filePath = "d:/tmp/fazhan.xml";
        FazhanXmlPipeline xmlPipeline = new FazhanXmlPipeline(filePath);
        Spider.create(new FazhanListPageProcessor()).addUrl("http://www.fazhan.com/investSet/index.html")
                .addPipeline(new ConsolePipeline()).addPipeline(xmlPipeline)
                .run();
        InvestSet investSet = xmlPipeline.parse();


    }
}
