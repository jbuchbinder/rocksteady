/**
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * @author Implement Esper UpdateListener to be used when event is triggered.  This is testing the base form.
 *
 */
public class Rrd implements UpdateListener  {
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

        	} catch(Exception e) {
        		logger.error("Rrd update error");
        	}

        }
    }

}
