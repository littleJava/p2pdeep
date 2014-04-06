package com.p2p.web.controller;

import com.p2p.spider.fazhan.SpiderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.xml.ws.Service;

/**
 * TODO: sth about the class
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-4-6
 */
@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Resource
    SpiderService spiderService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String showIndex() {
        try {
            spiderService.grab("http://www.fazhan.com/tenders/index");
        } catch (Exception e) {
            logger.error("grab error",e);
            return e.getMessage();
        }
        return "Hello world";
    }

}
