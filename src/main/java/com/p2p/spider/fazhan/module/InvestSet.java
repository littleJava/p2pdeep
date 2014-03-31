package com.p2p.spider.fazhan.module;

import com.alibaba.fastjson.JSON;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 *Trender list
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-3-23
 */
@Root
public class InvestSet {
    @ElementList(inline = true)
    private List<Invest> invests;

    public List<Invest> getInvests() {
        return invests;
    }

    public void setInvests(List<Invest> invests) {
        this.invests = invests;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
