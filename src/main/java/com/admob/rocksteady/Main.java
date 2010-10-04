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
