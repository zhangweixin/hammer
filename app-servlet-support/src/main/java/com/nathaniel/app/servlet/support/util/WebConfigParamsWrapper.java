package com.nathaniel.app.servlet.support.util;

/**
 * {@link javax.servlet.ServletContext} or {@link javax.servlet.ServletConfig}
 * 包装类,为获取web配置参数提供统一抽象接口
 * 
 * @author nathaniel 2018-02-22
 */
public interface WebConfigParamsWrapper {
    /**
     * 根据参数名字获取web.xml文件中的上下文配置或者servlet配置
     * 
     * @param paramName web配置参数名字
     * @return 配置值
     */
    String getConfigParam(String paramName);
}
