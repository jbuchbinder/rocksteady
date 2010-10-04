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
public class Latency {
  private static final Logger logger = LoggerFactory.getLogger(Latency.class);

  private String original;
  private String name;
  private Double value;
  private String timestamp;
  private String retention = "";
  private String colo = "";
  private String hostname = "";
  private String app = "";

  private Pattern metricPattern = Pattern.compile("(\\d+)\\s(\\S+)\\s(\\S+)");

  // Instantiate metric object with a piece of graphite metric
  public Latency() {

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

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
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

  public Pattern getMetricPattern() {
    return metricPattern;
  }

  public void setMetricPattern(Pattern metricPattern) {
    this.metricPattern = metricPattern;
  }

  public String toString() {
    return "Metric: " + "app = " + app + ";name = " + name + ";value = " + value;
  }


}
