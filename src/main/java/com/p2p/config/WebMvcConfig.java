package com.p2p.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.concurrent.TimeUnit;

/**
 * code configuration instead of the p2p-servlet.xml/applicationContext.xml <br />
 * <br />
 * configuration:@PropertySource(name = "env",value = "classpath:env.properties")<br/>
 * Spring:@Autowired<br />
 * private Environment env;
 * env.getProperty("jdbc.username")<br/>
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-4-6
 */
@EnableWebMvc
@Configuration
@ComponentScan("com.p2p")
@PropertySource(value = "classpath:env.properties")
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    /**
     * <mvc:resources mapping="/resources/**" location="classpath:/,/public-resources/" cacheperiod="
     * 31556926"/>
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/", "/publicresources/")
                .setCachePeriod((int) TimeUnit.HOURS.toMillis(1L));
    }
    /*@Bean
    public InternalResourceViewResolver configureInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".vm");
        return resolver;
    }*/
}
