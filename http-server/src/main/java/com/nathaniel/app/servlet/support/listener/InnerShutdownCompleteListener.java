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
package com.nathaniel.app.servlet.support.listener;

import com.nathaniel.app.servlet.support.container.UndertowWrapper;
import io.undertow.server.handlers.GracefulShutdownHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InnerShutdownCompleteListener implements GracefulShutdownHandler.ShutdownListener {
    private static Logger   logger = LoggerFactory.getLogger(InnerShutdownCompleteListener.class);
    private UndertowWrapper undertow;

    @Override
    public void shutdown(boolean shutdownSuccessful) {
        logger.info("all request has bean processed,begin shutdown server...");
        if (shutdownSuccessful) {
            undertow.shutdown();
        }
        logger.info("server shutdown complete!");
    }

    public void setUndertowWrapper(UndertowWrapper undertow) {
        this.undertow = undertow;
    }
}
