package com.p2p.spider.db;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.p2p.spider.fazhan.module.Invest;
import com.p2p.spider.fazhan.module.Investor;
import com.p2p.spider.repo.dao.ConnectionSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * create table
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-3-24
 */
public class TableCreator {
    private static final Logger logger = LoggerFactory.getLogger(TableCreator.class);

    public static void main(String[] args) {
        try {
            ConnectionSource connectionSource = ConnectionSourceFactory.getConnectionSource();
            int i = TableUtils.createTableIfNotExists(connectionSource, Invest.class);
            logger.info("create table result:" + i);
            List<String> sql = TableUtils.getCreateTableStatements(connectionSource, Invest.class);
            logger.info("create table sql:" + sql);

            i = TableUtils.createTableIfNotExists(connectionSource, Investor.class);
            logger.info("create table result:" + i);
            sql = TableUtils.getCreateTableStatements(connectionSource, Investor.class);
            logger.info("create table sql:" + sql);
        } catch (Exception e) {
            logger.error("create table error",e);
        }

    }

}
