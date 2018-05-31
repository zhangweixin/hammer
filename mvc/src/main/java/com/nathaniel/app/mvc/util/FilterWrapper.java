/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nathaniel.app.mvc.util;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ObjectUtils;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public class FilterWrapper {
    private static Logger        logger                = LoggerFactory.getLogger(FilterWrapper.class);
    private static String        INIT_PARAMS_ATTR      = "initParams";
    private static String        DISPATCHER_TYPES_ATTR = "dispatcherTypes";
    private static String        VALUE_ATTR            = "value";
    private static String        URL_PATTERNS_ATTR     = "urlPatterns";
    private static String        ASYNC_SUPPORTED_ATTR  = "asyncSupported";
    private static String        SERVLET_NAMES_ATTR    = "servletNames";
    private static String        FILTER_NAME_ATTR      = "filterName";

    private AnnotationAttributes filterAttributes;

    private Filter               filter;

    public FilterWrapper(Filter filter, AnnotationAttributes filterAttributes) {
        this.filterAttributes = filterAttributes;
        this.filter = filter;
    }

    public void registerFilter(ServletContext servletContext) {
        Object order = filterAttributes.get("order");
        String filterName = filterAttributes.getString(FILTER_NAME_ATTR);
        if (filterAttributes.containsKey("order")) {
            Integer temp = (Integer) order;
            filterName = temp + filterName;
        }

        logger.info("register servlet filter:{}", filter.getClass().getName());
        FilterRegistration.Dynamic filterRegistration = servletContext.addFilter(filterName, filter);
        AnnotationAttributes[] initParams = filterAttributes.getAnnotationArray(INIT_PARAMS_ATTR);
        if (!ObjectUtils.isEmpty(initParams)) {
            Arrays.stream(initParams).forEach(initParam -> {
                String name = initParam.getString("name");
                String value = initParam.getString("value");
                logger.info("init param:{},{}", name, value);
                filterRegistration.setInitParameter(name, value);
            });
        }

        String[] value = filterAttributes.getStringArray(VALUE_ATTR);
        String[] urlPatterns = filterAttributes.getStringArray(URL_PATTERNS_ATTR);
        Set<String> filteUrls = Sets.newHashSet();
        if (!ObjectUtils.isEmpty(value)) {
            filteUrls.addAll(Arrays.asList(value));
        }
        if (!ObjectUtils.isEmpty(urlPatterns)) {
            filteUrls.addAll(Arrays.asList(urlPatterns));
        }
        logger.info("filte urls:{}", filteUrls);
        filteUrls.addAll(filteUrls);

        String[] dispatcherTypeNames = filterAttributes.getStringArray(DISPATCHER_TYPES_ATTR);
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.noneOf(DispatcherType.class);
        if (!ObjectUtils.isEmpty(dispatcherTypeNames)) {
            for (String type : dispatcherTypeNames) {
                dispatcherTypes.add(DispatcherType.valueOf(type));
            }
        }
        filterRegistration.addMappingForUrlPatterns(dispatcherTypes, false, filteUrls.toArray(new String[filteUrls.size()]));
        filterRegistration.setAsyncSupported(filterAttributes.getBoolean(ASYNC_SUPPORTED_ATTR));

        String[] filteServletNames = filterAttributes.getStringArray(SERVLET_NAMES_ATTR);
        Set<String> filteServlts = Sets.newHashSet();
        if (!ObjectUtils.isEmpty(filteServletNames)) {
            filteServlts.addAll(Arrays.asList(filteServletNames));
        }
        logger.info("filte servlets:{}", filteServlts);
        filterRegistration.addMappingForServletNames(dispatcherTypes, false, filteServlts.toArray(new String[filteServlts.size()]));
    }
}
