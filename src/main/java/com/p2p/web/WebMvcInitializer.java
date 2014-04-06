package com.p2p.web;

import com.p2p.config.WebMvcConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * p2pdeep spring container initializer
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-4-6
 */
public class WebMvcInitializer implements WebApplicationInitializer{
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        WebApplicationContext context = getContext();
        initListener(servletContext, context);
        initServlet(servletContext, context);
    }

    private void initListener(ServletContext servletContext, WebApplicationContext context) {
        servletContext.addListener(new ContextLoaderListener(context));
    }

    private void initServlet(ServletContext servletContext, WebApplicationContext context) {
        ServletRegistration.Dynamic p2pServlet = servletContext.addServlet("p2pServlet", new DispatcherServlet(context));
        p2pServlet.setLoadOnStartup(1);
        p2pServlet.addMapping("/*");
    }

    /*private AbstractRefreshableWebApplicationContext getContext() {
        XmlWebApplicationContext appContext = new XmlWebApplicationContext();
        appContext.setConfigLocation("classpath:spring/dispatcher-config.xml");
        return appContext;
    }*/
    private AbstractRefreshableWebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
//        context.setConfigLocation("com.p2p.config");
        context.register(WebMvcConfig.class);
        return context;
    }
}
