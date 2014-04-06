package com.p2p.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * code configuration instead of the p2p-servlet.xml/applicationContext.xml
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-4-6
 */
@EnableWebMvc
@Configuration
@ComponentScan("com.p2p")
public class WebMvcConfig extends WebMvcConfigurerAdapter {
}
