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
