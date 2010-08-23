/**
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/
package com.admob.rocksteady.router;

import com.admob.rocksteady.common.Service;
import com.admob.rocksteady.common.DefaultConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * This class is the integration point with the Spring framework.  It manages and bootstraps spring.
 */
public class SpringManager implements Service {

    private static final Logger logger = LoggerFactory.getLogger(SpringManager.class);
    private static SpringManager instance;
    private static ClassPathXmlApplicationContext applicationContext;
    private boolean initialized = false;

    private SpringManager() {
        logger.info("Spring Manager Created");
    }

    public static SpringManager getInstance() {
        if (instance == null) {
            instance = new SpringManager();
        }
        return instance;
    }

    public void initialize() {
        logger.info("Initializing service " + getDescription());
        applicationContext = new ClassPathXmlApplicationContext(new String[]{DefaultConfig.SPRING_CONTEXT_FILE});
        applicationContext.registerShutdownHook();
        initialized = true;
        logger.info(getDescription() + " initialized successfully");
    }

    public String getDescription() {
        return "Spring Manager";
    }

    public void start() {
        if (!initialized || applicationContext == null) {
            throw new IllegalStateException("Please initialized the SpringManager before starting it.");
        }
        applicationContext.start();
        
        logger.info("Spring Manager started");
        
    }


    public void shutdown() {
        logger.info("Shutting down service " + getDescription());
        applicationContext.close();
        logger.info(getDescription() + " was shutdown");
    }

    public Object getBean(String name) {
        if (!initialized || applicationContext == null) {
            throw new IllegalStateException("Please initialized the Spring Manager first.");
        }
        return applicationContext.getBean(name);
    }
}