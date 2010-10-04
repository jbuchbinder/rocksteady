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
