package com.p2p.spider.fazhan.pipeline;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.p2p.spider.fazhan.module.Invest;
import com.p2p.spider.fazhan.module.InvestSet;
import com.p2p.spider.pipeline.DbTemplatePipeline;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

import java.sql.SQLException;
import java.util.List;

/**
 * flush the data from http://www.fazhan.com/tenders/index.html to database
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-3-23
 */
public class DbListPagePipeline extends DbTemplatePipeline {
    private static final Logger logger = LoggerFactory.getLogger(DbListPagePipeline.class);

    @Override
    public void process(ResultItems resultItems, Task task, ConnectionSource connectionSource) throws SQLException {
        Dao<Invest, Integer> investDAO = DaoManager.createDao(connectionSource, Invest.class);
        InvestSet investSet = resultItems.get("investSet");
        if (investSet == null) {
            return;
        }
        for (Invest invest : investSet.getInvests()) {
            Invest existInvest = new Invest();
            existInvest.setInvestId(invest.getInvestId());
            existInvest.setPlatform(invest.getPlatform());
            List<Invest> existsList = investDAO.queryForMatching(existInvest);
            if (CollectionUtils.isEmpty(existsList)) {
                investDAO.create(invest);
            } else {
                logger.error("exists invest:{},{}",invest.getPlatform(),invest.getInvestId());
            }
        }
    }

    private void merge(InvestSet investSet, Invest invest) {
        List<Invest> investList = investSet.getInvests();
        if (investSet != null&&CollectionUtils.isNotEmpty(investList)) {
            for (Invest oriInvest : investList) {
                if (oriInvest.getInvestId().equals(invest.getInvestId())) {
                    oriInvest.setDesc(invest.getDesc());
                    oriInvest.setMinInvest(invest.getMinInvest());
                    oriInvest.setMaxInvest(invest.getMaxInvest());
                }
            }
        }
    }

}
