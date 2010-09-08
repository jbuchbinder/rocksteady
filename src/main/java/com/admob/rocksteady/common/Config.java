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
package com.admob.rocksteady.common;


public class Config {
  private static final String CONFIG_PREFIX = "CONFIG:";
  private static final String DEFAULT_CONFIG = "DEFAULT:";
  private static final String GET = "get";

  private String rabbitVirtualHost;
  private String rabbitExchange;
  private String rabbitUser;
  private String rabbitPassword;
  private String rabbitHostname;

  private static Config config = null;

  /**
   * @return the rabbitServer
   */
  public String getRabbitHostname() {
    return rabbitHostname;
  }

  /**
   * @param rabbitServer the rabbitServer to set
   */
  public void setRabbitHostname(String rabbitHostname) {
    this.rabbitHostname = rabbitHostname;
  }


  /**
   * @return the rabbitExchange
   */
  public String getRabbitExchange() {
    return rabbitExchange;
  }

  /**
   * @param rabbitExchange the rabbitExchange to set
   */
  public void setRabbitExchange(String rabbitExchange) {
    this.rabbitExchange = rabbitExchange;
  }

  public void setRabbitVirtualHost(String rabbitVirtualHost) {
    this.rabbitVirtualHost = rabbitVirtualHost;
  }

  public String getRabbitVirtualHost() {
    return rabbitVirtualHost;
  }

  /**
   * @return the rabbitUser
   */
  public String getRabbitUser() {
    return rabbitUser;
  }

  /**
   * @param rabbitUser the rabbitUser to set
   */
  public void setRabbitUser(String rabbitUser) {
    this.rabbitUser = rabbitUser;
  }

  /**
   * @return the rabbitPassword
   */
  public String getRabbitPassword() {
    return rabbitPassword;
  }

  /**
   * @param rabbitPassword the rabbitPassword to set
   */
  public void setRabbitPassword(String rabbitPassword) {
    this.rabbitPassword = rabbitPassword;
  }



  private Config() {
  }

  /*
   * Get singleton instance
   */
  public static Config getInstance() {
    if (config == null) {
      config = new Config();
    }
    return config;
  }
}
