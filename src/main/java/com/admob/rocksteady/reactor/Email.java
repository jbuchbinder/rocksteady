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

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 *
 * @author Implement Esper UpdateListener to be used when event is triggered.
 *         This is testing the base form.
 *
 */
public class Email implements UpdateListener {
  private static final Logger logger = LoggerFactory.getLogger(Email.class);

  private String type;
  private String tag;
  private String recipient;
  private String subject = "Event Triggered";
  private MailSender mailSender;
  private SimpleMailMessage templateMessage;

  public void setMailSender(MailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void setTemplateMessage(SimpleMailMessage templateMessage) {
    this.templateMessage = templateMessage;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getTag() {
    return tag;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public String getRecipient() {
    return recipient;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getSubject() {
    return subject;
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

        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);

        msg.setTo(this.recipient);

        logger.info(
            " event triggered - type " + type + " - " + colo + " - " + name + " - " + value);
        msg.setText(
            " event triggered - type " + type + " - " + colo + " - " + name + " - " + value);

        // this.mailSender.send(msg);

      } catch (Exception e) {
        logger.error("Problem with event: " + newEvent.toString());
      }

    }
  }

}
