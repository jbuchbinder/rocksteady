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
