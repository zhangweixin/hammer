package com.nathaniel.app.mvc.servlet;

import com.nathaniel.app.mvc.WebAppContextConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.Optional;

public class AppServletDispatcher extends HttpServlet implements WebAppContextConfigLoader {
    Logger                    logger = LoggerFactory.getLogger(AppServletDispatcher.class);
    private DispatcherServlet dispatcherServlet;

    @Override
    public void init(ServletConfig config) throws ServletException {
        dispatcherServlet = new DispatcherServlet(createDispatchServletContext(config));
        dispatcherServlet.init(config);
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        dispatcherServlet.service(req, res);
    }

    @Override
    public void destroy() {
        dispatcherServlet.destroy();
    }

    protected WebApplicationContext createDispatchServletContext(ServletConfig config) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        Optional.ofNullable(config.getInitParameter(MAIN_CONFIG_CLASS_PARAM)).ifPresent(mainConfigClassName -> {
            try {
                Class<?> mainConfigClass = ClassUtils.forName(mainConfigClassName, AppServletDispatcher.class.getClassLoader());
                logger.info("注册应用配置类:{}", mainConfigClass.getName());
                context.register(mainConfigClass);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        Optional.ofNullable(config.getInitParameter(CONFIG_FILE_LOCATION_PARAM)).ifPresent(configFileLocation -> {
            logger.info("注册配置文件:{}", configFileLocation);
            context.setConfigLocation(configFileLocation);
        });
        return context;
    }
}
