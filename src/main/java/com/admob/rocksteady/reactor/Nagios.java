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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import com.googlecode.jsendnsca.core.*;
import com.googlecode.jsendnsca.core.builders.*;


/**
 * 
 * @author Implement Esper UpdateListener to be used when event is triggered.  This is testing the base form.
 * 
 *
 */
public class Nagios implements UpdateListener  {
    private static final Logger logger = LoggerFactory.getLogger(Nagios.class);

    private String type;
    private String tag;
    private String service;
    private Integer level;
    
    @Autowired
    private NagiosPassiveCheckSender nagiosSender;
    
	public void setType(String type) {
		this.type = type;
	}

    public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}


	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getLevel() {
		return level;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getService() {
		return service;
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
    			String msg;
                logger.info(" event triggered - type " + type + " - " + colo + " - " + name + " - " + value);
                
                msg = " event triggered - type " + type + " - " + colo + " - " + name + " - " + value;
                
                // This builds the message to be sent to nagios.
                MessagePayload payload = new MessagePayloadBuilder()
                .withHostname("localhost")
                .withLevel(Level.CRITICAL)
                .withServiceName(service)
                .withMessage(msg)
                .create();
                
                nagiosSender.send(payload);
                 
        	} catch(NagiosException e) {
        		logger.error("Nagios sending exception: " + e.getMessage());
        	} catch(Exception e) {
        		logger.error("Problem with event: " + newEvent.toString());
        	}

        }
    }

}
