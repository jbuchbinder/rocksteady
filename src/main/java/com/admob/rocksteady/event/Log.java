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
