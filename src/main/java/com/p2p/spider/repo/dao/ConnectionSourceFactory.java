package com.p2p.spider.repo.dao;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.SQLException;

/**
 * get database db {@code ConnectionSource}
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-3-24
 */
@Component
public class ConnectionSourceFactory {
    public static final Logger logger = LoggerFactory.getLogger(ConnectionSourceFactory.class);
    private static String dbUrl = "jdbc:h2:tcp://10.235.166.113:9092/p2pdata;MODE=MySQL;AUTO_RECONNECT=TRUE;AUTO_SERVER=TRUE";
    private static String dbUsername = "p2p";
    private static String dbPassword = "p2p";
    private static volatile ConnectionSource source = null;

    @PostConstruct
    public void init() {
        logger.debug("dbUrl:{},dbUsername:{}",dbUrl,dbUsername);
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

    public static String getDbUrl() {
        return dbUrl;
    }

    @Value("#{environment['jdbc.url']}")
    public void setDbUrl(String dbUrl) {
        ConnectionSourceFactory.dbUrl = dbUrl;
    }

    public static String getDbUsername() {
        return dbUsername;
    }
    @Value("#{environment['jdbc.username']}")
    public void setDbUsername(String dbUsername) {
        ConnectionSourceFactory.dbUsername = dbUsername;
    }

    public static String getDbPassword() {
        return dbPassword;
    }
    @Value("#{environment['jdbc.password']}")
    public void setDbPassword(String dbPassword) {
        ConnectionSourceFactory.dbPassword = dbPassword;
    }
}
