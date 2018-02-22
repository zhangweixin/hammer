package com.nathaniel.app.mvc.starter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.nathaniel.app.mvc.WebAppContextConfigLoader;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

public class AnnotationConfigContextStarter extends AbstractAnnotationConfigDispatcherServletInitializer implements WebAppContextConfigLoader {

    private ServletContext servletContext;

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
        return new String[] { "/" };
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
}
