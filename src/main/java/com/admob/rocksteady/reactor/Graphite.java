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
package com.admob.rocksteady.reactor;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.admob.rocksteady.util.GraphiteInterface;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;


/**
 *
 * @author Implement Esper UpdateListener to be used when event is triggered.
 *         This is testing the base form.
 *
 */
public class Graphite implements UpdateListener {
  private static final Logger logger = LoggerFactory.getLogger(Graphite.class);

  private String type;
  private String tag;


  private String suffix;

  @Autowired
  private GraphiteInterface graphiteInterface;


  public void setType(String type) {
    this.type = type;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getTag() {
    return tag;
  }


  public String getSuffix() {
    return suffix;
  }

  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }

  /**
   * Handle the triggered event
   *
   * @param newEvents the new events in the window
   * @param oldEvents the old events in the window
   */
  public void update(EventBean[] newEvents, EventBean[] oldEvents) {

    if (newEvents == null) {
      return;
    }
    for (EventBean newEvent : newEvents) {
      try {
        String name = newEvent.get("name").toString();
        String value = newEvent.get("value").toString();
        String colo = newEvent.get("colo").toString();
        String retention = newEvent.get("retention").toString();
        String app = newEvent.get("app").toString();
        String gs;
        if (retention.isEmpty()) {
          gs = app + "." + name + "." + colo + "." + suffix + " " + value;
        } else {
          gs = retention + "." + app + "." + name + "." + colo + "." + suffix + " " + value;
        }

        if (gs == null) {
          logger.error("Null string detected");
        }

        logger.debug("graphite string:" + gs);

        // Send the data
        graphiteInterface.send(graphiteInterface.graphiteString(gs));

      } catch (Exception e) {
        // logger.error("Problem with sending metric to graphite: " +
        // newEvent.toString());
      }

    }
  }

}
