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

// import com.admob.rocksteady.router.cep.ComplexEventManager;
import java.util.regex.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * POJO class to take on metrics
 *
 */
public class Metric {
  private static final Logger logger = LoggerFactory.getLogger(Metric.class);

  private String original;
  private String name;
  private Double value;
  private Double timestamp;
  private String retention = "";
  private String colo = "";
  private String hostname = "";
  private String app = "";

  // Instantiate metric object with a piece of graphite metric
  public Metric(String data) {

    this.original = data;
    // First split it by space
    String[] splitString = data.split("\\s+");
    if (splitString.length == 3) {

      // now we split it by period
      String[] splitName = splitString[0].split("\\.");

      this.hostname = splitName[splitName.length - 1];
      this.colo = splitName[splitName.length - 2];

      // Implode the rest of the array.
      StringBuffer sb = new StringBuffer();
      // Tracking splitName position for starting of key
      int p;

      if (splitName[0].matches("(1sec|10sec|1min|5min)")) {
        this.retention = splitName[0];
        this.app = splitName[1];
        p = 2;
      } else {
        this.app = splitName[0];
        // sb.append(splitName[0]);
        p = 1;
      }


      for (int i = p; i < splitName.length - 2; i++) {
        if (sb.length() > 0) {
          sb.append(".");
        }

        sb.append(splitName[i]);
      }

      this.name = sb.toString();
      this.value = new Double(splitString[1]);
      this.timestamp = new Double(splitString[2]);
      // logger.info("metric found: " + name);

      // Only send event if everything is cool
      // ComplexEventManager.getInstance().sendEvent(this);

    } else {
      logger.warn("metric not recognized: " + data);
    }

  }

  public void setOriginal(String graphite) {
    this.original = graphite;
  }

  public String getOriginal() {
    return original;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public Double getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Double timestamp) {
    this.timestamp = timestamp;
  }

  public String getRetention() {
    return retention;
  }

  public void setRetention(String retention) {
    this.retention = retention;
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

  public String toString() {
    return "hostname = " + hostname + ";app = " + app + ";name = " + name + ";value = " + value;
  }


}
