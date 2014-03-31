package com.p2p.spider.fazhan.module;

import com.alibaba.fastjson.JSON;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Invest detail
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-3-23
 */

@Root
@DatabaseTable(tableName = "invest")
public class Invest {
    @DatabaseField(id=true)
    private Integer id;
    @Element
    @DatabaseField(columnName = "invest_id",uniqueIndexName="invest_unique_idx")
    private String investId;
    @DatabaseField(uniqueIndexName="invest_unique_idx")
    private String platform;

    @Element
    @DatabaseField
    private String title;
    @Element
    @DatabaseField
    private String type;//标类型：信用标 担保标 抵押标 净值标 流转标 天标 秒标
    @Element
    @DatabaseField
    private String rate;//年利息
    @Element
    @DatabaseField
    private String reward;//奖励
    @Element
    @DatabaseField
    private Integer total;//总额，单位：分
    /*
    @Element
    private Integer totalInterest;//总共要付的利息
    */
    @Element(required = false)
    @DatabaseField(columnName = "min_invest")
    private String minInvest;

    @Element(required = false)
    @DatabaseField(columnName = "max_invest")
    private String maxInvest;
    @Element
    @DatabaseField(columnName = "due_time")
    private String dueTime;
    @DatabaseField(columnName = "active_time")
    private String activeTime;
    @Element
    @DatabaseField(columnName = "payment_type")
    private String paymentType;
    @DatabaseField(columnName = "invest_desc")
    @Element(required = false)
    private String desc;
    @Element
    @DatabaseField
    private String url;
    @DatabaseField
    private Integer step;//数据完整度到哪一步

    @ElementList(required = false)
    private List<Investor> investors;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }


    public String getMinInvest() {
        return minInvest;
    }

    public void setMinInvest(String minInvest) {
        this.minInvest = minInvest;
    }

    public String getMaxInvest() {
        return maxInvest;
    }

    public void setMaxInvest(String maxInvest) {
        this.maxInvest = maxInvest;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Investor> getInvestors() {
        return investors;
    }

    public void setInvestors(List<Investor> investors) {
        this.investors = investors;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
