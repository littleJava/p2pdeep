package com.p2p.spider.fazhan;

import com.p2p.spider.fazhan.module.InvestSet;
import com.p2p.spider.fazhan.pipeline.DbListPagePipeline;
import com.p2p.spider.fazhan.pipeline.FazhanXmlPipeline;
import com.p2p.spider.utils.RandomUtil;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

import java.util.concurrent.TimeUnit;

/**
 * 抓取fazhan标list页面和detail页面
 * User: haibo.lhb
 * Date: 14-3-22
 */
@Component
public class SpiderService {

    public void grab(String url) throws Exception {
        FazhanListPageProcessor.main(null);
        TimeUnit.MILLISECONDS.sleep(RandomUtil.getInt(1000, 3000));
        FazhanDetailPageProcessor.main(null);
    }


    public static void main(String[] args) throws Exception {
        SpiderService spiderService = new SpiderService();
        String urlPrefix = "http://www.fazhan.com/tenders/index";
        for (int i = 0; i < 30; i++) {
            String url;
            if (i == 0) {
                url = urlPrefix + ".html";
            } else {
                url = urlPrefix +i+ ".html";
            }
            spiderService.grab(url);
        }
    }
}
