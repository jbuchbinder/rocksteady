/**
 * Copyright 2010 Google Inc.

 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 **/
package com.admob.rocksteady.router;

import com.admob.rocksteady.common.Service;
import com.admob.rocksteady.common.DefaultConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * This class is the integration point with the Spring framework. It manages and
 * bootstraps spring.
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
    applicationContext =
        new ClassPathXmlApplicationContext(new String[] {DefaultConfig.SPRING_CONTEXT_FILE});
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
