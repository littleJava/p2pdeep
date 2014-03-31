package com.p2p.spider.fazhan.module;

import com.alibaba.fastjson.JSON;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * investor info
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-3-23
 */
@Root
@DatabaseTable(tableName = "investor")
public class Investor {
    @DatabaseField(id=true)
    private Integer id;
    @Element
    @DatabaseField(columnName = "nick_name")
    private String nickName;
    @Element
    @DatabaseField(columnName = "per_invest")
    private Integer perInvest;//每次投资金额，单位（分）
    @Element
    @DatabaseField(columnName = "invest_time")
    private String investTime;
    @DatabaseField(columnName = "url")
    @Element(required = false)
    private String url;//用户的个人页面
    @DatabaseField(columnName = "invest_id")
    private String investId;
    @DatabaseField
    private String platform;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getPerInvest() {
        return perInvest;
    }

    public void setPerInvest(int perInvest) {
        this.perInvest = perInvest;
    }

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getInvestId() {
        return investId;
    }

    public void setInvestId(String investId) {
        this.investId = investId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
