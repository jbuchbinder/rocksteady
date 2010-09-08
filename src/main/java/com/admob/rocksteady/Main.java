/**
 * Copyright 2010 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **/
package com.admob.rocksteady;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;

import com.admob.rocksteady.util.ContextHelper;
import com.admob.rocksteady.util.ServerInfo;
import com.admob.rocksteady.router.SpringManager;
import com.admob.rocksteady.Main;
import com.admob.rocksteady.util.ClassHelper;
import com.admob.rocksteady.common.Config;
import com.admob.rocksteady.common.DefaultConfig;

public class Main {
  // test
  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  private static final Config config = Config.getInstance();


  public static void main(String[] args) {
    logger.info("Running Rocksteady...");
    Main mainProc = new Main();
    try {
      mainProc.setRuntimeContext();
      mainProc.initialize();
      mainProc.start();
    } catch (Throwable xThrowable) {
      logger.error(xThrowable.getMessage(), xThrowable);
    }
  }

  private void setRuntimeContext() {
    System.setProperty(ContextHelper.SERVER_CONTEXT_KEY, ServerInfo.getServerName());
  }


  /*
   * This is to initiate Config object first, before we even initiate Spring
   */
  public void initProps() {
    try {
      Properties props = ClassHelper.getPropertiesFile(DefaultConfig.DEFAULT_PROPERTIES_FILE);
      // Set properties to config
      config.setRabbitHostname(props.getProperty("rabbitHostname"));
      config.setRabbitVirtualHost(props.getProperty("rabbitVirtualHost"));
      config.setRabbitExchange(props.getProperty("rabbitExchange"));
      config.setRabbitUser(props.getProperty("rabbitUser"));
      config.setRabbitPassword(props.getProperty("rabbitPassword"));

      logger.info("Read properties from " + DefaultConfig.DEFAULT_PROPERTIES_FILE);
    } catch (Throwable xThrowable) {
      logger.error(xThrowable.getMessage(), xThrowable);
      xThrowable.printStackTrace();
    }
  }

  void initialize() throws Exception {
    initProps();
    // Initialize the Spring Manager, singleton
    SpringManager.getInstance().initialize();
  }

  void start() throws Exception {
    // Start the Spring container and all Startable Service
    SpringManager.getInstance().start();

    logger.info("ROCKSTEADY Server Started Successfully");
  }

  public static Config getConfig() {
    return config;
  }

}
