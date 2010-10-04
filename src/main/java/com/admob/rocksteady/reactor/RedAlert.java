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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.admob.rocksteady.event.Type;

/**
 *
 * @author Implement Esper UpdateListener to be used when event is triggered.
 *         This is testing the base form.
 *
 */
public class RedAlert implements UpdateListener {
  private static final Logger logger = LoggerFactory.getLogger(RedAlert.class);

  private Object id;
  private Type type;
  private float threshold;
  private Object defaultId;

  /**
   * @param type the type to set
   */
  public void setType(Type type) {
    this.type = type;
  }

  /**
   * Set the default id for this listener. If not set then the id is extracted
   * from the Event.
   *
   * @param id the default id.
   */
  public void setDefaultId(String id) {
    this.defaultId = id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setThreshold(float threshold) {
    this.threshold = threshold;
  }

  public float getThreshold() {
    return threshold;
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

        logger.info(
            " Problem!! " + colo + " - " + name + " with " + value + " exceeded threshold of "
                + threshold);

      } catch (Exception e) {
        // logger.error(e.getCause().getMessage());
        logger.error("Problem with event: " + newEvent.toString());

      }

    }
  }

}
