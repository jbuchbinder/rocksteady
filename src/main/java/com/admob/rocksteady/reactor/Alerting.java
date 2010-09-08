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

import java.text.DecimalFormat;
import java.util.Date;
import java.math.BigDecimal;

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
public class Alerting implements UpdateListener {
  private static final Logger logger = LoggerFactory.getLogger(Alerting.class);

  private Object id;
  private String type;

  private String[] recipients = new String[0];

  private String subject = "Event Triggered";

  @Autowired
  private MailSender mailSender;

  @Autowired
  private SimpleMailMessage templateMessage;

  private String rrdtool;

  private String rsweb = "10.11.201.22";

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getSubject() {
    return subject;
  }

  public void setRecipients(String[] recipients) {
    this.recipients = recipients;
  }

  public String[] getRecipients() {
    return recipients;
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
    Date sqlDate = new Date();
    for (EventBean newEvent : newEvents) {
      i++;
      try {
        String msg = "";
        String _subject = "";
        // This is how string comparison is done in JAVA, not ==
        if (type.equals("log")) {
          String name = newEvent.get("name").toString();
          // String value = newEvent.get("value").toString();
          String error = newEvent.get("error").toString();
          String count = newEvent.get("count").toString();

          msg = name + " - " + error + " - " + count + " errors.";

        } else if (type.equals("latency_single")) {
          String hostname = newEvent.get("hostname").toString();
          String name = newEvent.get("name").toString();
          String value = newEvent.get("value").toString();
          String colo = newEvent.get("colo").toString();
          String app = newEvent.get("app").toString();

          String graphiteTs = newEvent.get("timestamp").toString();


          msg = colo + " - " + hostname + " - " + app + " - " + name + " - " + value;

          Threshold threshold = new Threshold();
          threshold.setName(name);
          threshold.setHostname(hostname);
          threshold.setColo(colo);
          threshold.setApp(app);
          threshold.setGraphiteTs(graphiteTs);
          threshold.setGraphiteValue(value);
          threshold.setCreateOn(sqlDate);
          threshold.persist();

          msg = msg + "\n" + "http://" + rsweb + "/rsweb/threshold.php?id="
              + String.valueOf(threshold.getId());
          _subject = this.subject + " - " + colo + " - " + hostname;


        } else if (type.equals("latency_multi")) {
          Metric l = (Metric) newEvent.get("latency");
          String l_hostname = l.getHostname();
          String l_value = l.getValue().toString();
          String l_name = l.getName();
          Metric w = (Metric) newEvent.get("win");
          String name = w.getName();
          String value = w.getValue().toString();
          String ts = w.getTimestamp().toString();
          String colo = w.getColo();
          String hostname = w.getHostname();
          String app = w.getApp();

          msg = l_hostname + " - " + l_name + " - " + l_value + " - " + app + " - " + name + " - "
              + value + " - " + ts;

          // Persistent the threshold cross event
          Threshold threshold = new Threshold();
          threshold.setName(name);
          threshold.setHostname(hostname);
          threshold.setColo(colo);
          threshold.setApp(app);
          threshold.setGraphiteTs(ts);
          threshold.setGraphiteValue(value);
          threshold.setCreateOn(sqlDate);
          threshold.persist();


        } else if (type.equals("deploy")) {
          String new_revision = newEvent.get("revision").toString();

          String colo = newEvent.get("colo").toString();
          String hostname = newEvent.get("hostname").toString();
          String app = newEvent.get("app").toString();

          Revision revision = new Revision();
          revision.setHostname(hostname);
          revision.setColo(colo);
          revision.setApp(app);
          revision.setCreateOn(sqlDate);
          revision.setRevision(new_revision);
          revision.persist();

          msg = "Revision changed on " + colo + " " + hostname + " to " + new_revision;
        } else if (type.equals("test")) {
          // String hostname = newEvent.get("hostname").toString();
          String colo = newEvent.get("colo").toString();
          // String app = newEvent.get("app").toString();
          // String timestamp = newEvent.get("timestamp").toString();
          // String revision = newEvent.get("revision").toString();
          // String diff = newEvent.get("diff").toString();
          String value = newEvent.get("value").toString();
          String name = newEvent.get("name").toString();

          msg = colo + " - " + name + " - " + value;

        } else if (type.equals("averagedThreshold")) {
          DecimalFormat df = new DecimalFormat("#.##");
          String colo = newEvent.get("colo").toString();
          String app = newEvent.get("app").toString();
          String value = df.format(newEvent.get("value"));
          String name = newEvent.get("name").toString();



          msg = app + " - " + colo + " - " + name + " - " + value;
          _subject = this.subject + " - " + msg;


        } else if (type.equals("count")) {
          String count = newEvent.get("count").toString();
          msg = i + " count: " + count;

        } else {
          String name = newEvent.get("name").toString();
          String value = newEvent.get("value").toString();
          String colo = newEvent.get("colo").toString();



          msg = " event triggered - type " + type + " - " + colo + " - " + name + " - " + value;

        }
        logger.debug(msg);

        // This email recipient iisn't empty, we send email out.
        if (recipients != null & recipients.length > 0) {
          logger.debug("Email sent");
          SimpleMailMessage mail = new SimpleMailMessage(this.templateMessage);

          mail.setTo(this.recipients);
          mail.setSubject(_subject);
          mail.setText(msg);
          this.mailSender.send(mail);

        }

      } catch (Exception e) {
        logger.error(
            "Trouble. Alert type: " + type + " " + newEvent.toString() + " - " + e.getMessage());
      }

    }
  }

}
