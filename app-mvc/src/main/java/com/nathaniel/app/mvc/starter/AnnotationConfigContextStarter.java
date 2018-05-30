package com.nathaniel.app.mvc.starter;

import com.google.common.collect.Lists;
import com.nathaniel.app.mvc.WebAppContextConfigLoader;
import com.nathaniel.app.mvc.util.FilterWrapper;
import com.nathaniel.app.mvc.util.ListenerWrapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebListener;
import java.util.List;

@Component
public class AnnotationConfigContextStarter extends AbstractAnnotationConfigDispatcherServletInitializer implements WebAppContextConfigLoader, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private ServletContext servletContext;

    private AnnotationConfigWebApplicationContext webApplicationContext;

    private List<FilterWrapper> filterWrappers = Lists.newArrayList();
    private List<ListenerWrapper> listenerWrappers = Lists.newArrayList();

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
        registerListener();
        registerFilter();
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


    private void registerFilter() {
        filterWrappers.forEach(filterWrapper -> filterWrapper.registerFilter(servletContext));
    }

    private void registerListener() {
        listenerWrappers.forEach(listenerWrapper -> listenerWrapper.registerListener(servletContext));
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        String[] beanNamesForWebFilter = webApplicationContext.getBeanNamesForAnnotation(WebFilter.class);
        String[] beanNamesForWebListener = webApplicationContext.getBeanNamesForAnnotation(WebListener.class);

        if (!ObjectUtils.isEmpty(beanNamesForWebFilter)) {
            List<String> filterNames = Lists.newArrayList(beanNamesForWebFilter);
            filterNames.forEach(filterName -> filterWrappers.add(parseFilterInfo(webApplicationContext.getBean(filterName))));
        }

        if (!ObjectUtils.isEmpty(beanNamesForWebListener)) {
            List<String> listenerNames = Lists.newArrayList(beanNamesForWebListener);
            listenerNames.forEach(listenerName -> listenerWrappers.add(parseListenerInfo(webApplicationContext.getBean(listenerName))));
        }

    }


    private FilterWrapper parseFilterInfo(Object o) {
        if (o instanceof Filter) {
            Filter filter = (Filter) o;
            Class<? extends Filter> filterClass = filter.getClass();
            WebFilter annotation = AnnotationUtils.findAnnotation(filterClass, WebFilter.class);
            AnnotationAttributes attributes = AnnotationUtils.getAnnotationAttributes(annotation, true, true);
            Order orderAnnotation = AnnotationUtils.findAnnotation(filterClass, Order.class);
            if (orderAnnotation != null) {
                attributes.put("order", AnnotationUtils.getValue(orderAnnotation));
            }
            return new FilterWrapper(filter, attributes);
        } else {
            throw new RuntimeException("");
        }
    }

    private ListenerWrapper parseListenerInfo(Object o) {
        if (o instanceof ServletContextListener) {
            ServletContextListener listener = (ServletContextListener) o;
            WebListener annotation = AnnotationUtils.findAnnotation(listener.getClass(), WebListener.class);
            AnnotationAttributes attributes = AnnotationUtils.getAnnotationAttributes(annotation, true, true);
            return new ListenerWrapper(listener, attributes);
        } else {
            throw new RuntimeException("");
        }
    }
    
}
