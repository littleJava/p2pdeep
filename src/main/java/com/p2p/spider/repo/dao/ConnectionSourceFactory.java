package com.p2p.spider.repo.dao;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * get database db {@code ConnectionSource}
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-3-24
 */
public class ConnectionSourceFactory {
    public static final String dbUrl = "jdbc:h2:tcp://10.235.166.113:9092/p2pdata;MODE=MySQL;AUTO_RECONNECT=TRUE;AUTO_SERVER=TRUE";
    private static volatile ConnectionSource source = null;
    public static String getConnectionInfo() {
        return dbUrl;
    }
    public static ConnectionSource getConnectionSource() throws SQLException {
        if (source == null) {
            synchronized (ConnectionSource.class) {
                if (source == null) {
                    source = new JdbcPooledConnectionSource(
                            dbUrl,
                            "p2p", "p2p"
                    );
                    JdbcPooledConnectionSource pooledSource = (JdbcPooledConnectionSource) source;
                    pooledSource.setMaxConnectionAgeMillis(60 * 1000);
                    pooledSource.setTestBeforeGet(true);
                    pooledSource.setCheckConnectionsEveryMillis(10 * 1000);
                }
            }
        }
        return source;
    }
}
