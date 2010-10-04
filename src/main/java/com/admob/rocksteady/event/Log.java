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
package com.admob.rocksteady.event;

import com.admob.rocksteady.router.cep.ComplexEventManager;
import java.util.regex.*;
import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * POJO class to take on metrics
 *
 */
public class Log implements Serializable {
  private static final Logger logger = LoggerFactory.getLogger(Log.class);

  static final long serialVersionUID = 00001L;
  private String name;
  private String error;
  private String value;
  private Integer status;
  private String hostname;
  private String timestamp;
  private String tag = "";

  public Log() {

  }

  public String toString() {
    return "name = " + name + ";value = " + value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getError() {
    return error;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getStatus() {
    return status;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getTag() {
    return tag;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
