package com.nathaniel.app.mvc.starter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.nathaniel.app.mvc.WebAppContextConfigLoader;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

@Component
public class AnnotationConfigContextStarter extends AbstractAnnotationConfigDispatcherServletInitializer implements WebAppContextConfigLoader, ApplicationContextAware {

    private ServletContext servletContext;

    private AnnotationConfigWebApplicationContext webApplicationContext;

    @Override
    protected Class<?>[] getRootConfigClasses() {

        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[0];
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected String getServletName() {
        return "spring-mvc-dispatcher";
    }

    /**
     * 重载{@link AbstractDispatcherServletInitializer}的onStartup方法保存servletContext
     *
     * @param servletContext
     * @throws ServletException
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        this.servletContext = servletContext;
        super.onStartup(servletContext);
    }

    /**
     * 自定义applicationContext配置
     *
     * @return WebApplicationContext {@link AnnotationConfigWebApplicationContext}
     */
    @Override
    protected WebApplicationContext createServletApplicationContext() {
        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
        registerConfigClass(paramName -> servletContext.getInitParameter(paramName), webApplicationContext);
        servletContext = null;
        return webApplicationContext;
    }


    @Override
    protected WebApplicationContext createRootApplicationContext() {
        return webApplicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof AnnotationConfigWebApplicationContext) {
            webApplicationContext = (AnnotationConfigWebApplicationContext) applicationContext;
        }
    }
}
