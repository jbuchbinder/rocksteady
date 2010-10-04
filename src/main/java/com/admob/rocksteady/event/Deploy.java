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
