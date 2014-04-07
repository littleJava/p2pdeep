package com.p2p.spider.fazhan;

import com.p2p.spider.fazhan.module.InvestSet;
import com.p2p.spider.fazhan.pipeline.DbListPagePipeline;
import com.p2p.spider.fazhan.pipeline.FazhanXmlPipeline;
import com.p2p.spider.repo.dao.ConnectionSourceFactory;
import com.p2p.spider.utils.RandomUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
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
    private static final Logger logger = LoggerFactory.getLogger(SpiderService.class);
    public void grab(String url) throws Exception {
        FazhanListPageProcessor.main(null);
        TimeUnit.MILLISECONDS.sleep(RandomUtil.getInt(1000, 3000));
        FazhanDetailPageProcessor.main(null);
    }
    @Value("#{environment['jdbc.username']}")
    private String dbUsername;

    @Scheduled(cron = "0/10 * * * * ?")
    public void doSth() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String time = fmt.print(new DateTime());
        logger.debug(ConnectionSourceFactory.getDbUrl()+"~~~"+dbUsername+"----------------"+time);
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
