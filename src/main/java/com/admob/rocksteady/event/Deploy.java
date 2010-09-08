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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * POJO class to take on metrics
 *
 */
public class Deploy {
  private static final Logger logger = LoggerFactory.getLogger(Deploy.class);

  private Double revision;

  private Double timestamp;
  private String colo = "";
  private String hostname = "";
  private String app = "";

  // Instantiate metric object with a piece of graphite metric
  public Deploy() {

  }

  public Double getRevision() {
    return revision;
  }

  public void setRevision(Double revision) {
    this.revision = revision;
  }

  public Double getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Double timestamp) {
    this.timestamp = timestamp;
  }

  public String getColo() {
    return colo;
  }

  public void setColo(String colo) {
    this.colo = colo;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public String getApp() {
    return app;
  }

  public void setApp(String app) {
    this.app = app;
  }



}
