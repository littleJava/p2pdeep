package com.p2p.spider.pipeline;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.p2p.spider.repo.dao.ConnectionSourceFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.sql.SQLException;

/**
 * flush the data to database
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-3-23
 */
public abstract class DbTemplatePipeline implements Pipeline {
//    private Logger logger = LoggerFactory.getLogger(getClass());

    public abstract void process(ResultItems resultItems, Task task, ConnectionSource connectionSource) throws Exception;

    @Override
    public void process(ResultItems resultItems, Task task) {
        ConnectionSource connectionSource = null;
        try {
            connectionSource = ConnectionSourceFactory.getConnectionSource();
            process(resultItems, task, connectionSource);
        } catch (Exception e) {
            throw new IllegalStateException("process data error,db url:"
                    + ConnectionSourceFactory.getConnectionInfo(), e);
        }/* finally {
            if (connectionSource != null) {
                connectionSource.closeQuietly();
            }
        }*/
    }

}