package com.p2p.spider.net;

import com.google.common.collect.Lists;
import com.p2p.spider.utils.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * generate the http proxy for httpcomponent
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-3-27
 */
public class HttpProxyFactory {
    private static final Logger logger = LoggerFactory.getLogger(HttpProxyFactory.class);
    public static final String[] proxies = new String[]{
            "221.130.29.184:8888@HTTP;江苏省南京市 移动",
//            "123.129.240.176:8081@HTTP;山东省 联通",
//            "183.129.198.231:80@HTTP;浙江省杭州市 电信",
//            "111.1.36.140:80@HTTP;浙江省温州市 移动",
//            "122.228.156.126:80@HTTP;浙江省温州市 电信",
//            "122.224.194.234:80@HTTP;浙江省杭州市 电信"
    };
    private static List<HttpHost> httpProxies = null;
    public static HttpHost getProxy() {
        String proxy = proxies[RandomUtil.getInt(0, proxies.length)];
        try {
            HttpHost httpPost = parseHttpHost(proxy);
            return httpPost;
        } catch (Exception e) {
            logger.error("get http proxy error:"+proxy,e);
            return null;
        }
    }

    private static HttpHost parseHttpHost(String proxy) {
        String port = StringUtils.substringBetween(proxy, ":", "@");
        String ip = StringUtils.substringBefore(proxy, ":");
        return new HttpHost(ip, Integer.valueOf(port), "http");
    }

    public static List<HttpHost> getProxies() {
        if (httpProxies == null) {
            synchronized (proxies) {
                if (httpProxies == null) {
                    httpProxies = Lists.newArrayList();
                    for (String proxy : proxies) {
                        try {
                            httpProxies.add(parseHttpHost(proxy));
                        } catch (Exception e) {
                            logger.error("get http proxy error:" + proxy, e);
                        }
                    }
                }
            }
        }
        return httpProxies;
    }
}
