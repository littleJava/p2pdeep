package com.p2p.spider.net;

import com.google.common.collect.Lists;
import com.p2p.spider.utils.RandomUtil;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Site;

import java.util.List;

/**
 * cutomize the {@code Site} about the sleepTime, retryTime, userAgent
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-3-23
 */
public class CustomSite extends Site {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static Site me() {
        return new CustomSite();
    }

    @Override
    public int getSleepTime() {
        int sleepTime = RandomUtil.getInt(2000, 5000);
        logger.info("sleep time {}ms:", sleepTime);
        return sleepTime;
    }

    private List<String> userAgents = Lists.newArrayList(
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1"
            , "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/6.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.2; .NET4.0C; .NET4.0E)"
            , "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1"
            , "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3 GTBDFff GTB7.0"
            , "Mozilla/5.0 (iPad; CPU OS 7_0_4 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11B554a Safari/9537.53"
            , "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/3.5 Safari/536.11"
            , "Mozilla/5.0 (iPad; CPU OS 7_0_4 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Mobile/11B554a"
            , "Mozilla/5.0 (iPad; CPU OS 7_0_6 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/6.0 MQQBrowser/4.0.1 Mobile/11B651 Safari/7534.48.3"
            , "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; 2345Explorer 3.2.0.12012)"
    );

    private List<HttpHost> httpProxies = HttpProxyFactory.getProxies();


    public CustomSite addUserAgent(String userAgent) {
        this.userAgents.add(userAgent);
        return this;
    }

    @Override
    public String getUserAgent() {
        String userAgent;
        if (userAgents.isEmpty()) {
            userAgent = "";
        } else {
            userAgent = userAgents.get(RandomUtil.getInt(0, userAgents.size()));
        }
        logger.info("userAgent:" + userAgent);
        return userAgent;
    }

    @Override
    public Site setUserAgent(String userAgent) {
        return this.addUserAgent(userAgent);
    }

    @Override
    public Site setHttpProxy(HttpHost httpProxy) {
        return this.addHttpProxy(httpProxy);
    }

    public CustomSite addHttpProxy(HttpHost httpProxy) {
        this.httpProxies.add(httpProxy);
        return this;
    }

    @Override
    public HttpHost getHttpProxy() {
        if (httpProxies.isEmpty()) {
            return null;
        }
        HttpHost httpHost = httpProxies.get(RandomUtil.getInt(0, httpProxies.size()));
        logger.info("http proxy="+httpHost);
        return httpHost;
//        return null;
    }
}
