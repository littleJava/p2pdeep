package com.p2p.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * spring scheduling and asynchronous task
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-4-7
 */
@Configuration
@EnableAsync
@EnableScheduling
public class TaskConfig {
}
