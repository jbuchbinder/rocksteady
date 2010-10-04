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

import java.util.Date;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.admob.rocksteady.domain.*;
import com.admob.rocksteady.event.*;

import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 * @author Implement Esper UpdateListener to be used when event is triggered.
 *         This is testing the base form.
 *
 */
public class Rrd implements UpdateListener {
  private static final Logger logger = LoggerFactory.getLogger(Rrd.class);

  private String rrdtool;


  public void setRrdtool(String rrdtool) {
    this.rrdtool = rrdtool;
  }


  public String getRrdtool() {
    return rrdtool;
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
    Integer i = 0;
    for (EventBean newEvent : newEvents) {
      i++;
      try {
        String msg = "";
        String value = newEvent.get("count").toString();

        msg = rrdtool + " update /tmp/revenue.rrd -t value N:" + value;
        logger.debug(msg);
        Runtime.getRuntime().exec(msg);

      } catch (Exception e) {
        logger.error("Rrd update error");
      }

    }
  }

}
