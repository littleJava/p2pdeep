package com.p2p.spider.fazhan.pipeline;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.support.ConnectionSource;
import com.p2p.spider.common.EnvConstant;
import com.p2p.spider.fazhan.module.Invest;
import com.p2p.spider.fazhan.module.InvestSet;
import com.p2p.spider.fazhan.module.Investor;
import com.p2p.spider.pipeline.DbTemplatePipeline;
import com.p2p.spider.repo.dao.ConnectionSourceFactory;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * flush the data from http://www.fazhan.com/tenders/a20140300113.html to database
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-3-23
 */
public class DbDetailPagePipeline extends DbTemplatePipeline {
    private static final Logger logger = LoggerFactory.getLogger(DbDetailPagePipeline.class);
    private List<Invest> uncompleteInvests = null;

    @Override
    public void process(ResultItems resultItems, Task task, ConnectionSource connectionSource) throws SQLException {
        final Dao<Invest, Integer> investDAO = DaoManager.createDao(connectionSource, Invest.class);
        final Dao<Investor, Integer> investorDAO = DaoManager.createDao(connectionSource, Investor.class);
        Invest invest = resultItems.get("invest");
        final List<Investor> investors = resultItems.get("investors");
        final Invest completeInvest = merge(uncompleteInvests, invest, investors);
        if (completeInvest != null&&CollectionUtils.isNotEmpty(investors)) {
            TransactionManager.callInTransaction(connectionSource, new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    investDAO.update(completeInvest);
                    //每次重新插入投资人数据，删除之前的数据
                    /*Investor delInvestor = new Investor();
                    delInvestor.setInvestId(completeInvest.getInvestId());
                    delInvestor.setPlatform(completeInvest.getPlatform());
                    int totalDel = investorDAO.delete(delInvestor);*/
                    DeleteBuilder<Investor, Integer> deleteBuilder = investorDAO.deleteBuilder();
                    deleteBuilder.where().eq("platform", completeInvest.getPlatform()).and().eq("invest_id", completeInvest.getInvestId());
                    int totalDel = deleteBuilder.delete();
                    logger.info("total delete {} former data, intest:{}",totalDel,completeInvest.getInvestId());
                    int totalAdded = 0;
                    for (Investor investor : investors) {
                        totalAdded += investorDAO.create(investor);
                    }
                    return totalAdded;
                }
            });
        } else {
            logger.error(resultItems.getRequest() + "invalid invest result:" + JSON.toJSONString(invest));
        }
    }

    public List<Invest> getUncompleteInvest() throws SQLException {
        if (uncompleteInvests == null) {
            synchronized (Invest.class) {
                if (uncompleteInvests == null) {
                    ConnectionSource connectionSource = ConnectionSourceFactory.getConnectionSource();
                    Dao<Invest, Integer> investDAO = DaoManager.createDao(connectionSource, Invest.class);
                    Map<String, Object> params = Maps.newHashMap();
                    params.put("platform", "fazhan");
                    params.put("step", 0);
                    uncompleteInvests = investDAO.queryForFieldValues(params);
                    return uncompleteInvests;
                }
            }
        }
        return uncompleteInvests;
    }

    private Invest merge(List<Invest> investList, Invest invest, List<Investor> investors) {
        if (CollectionUtils.isNotEmpty(investList)) {
            for (Invest oriInvest : investList) {
                if (oriInvest.getInvestId().equals(invest.getInvestId())) {
                    oriInvest.setDesc(invest.getDesc());
                    oriInvest.setMinInvest(invest.getMinInvest());
                    oriInvest.setMaxInvest(invest.getMaxInvest());
                    oriInvest.setActiveTime(findActiveTime(investors));
                    oriInvest.setStep(EnvConstant.STEP_FAZHAN_COMPLATE);
                    return oriInvest;
                }
            }
        }
        return null;
    }

    private String findActiveTime(List<Investor> investors) {
        String maxTime = investors.get(0).getInvestTime();
        for (Investor investor : investors) {
            String investTime = investor.getInvestTime();
            if (maxTime.compareTo(investTime) < 0) {
                maxTime  = investTime;
            }
        }
        return maxTime;
    }

}
